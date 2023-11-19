package online.bacovsky.trainingmanagement.presentation.add_client_screen


sealed class AddClientFormEvent {
    data class NameChanged(val name: String): AddClientFormEvent()

    data class PriceChanged(val price: String): AddClientFormEvent()

    data class FundsChanged(val funds: String, val fundsNote: String): AddClientFormEvent()

    data object Submit: AddClientFormEvent()
}
