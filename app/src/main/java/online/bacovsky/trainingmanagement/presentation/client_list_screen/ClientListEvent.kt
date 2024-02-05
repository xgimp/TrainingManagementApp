package online.bacovsky.trainingmanagement.presentation.client_list_screen

import online.bacovsky.trainingmanagement.domain.model.Client

sealed class ClientListEvent {
    data class OnDeleteClick(val client: Client): ClientListEvent()
    data object OnUndoDeleteClick: ClientListEvent()
    data object OnAddClientClick: ClientListEvent()
    data class OnClickOnClientRowItem(val clientId: Long?): ClientListEvent()
    data object OnSortButtonClick: ClientListEvent()
    data object OnSmsSendClick : ClientListEvent()
    data class OnSortByNameAscClick(val displayName: String): ClientListEvent()
    data class OnSortByNameDescClick(val displayName: String): ClientListEvent()
    data class OnSortByClosestTrainingClick(val displayName: String): ClientListEvent()
    data class OnSortByLastPaymentDescClick(val displayName: String): ClientListEvent()
    data class OnSortByAvailableTrainingsAscClick(val displayName: String): ClientListEvent()
    data class OnSortByAvailableTrainingsDescClick(val displayName: String): ClientListEvent()

}
