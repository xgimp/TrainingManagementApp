package online.bacovsky.trainingmanagement.presentation.client_detail_edit_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import online.bacovsky.trainingmanagement.data.repository.ClientRepository
import online.bacovsky.trainingmanagement.domain.model.Client
import online.bacovsky.trainingmanagement.util.validation.ValidateName
import online.bacovsky.trainingmanagement.util.validation.ValidatePrice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import online.bacovsky.trainingmanagement.util.validation.ValidatePhoneNumber
import javax.inject.Inject

@HiltViewModel
class ClientDetailEditScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val clientRepository: ClientRepository,
    private val validateName: ValidateName,
    private val validatePrice: ValidatePrice,
    private val validatePhoneNumber: ValidatePhoneNumber,
) : ViewModel() {

    var state by mutableStateOf(ClientEditFormState())

    private val clientDetailEventChannel = Channel<ClientDetailEvent>()
    val validationEvents = clientDetailEventChannel.receiveAsFlow()

   private val clientId = savedStateHandle.get<Long?>("clientId")!!

    var client by mutableStateOf<Client?>(null)
        private set

    private var existingPhoneNumbers = emptyList<String>()

    init {
        viewModelScope.launch {
            client = clientRepository.getClientById(clientId)

            state = state.copy(
                id = client?.id,
                name = client?.name ?: "",
                phoneNumber = client?.telephoneNumber ?: "",
                price = client?.trainingPrice.toString(),
                balance = client?.balance
            )

            val currentClientExcluded = clientRepository.getAllPhoneNumbers().toMutableList()
            currentClientExcluded.remove(client!!.telephoneNumber)
            existingPhoneNumbers = currentClientExcluded.toList()
        }
    }

    fun onEvent(event: EditClientFormEvent) {
        when(event) {
            is EditClientFormEvent.OnNameChanged -> {
                state = state.copy(name = event.name)
            }
            is EditClientFormEvent.OnPricePerTrainingChanged -> {
                state = state.copy(price = event.price)
            }
            is EditClientFormEvent.Submit -> {
                submitForm()
            }
            is EditClientFormEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    val clientToDelete = clientRepository.getClientById(clientId)
                    clientToDelete?.let { clientRepository.delete(it) }
                    clientDetailEventChannel.send(ClientDetailEvent.ClientDeleted)
                }
            }
            is EditClientFormEvent.OnPhoneNumberChanged -> {
                state = state.copy(phoneNumber = event.phoneNumber)
            }
        }
    }

    private fun submitForm() {
        val nameResult = validateName.execute(state.name)
        val priceResult = validatePrice.execute(state.price)
        val phoneNumberResult = validatePhoneNumber.execute(state.phoneNumber, existingPhoneNumbers)

        state = state.copy(
            nameError = nameResult.errorMessage,
            priceError = priceResult.errorMessage,
            phoneNumberError = phoneNumberResult.errorMessage
        )

        val hasError = listOf(nameResult, priceResult, phoneNumberResult).any { !it.success }
        if (hasError) { return }

        // form is valid
        viewModelScope.launch {
            clientRepository.update(
                Client(
                    id = client?.id,
                    name = state.name,
                    telephoneNumber = state.phoneNumber,
                    trainingPrice = state.price.toLong(),
                    balance = client!!.balance
                )
            )
            clientDetailEventChannel.send(ClientDetailEvent.FormIsValid)
        }
    }

    sealed class ClientDetailEvent {
        data object FormIsValid: ClientDetailEvent()

        data object ClientDeleted: ClientDetailEvent()
    }
}