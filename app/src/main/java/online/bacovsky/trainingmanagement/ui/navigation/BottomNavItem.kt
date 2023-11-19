package online.bacovsky.trainingmanagement.ui.navigation

import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.util.Routes
import online.bacovsky.trainingmanagement.util.UiText

sealed class BottomNavItem(
    var title : UiText.StringResource,
    var icon : Int,
    var screenRoute : String
) {
    data object Home : BottomNavItem(
        title = UiText.StringResource(resourceId = R.string.bottom_nav_item_calendar),
        icon = R.drawable.ic_outline_calendar_month_24,
        screenRoute = Routes.CALENDAR_SCREEN
    )

    data object Clients : BottomNavItem(
        title = UiText.StringResource(R.string.bottom_nav_item_clients),
        icon = R.drawable.ic_outline_people_24,
        screenRoute = Routes.CLIENT_LIST_SCREEN
    )

    data object Backups : BottomNavItem(
        title = UiText.StringResource(R.string.bottom_nav_item_backups),
        icon = R.drawable.ic_outline_backup_24,
        screenRoute = Routes.BACKUP_SCREEN
    )
}