package online.bacovsky.trainingmanagement.util.validation

import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.domain.model.Client
import online.bacovsky.trainingmanagement.util.UiText

class ValidateClient {

    fun execute(client: Client?): ValidationResult {
        if (client == null || client.id == -1L) {
            return ValidationResult(
                success = false,
                errorMessage = UiText.StringResource(R.string.select_client)
            )
        }
        return ValidationResult(success = true)
    }
}