package online.bacovsky.trainingmanagement.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Sms
import androidx.compose.ui.graphics.vector.ImageVector
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.util.Routes
import online.bacovsky.trainingmanagement.util.UiText

sealed class BottomNavItem(
    var title: UiText.StringResource,
    var icon: ImageVector,
    var screenRoute: String
) {
    data object Home : BottomNavItem(
        title = UiText.StringResource(R.string.bottom_nav_item_calendar),
        icon = Icons.Outlined.CalendarMonth,
        screenRoute = Routes.CALENDAR_SCREEN
    )

    data object Clients : BottomNavItem(
        title = UiText.StringResource(R.string.bottom_nav_item_clients),
        icon = Icons.Outlined.People,
        screenRoute = Routes.CLIENT_LIST_SCREEN
    )

    data object Sms : BottomNavItem(
        title = UiText.StringResource(R.string.sms),
        icon = Icons.Outlined.Sms,
        screenRoute = Routes.SMS_SCREEN
    )

    data object Backups : BottomNavItem(
        title = UiText.StringResource(R.string.bottom_nav_item_backups),
        icon = Icons.Outlined.Backup,
        screenRoute = Routes.BACKUP_SCREEN
    )
}