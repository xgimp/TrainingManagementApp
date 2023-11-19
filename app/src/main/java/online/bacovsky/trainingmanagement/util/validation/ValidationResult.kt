package online.bacovsky.trainingmanagement.util.validation

import online.bacovsky.trainingmanagement.util.UiText

data class ValidationResult(
    val success: Boolean,
    val errorMessage: UiText.StringResource? = null
)
