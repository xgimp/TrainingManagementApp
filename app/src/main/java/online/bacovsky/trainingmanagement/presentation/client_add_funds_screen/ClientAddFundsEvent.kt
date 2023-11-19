package online.bacovsky.trainingmanagement.presentation.client_add_funds_screen

sealed class ClientAddFundsEvent {

    data object OnBackButtonClick: ClientAddFundsEvent()

    data class OnFundsChanged(val funds: String = "", val fundsNote: String = ""): ClientAddFundsEvent()

    data object OnSaveClick: ClientAddFundsEvent()

}
