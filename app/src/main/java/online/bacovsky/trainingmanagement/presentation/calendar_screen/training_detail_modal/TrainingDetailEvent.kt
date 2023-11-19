package online.bacovsky.trainingmanagement.presentation.calendar_screen.training_detail_modal

sealed class TrainingDetailEvent {

    data class OnCanceledClick(val isCanceled: Boolean) : TrainingDetailEvent()

    data class OnNotChargeFee(val paymentNote: String) : TrainingDetailEvent()

    data object OnDismiss : TrainingDetailEvent()

    data object OnChargeFee : TrainingDetailEvent()

}
