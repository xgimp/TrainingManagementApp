package online.bacovsky.trainingmanagement.util.validation

import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.util.UiText
import java.time.LocalTime

class ValidateTrainingStartTime {
    fun execute(time: LocalTime?): ValidationResult {
        if (time == null) {
            return ValidationResult(
                success = false,
                errorMessage = UiText.StringResource(R.string.time_error)
            )
        }
        return ValidationResult(success = true)
    }
}