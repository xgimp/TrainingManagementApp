package online.bacovsky.trainingmanagement.presentation.add_client_screen

sealed class AddClientEvent {
    data class OnNameChange(val name: String): AddClientEvent()
    data class OnPriceChange(val price: String): AddClientEvent()
    data class OnFundsChange(val funds: String): AddClientEvent()
    object OnSaveClientClick: AddClientEvent()
}
