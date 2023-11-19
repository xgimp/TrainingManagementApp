package online.bacovsky.trainingmanagement.presentation.add_client_screen

import online.bacovsky.trainingmanagement.util.UiText

data class AddClientFormState(
    val name: String = "",
    val nameError: UiText.StringResource? = null,
    val price: String = "",
    val priceError: UiText.StringResource? = null,
    val funds: String = "",
    val fundsError: UiText.StringResource? = null,
    val fundsNote: String = ""
)