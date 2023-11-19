package online.bacovsky.trainingmanagement.util.validation

import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.util.UiText

class ValidateName {

    fun execute(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(
                success = false,
                errorMessage = UiText.StringResource(R.string.name_error)
            )
        }
        return ValidationResult(
            success = true
        )
    }
    
}