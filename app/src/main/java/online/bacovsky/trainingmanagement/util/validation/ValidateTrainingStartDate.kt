package online.bacovsky.trainingmanagement.util.validation

import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.util.UiText
import java.time.LocalDate

class ValidateTrainingStartDate {
    fun execute(date: LocalDate?): ValidationResult {
        if (date == null) {
            return ValidationResult(
                success = false,
                errorMessage = UiText.StringResource(R.string.date_error)
            )
        }
        return ValidationResult(success = true)
    }
}