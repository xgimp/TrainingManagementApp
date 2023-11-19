package online.bacovsky.trainingmanagement.presentation.calendar_screen

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.presentation.main_screen.SelectedClientState
import online.bacovsky.trainingmanagement.util.scrollToClosestStartDayOfWeek
import online.bacovsky.trainingmanagement.util.toLocalDateTime
import com.alamkanak.weekview.WeekView
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreenTopAppBar(
    mWeekView: WeekView,
    drawerState: DrawerState,
    selectedClient: MutableState<SelectedClientState>
) {

    val scope = rememberCoroutineScope()

    TopAppBar(
        title = {
            Text(
                text = if (selectedClient.value.id == -1L) stringResource(id = R.string.calendar_screen_title) else selectedClient.value.name,
                maxLines = 2
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        actions = {
            IconButton(onClick = {
                // On Today click
                mWeekView.scrollToClosestStartDayOfWeek(
                    LocalDateTime.now()
                )
            }) {
                Icon(Icons.Outlined.Today, contentDescription = "Scroll to Today")
            }
            IconButton(onClick = {
                // On Previous week click
                mWeekView.scrollToClosestStartDayOfWeek(
                    mWeekView.firstVisibleDate
                        .toLocalDateTime()
                        .minusDays(mWeekView.numberOfVisibleDays.toLong())
                )
            }) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "Scroll 5 days back")
            }
            IconButton(onClick = {
                // On next week click
                mWeekView.scrollToClosestStartDayOfWeek(
                    mWeekView.lastVisibleDate
                        .toLocalDateTime()
                        .plusDays(1L)
                )
            }) {
                Icon(Icons.Outlined.ArrowForward, contentDescription = "Scroll 5 days forward")
            }
            if (selectedClient.value.id != -1L) {
                IconButton(
                    onClick = {
                        // cancel quick create
                        Log.d(TAG, "CalendarScreen: clicked on close")
                        selectedClient.value = selectedClient.value.copy(id = -1, name = "")
                        Log.d(TAG, "CalendarScreen: $selectedClient")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Cancel"
                    )
                }
            }
        }
    )
}