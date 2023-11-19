package online.bacovsky.trainingmanagement.presentation.client_add_funds_screen

import online.bacovsky.trainingmanagement.util.UiText

data class ClientAddFundsFormState(
    val funds: String = "",
    val fundsNote: String = "",
    val fundsError: UiText.StringResource? = null
)
