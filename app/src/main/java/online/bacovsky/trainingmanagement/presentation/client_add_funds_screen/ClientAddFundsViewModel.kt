package online.bacovsky.trainingmanagement.presentation.client_add_funds_screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import online.bacovsky.trainingmanagement.data.repository.ClientRepository
import online.bacovsky.trainingmanagement.util.Routes
import online.bacovsky.trainingmanagement.util.UiEvent
import online.bacovsky.trainingmanagement.util.validation.ValidateFunds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ClientPaymentListViewModel"


@HiltViewModel
class ClientAddFundsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val validateFunds: ValidateFunds,
    private val clientRepository: ClientRepository
): ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val clientId = savedStateHandle.get<Long?>("clientId")!!
    val payments = clientRepository.getPaymentsByClientId(clientId)

    var state by mutableStateOf(ClientAddFundsFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: ClientAddFundsEvent) {
        when(event) {
            is ClientAddFundsEvent.OnBackButtonClick -> {
                sendUiEvent(
                    UiEvent.Navigate(
                        Routes.CLIENT_DETAIL_EDIT
                            .replace("{clientId}", clientId.toString())
                    )
                )
            }
            is ClientAddFundsEvent.OnFundsChanged -> {
                Log.d(TAG, "onEvent: funds Changed: ${event.funds}")
                state = state.copy(funds = event.funds, fundsNote = event.fundsNote)
            }
            is ClientAddFundsEvent.OnSaveClick -> {
                submitForm()
            }
        }
    }

    private fun submitForm() {
        val fundsResult = validateFunds.execute(state.funds)

        state = state.copy(
            fundsError = fundsResult.errorMessage
        )

        val hasError = listOf(fundsResult).any { !it.success }
        if (hasError) { return }

        // form is valid
        viewModelScope.launch {
            val client = clientRepository.getClientById(clientId)
            client?.let { updatedClient ->
                clientRepository
                    .updateBalanceAndLogPayment(
                        client = updatedClient.copy(balance = (client.balance + state.funds.toLong())),
                        newFunds = state.funds.toLong(),
                        fundsNote = state.fundsNote
                    )
            }
            validationEventChannel.send(ValidationEvent.Success)
            // remove value from funds input
            state = state.copy(funds = "")
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    sealed class ValidationEvent {
        data object Success: ValidationEvent()
    }
}