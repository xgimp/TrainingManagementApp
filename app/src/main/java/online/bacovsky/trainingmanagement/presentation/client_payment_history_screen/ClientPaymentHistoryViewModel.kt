package online.bacovsky.trainingmanagement.presentation.client_payment_history_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import online.bacovsky.trainingmanagement.data.repository.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


private const val TAG = "ClientPaymentHistoryViewModel"

@HiltViewModel
class ClientPaymentHistoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    clientRepository: ClientRepository
): ViewModel() {

    private val clientId = savedStateHandle.get<Long>("clientId")!!
    val payments = clientRepository.getPaymentsByClientId(clientId)

}