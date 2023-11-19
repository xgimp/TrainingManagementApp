package online.bacovsky.trainingmanagement.presentation.client_detail_edit_screen

import online.bacovsky.trainingmanagement.util.UiText

data class ClientEditFormState(
    val id: Long? = null,
    val name: String = "",
    val nameError: UiText.StringResource? = null,
    val price: String = "",
    val priceError: UiText.StringResource? = null,
    val balance: Long? = null
)