package online.bacovsky.trainingmanagement.presentation.client_list_screen

sealed class SortOrder {
    data object NameAsc: SortOrder()
    data object NameDesc: SortOrder()
    data object ClosestTraining: SortOrder()
    data object LastPaymentDesc: SortOrder()
    data object AvailableTrainingsAsc: SortOrder()
    data object AvailableTrainingsDesc: SortOrder()
}

