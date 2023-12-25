package online.bacovsky.trainingmanagement.presentation.client_list_screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import online.bacovsky.trainingmanagement.data.repository.ClientRepository
import online.bacovsky.trainingmanagement.domain.model.Client
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
    private val repository: ClientRepository
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

    private var lastDeletedClient: Client? = null

    fun onEvent(event: ClientListEvent) {
        when(event) {
            is ClientListEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    repository.delete(event.client)
                    lastDeletedClient = event.client
                    sendUiEvent(
                        UiEvent.ShowSnackbar(
                            message = "Client deleted",
                            action = "Undo"
                        )
                    )
                }
            }
            is ClientListEvent.OnAddClientClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_CLIENT))
            }
            is ClientListEvent.OnClickOnClientRowItem -> {
                // Fixme: use navArgs correctly
                sendUiEvent(
                    UiEvent.Navigate(
                    Routes.CLIENT_DETAIL_EDIT
                        .replace("{clientId}", "${event.clientId}")
                ))
            }
            is ClientListEvent.OnUndoDeleteClick -> {
                val client = lastDeletedClient?.copy(isDeleted = false)
                viewModelScope.launch {
                    client?.let {
                        repository.update(it)
                        lastDeletedClient = null
                    }
                }
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