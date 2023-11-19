package online.bacovsky.trainingmanagement.util

object Routes {
    // bottomNav routes
    const val CALENDAR_SCREEN = "calendar_screen/?dateToGo={dateToGo}"
    const val CLIENT_LIST_SCREEN = "client_list_screen"
    const val BACKUP_SCREEN = "backup_screen"

    // Client related routes
    const val ADD_CLIENT = "client/add"
    const val CLIENT_DETAIL_EDIT = "client/{clientId}/detail"
    const val CLIENT_FUNDS_SCREEN = "client/{clientId}/add_funds"
    const val CLIENT_PAYMENT_HISTORY_SCREEN = "client/{clientId}/payment_history"
}