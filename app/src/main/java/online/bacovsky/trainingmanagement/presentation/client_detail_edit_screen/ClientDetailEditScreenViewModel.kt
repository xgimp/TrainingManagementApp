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
import javax.inject.Inject

@HiltViewModel
class ClientDetailEditScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private var clientRepository: ClientRepository,
    private val validateName: ValidateName,
    private val validatePrice: ValidatePrice,
) : ViewModel() {

    var state by mutableStateOf(ClientEditFormState())

    private val clientDetailEventChannel = Channel<ClientDetailEvent>()
    val validationEvents = clientDetailEventChannel.receiveAsFlow()

   private val clientId = savedStateHandle.get<Long?>("clientId")!!

    var client by mutableStateOf<Client?>(null)
        private set

    init {
        viewModelScope.launch {
            client = clientRepository.getClientById(clientId)

            state = state.copy(
                id = client?.id,
                name = client?.name ?: "",
                price = client?.trainingPrice.toString(),
                balance = client?.balance
            )
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
        }
    }

    private fun submitForm() {
        val nameResult = validateName.execute(state.name)
        val priceResult = validatePrice.execute(state.price)

        state = state.copy(
            nameError = nameResult.errorMessage,
            priceError = priceResult.errorMessage,
        )

        val hasError = listOf(nameResult, priceResult).any { !it.success }
        if (hasError) { return }

        // form is valid
        viewModelScope.launch {
            clientRepository.update(
                Client(
                    id = client?.id,
                    name = state.name,
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