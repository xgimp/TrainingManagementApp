package online.bacovsky.trainingmanagement.presentation.client_list_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import online.bacovsky.trainingmanagement.data.repository.ClientRepository
import online.bacovsky.trainingmanagement.util.Routes
import online.bacovsky.trainingmanagement.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "ClientListViewModel"

@HiltViewModel
class ClientListViewModel @Inject constructor(
    repository: ClientRepository
): ViewModel() {
    val clients = repository.getAllActiveClients()

    val clientsWithMetadata = repository.getClientsWithMetadata()

    private val _uiEvent = Channel<UiEvent>()
    val iuEvent = _uiEvent.receiveAsFlow()

    var isSortOrderMenuExpanded by mutableStateOf(false)
        private set

    var currentSortOrder by mutableStateOf<SortOrder>(SortOrder.NameAsc)
        private set

    var currentSortOrderDisplayName by mutableStateOf("")
        private set

    fun onEvent(event: ClientListEvent) {
        when(event) {
            is ClientListEvent.OnAddClientClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_CLIENT))
            }
            is ClientListEvent.OnClickOnClientRowItem -> {
                sendUiEvent(
                    UiEvent.Navigate(
                    Routes.CLIENT_DETAIL_EDIT
                        .replace("{clientId}", "${event.clientId}")
                ))
            }
            is ClientListEvent.OnSmsSendClick -> {
                event.client

            }
            is ClientListEvent.OnSortButtonClick -> {
                isSortOrderMenuExpanded = !isSortOrderMenuExpanded
            }
            is ClientListEvent.OnSortByAvailableTrainingsAscClick -> {
                currentSortOrder = SortOrder.AvailableTrainingsAsc
                currentSortOrderDisplayName = event.displayName
            }
            is ClientListEvent.OnSortByAvailableTrainingsDescClick -> {
                currentSortOrder = SortOrder.AvailableTrainingsDesc
                currentSortOrderDisplayName = event.displayName
            }
            is ClientListEvent.OnSortByClosestTrainingClick -> {
                currentSortOrder = SortOrder.ClosestTraining
                currentSortOrderDisplayName = event.displayName
            }
            is ClientListEvent.OnSortByLastPaymentDescClick -> {
                currentSortOrder = SortOrder.LastPaymentDesc
                currentSortOrderDisplayName = event.displayName
            }
            is ClientListEvent.OnSortByNameAscClick -> {
                currentSortOrder = SortOrder.NameAsc
                currentSortOrderDisplayName = event.displayName
            }
            is ClientListEvent.OnSortByNameDescClick -> {
                currentSortOrder = SortOrder.NameDesc
                currentSortOrderDisplayName = event.displayName
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}