package online.bacovsky.trainingmanagement.util.validation

import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.util.UiText

class ValidatePrice {
    companion object {
        const val MIN_PRICE = 0L
    }

    fun execute(price: String): ValidationResult {
        val priceValue = price.toLongOrNull()
            ?: return ValidationResult(
                success = false,
                errorMessage = UiText.StringResource(R.string.price_error)
            )

        if (priceValue < MIN_PRICE) {
            return ValidationResult(
                success = false,
                errorMessage = UiText.StringResource(
                    resourceId = R.string.price_too_low_error,
                    args = arrayOf(MIN_PRICE)
                )
            )
        }
        return ValidationResult(
            success = true
        )
    }


}