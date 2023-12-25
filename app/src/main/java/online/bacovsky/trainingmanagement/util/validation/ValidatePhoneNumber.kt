package online.bacovsky.trainingmanagement.util.validation

import android.util.Patterns
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.util.UiText

class ValidatePhoneNumber {
    companion object {
        const val MIN_PRICE = 0L
    }

    fun execute(phoneNumber: String): ValidationResult {
        val phoneNumberRegex: Regex = "^(\\+420)? ?[1-9][0-9]{2} ?[0-9]{3} ?[0-9]{3}".toRegex()

        if (!phoneNumberRegex.matches(phoneNumber)) {
            return ValidationResult(
                success = false,
                errorMessage = UiText.StringResource(R.string.invalid_phone_number)
            )
        }
        return ValidationResult(
            success = true
        )
    }


}