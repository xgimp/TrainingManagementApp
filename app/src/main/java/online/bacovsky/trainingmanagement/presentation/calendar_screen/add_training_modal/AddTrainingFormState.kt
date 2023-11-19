package online.bacovsky.trainingmanagement.presentation.calendar_screen.add_training_modal

import online.bacovsky.trainingmanagement.domain.model.Client
import online.bacovsky.trainingmanagement.util.UiText
import java.time.LocalDateTime

data class AddTrainingFormState(
    val client: Client? = null,
    val clientError: UiText.StringResource? = null,
    val dateTime: LocalDateTime? = null,
    val dateError: UiText.StringResource? = null,
    val timeError: UiText.StringResource? = null,
    val paymentNote: String = ""
)
