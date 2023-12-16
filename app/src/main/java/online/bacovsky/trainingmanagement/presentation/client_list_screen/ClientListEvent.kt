package online.bacovsky.trainingmanagement.presentation.client_list_screen

import online.bacovsky.trainingmanagement.domain.model.Client

sealed class ClientListEvent {
    data class OnDeleteClick(val client: Client): ClientListEvent()
    data object OnUndoDeleteClick: ClientListEvent()
    data object OnAddClientClick: ClientListEvent()
    data class OnClickOnClientRowItem(val clientId: Long?): ClientListEvent()
    data object OnSortButtonClick: ClientListEvent()

}
