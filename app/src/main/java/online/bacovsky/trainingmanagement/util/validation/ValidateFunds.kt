package online.bacovsky.trainingmanagement.util.validation

import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.util.UiText

class ValidateFunds {

    fun execute(funds: String): ValidationResult {
        funds.toLongOrNull()
            ?: return ValidationResult(
                success = false,
                errorMessage = UiText.StringResource(R.string.price_error)
            )

        return ValidationResult(
            success = true
        )
    }
    
}