package online.bacovsky.trainingmanagement.presentation.add_client_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import online.bacovsky.trainingmanagement.domain.model.Client
import online.bacovsky.trainingmanagement.data.repository.ClientRepository
import online.bacovsky.trainingmanagement.util.validation.ValidateFunds
import online.bacovsky.trainingmanagement.util.validation.ValidateName
import online.bacovsky.trainingmanagement.util.validation.ValidatePrice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import online.bacovsky.trainingmanagement.util.validation.ValidatePhoneNumber
import javax.inject.Inject

const val TAG = "AddClientViewModel"

@HiltViewModel
class AddClientViewModel @Inject constructor(
    private val validateName: ValidateName,
    private val validatePrice: ValidatePrice,
    private val validateFunds: ValidateFunds,
    private val validatePhoneNumber: ValidatePhoneNumber,
    private val repository: ClientRepository
): ViewModel() {

    var state by mutableStateOf(AddClientFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: AddClientFormEvent) {
        when(event) {
            is AddClientFormEvent.NameChanged -> {
                state = state.copy(name = event.name)
            }
            is AddClientFormEvent.PriceChanged -> {
                state = state.copy(price = event.price)
            }
            is AddClientFormEvent.FundsChanged -> {
                state = state.copy(
                    funds = event.funds,
                    fundsNote = event.fundsNote
                )
            }
            is AddClientFormEvent.Submit -> {
                submitForm()
            }
            is AddClientFormEvent.PhoneNumberChanged -> {
                state = state.copy(phoneNumber = event.phoneNumber)
            }
        }
    }

    private fun submitForm() {
        val nameResult = validateName.execute(state.name)
        val priceResult = validatePrice.execute(state.price)
        val fundsResult = validateFunds.execute(state.funds)
        val phoneNumberResult = validatePhoneNumber.execute(state.phoneNumber)

        state = state.copy(
            nameError = nameResult.errorMessage,
            priceError = priceResult.errorMessage,
            fundsError = fundsResult.errorMessage,
            phoneNumberError = phoneNumberResult.errorMessage
        )

        val hasError = listOf(nameResult, priceResult, fundsResult, phoneNumberResult).any { !it.success }
        if (hasError) { return }

        // form is valid
        viewModelScope.launch {
            repository.insertClientAndLogPayment(
                client = Client(
                    name = state.name.trim(),
                    trainingPrice = state.price.toLong(),
                    balance = state.funds.toLong(),
                    telephoneNumber = state.phoneNumber
                ),
                paymentNote = state.fundsNote
            )
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    sealed class ValidationEvent {
        data object Success: ValidationEvent()
    }
}