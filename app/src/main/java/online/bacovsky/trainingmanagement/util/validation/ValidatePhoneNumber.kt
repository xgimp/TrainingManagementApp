package online.bacovsky.trainingmanagement.util.validation

import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.util.UiText

class ValidatePhoneNumber {

    fun execute(
        phoneNumber: String,
        existingPhoneNumbers: List<String>,
    ): ValidationResult {
        val phoneNumberRegex: Regex = "^(\\+420)? ?[1-9][0-9]{2} ?[0-9]{3} ?[0-9]{3}".toRegex()

        if (!phoneNumberRegex.matches(phoneNumber)) {
            return ValidationResult(
                success = false,
                errorMessage = UiText.StringResource(R.string.invalid_phone_number)
            )
        }

        if (existingPhoneNumbers.contains(phoneNumber)) {
            return ValidationResult(
                success = false,
                errorMessage = UiText.StringResource(R.string.phone_number_exists)
            )
        }

        return ValidationResult(
            success = true
        )
    }

}