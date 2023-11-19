package online.bacovsky.trainingmanagement.presentation.calendar_screen.add_training_modal

import online.bacovsky.trainingmanagement.domain.model.Client

sealed class AddTrainingEvent {
    data class ClientChanged(var client: Client) : AddTrainingEvent()

    data class Submit(val paymentNote: String) : AddTrainingEvent()
}
