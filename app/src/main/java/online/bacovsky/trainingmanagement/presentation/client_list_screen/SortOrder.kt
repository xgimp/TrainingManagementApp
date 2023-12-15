package online.bacovsky.trainingmanagement.presentation.client_list_screen

sealed class SortOrder(val displayName: String) {
    data object NameAsc: SortOrder("By Name ASC")
    data object NameDesc: SortOrder("By Name Desc")
    data object ClosestTraining: SortOrder("By Closest Training")
    data object LastPaymentDesc: SortOrder("By Last Payment")
}

