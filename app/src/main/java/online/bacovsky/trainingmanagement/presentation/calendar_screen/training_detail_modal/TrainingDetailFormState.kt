package online.bacovsky.trainingmanagement.presentation.calendar_screen.training_detail_modal

import online.bacovsky.trainingmanagement.domain.model.TrainingWithClient

data class TrainingDetailFormState(
    val training: TrainingWithClient? = null,
    val isCanceled: Boolean = false,
    val paymentNote: String = ""
)
