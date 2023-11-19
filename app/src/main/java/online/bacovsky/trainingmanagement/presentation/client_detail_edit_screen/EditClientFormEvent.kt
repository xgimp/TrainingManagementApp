package online.bacovsky.trainingmanagement.presentation.client_detail_edit_screen


sealed class EditClientFormEvent {
    data class OnNameChanged(val name: String): EditClientFormEvent()

    data class OnPricePerTrainingChanged(val price: String): EditClientFormEvent()

    data object Submit: EditClientFormEvent()

    data object OnDeleteClick: EditClientFormEvent()
}
