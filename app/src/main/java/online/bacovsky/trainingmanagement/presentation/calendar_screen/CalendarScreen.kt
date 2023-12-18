package online.bacovsky.trainingmanagement.presentation.calendar_screen

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.presentation.calendar_screen.add_training_modal.AddTrainingModalWindow
import online.bacovsky.trainingmanagement.presentation.calendar_screen.training_detail_modal.TrainingDetailEvent
import online.bacovsky.trainingmanagement.presentation.calendar_screen.training_detail_modal.TrainingDetailModalWindow
import online.bacovsky.trainingmanagement.presentation.main_screen.SelectedClientState
import online.bacovsky.trainingmanagement.ui.theme.gray_300
import online.bacovsky.trainingmanagement.ui.theme.gray_600
import online.bacovsky.trainingmanagement.util.UiEvent
import online.bacovsky.trainingmanagement.util.UiText
import online.bacovsky.trainingmanagement.util.px
import online.bacovsky.trainingmanagement.util.scrollToClosestStartDayOfWeek
import com.alamkanak.weekview.WeekView
import com.alamkanak.weekview.jsr310.scrollToDateTime
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar
import java.util.Locale

const val TAG = "CalendarScreen"

@Composable
fun CalendarScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    drawerState: DrawerState,
    viewModel: CalendarScreenViewModel = hiltViewModel(),
    selectedClient: MutableState<SelectedClientState>,
    dateToGo: String?

) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val DEFAULT_MIN_HOUR = viewModel.DEFAULT_MIN_HOUR
    val DEFAULT_MAX_HOUR = viewModel.DEFAULT_MAX_HOUR

    LaunchedEffect(key1 = true) {
        viewModel.canceledTrainingEvents.collect{event ->
            when(event) {
                is CalendarScreenViewModel.TrainingCanceledEvent.Success -> {
                    snackbarHostState.currentSnackbarData?.dismiss()

                    event.message?.let {message ->
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = message.asString(context),
                                withDismissAction = true,
                                duration = SnackbarDuration.Long
                            )
                        }
                    }
                }
            }
        }
    }

    // TODO: move this elsewhere
    val defaultPaymentNote = UiText.StringResource(
        resourceId = R.string.training_payment,
        args = arrayOf("{time}")
    ).asString()

    val myAdapter = remember {
        MyCustomPagingAdapter(
            dragHandler = viewModel::handleDrag,
            eventHandler = viewModel::onEvent,
            selectedClient = selectedClient,
            defaultPaymentNote = defaultPaymentNote
        )
    }
    val calendarColors = hashMapOf(
        "timeColumnTextColor" to MaterialTheme.colorScheme.outline.toArgb(),
        "headerTextColor" to MaterialTheme.colorScheme.outline.toArgb(),
        "weekendHeaderTextColor" to MaterialTheme.colorScheme.error.toArgb(),
        "todayBackgroundColor" to MaterialTheme.colorScheme.primaryContainer.toArgb(),
        "futureWeekendBackgroundColor" to MaterialTheme.colorScheme.errorContainer.toArgb(),
        "columSeparatorColor" to gray_300.toArgb(),
        "headerTextColor" to gray_600.toArgb()
    )

    val mWeekView = remember {
        WeekView(context).apply {
            adapter = myAdapter
            stickToActualWeek = false
            numberOfVisibleDays = 5
            minHour = DEFAULT_MIN_HOUR  // scrollToHour does not work if minHour is set
            maxHour = DEFAULT_MAX_HOUR
            showHeaderBottomLine = true
            showNowLine = true
            showNowLineDot = true
            showTimeColumnSeparator = true
            timeColumnSeparatorWidth = 1.px
            allDayEventTextSize = 13.px
            columnGap = 2.px
            overlappingEventGap = 2.px
            eventCornerRadius = 4.px
            headerBottomLineWidth = 1.px
            headerBottomLineColor = calendarColors.getOrDefault("columSeparatorColor", Color.Unspecified.toArgb())
            headerPadding = 12.px
            headerTextSize = 13.px
            hourHeight = 60.px
            timeColumnSeparatorColor = calendarColors.getOrDefault("columSeparatorColor", Color.Unspecified.toArgb())
            timeColumnTextColor = calendarColors.getOrDefault("timeColumnTextColor", Color.Unspecified.toArgb())
            headerTextColor = calendarColors.getOrDefault("headerTextColor", Color.Unspecified.toArgb())
            weekendHeaderTextColor = calendarColors.getOrDefault("weekendHeaderTextColor", Color.Unspecified.toArgb())
            todayBackgroundColor = calendarColors.getOrDefault("todayBackgroundColor", Color.Unspecified.toArgb())
            pastWeekendBackgroundColor = calendarColors.getOrDefault("futureWeekendBackgroundColor", Color.Unspecified.toArgb())
            futureWeekendBackgroundColor = calendarColors.getOrDefault("futureWeekendBackgroundColor", Color.Unspecified.toArgb())
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.Navigate -> onNavigate(event)
                is UiEvent.ShowSnackbar -> {
                    scope.launch {
                        // close previous snackbar(s) if exists
                        snackbarHostState.currentSnackbarData?.dismiss()

                        val snackbarResult = snackbarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action,
                            duration = SnackbarDuration.Indefinite,
                            withDismissAction = true
                        )
                        when(snackbarResult) {
                            SnackbarResult.ActionPerformed -> {
                                TODO("Not implemented yet")
                            }
                            else -> {}
                        }
                    }
                }
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CalendarScreenTopAppBar(
                selectedClient = selectedClient,
                drawerState = drawerState,
                mWeekView = mWeekView
            )
        },
    ) { paddingValues ->
        WeekViewCalendar(
            weekView = mWeekView,
            adapter = myAdapter,
            viewModel = viewModel,
            paddingValues = paddingValues,
            dateToGo = dateToGo
        )
    }

}


@Composable
fun WeekViewCalendar(
    viewModel: CalendarScreenViewModel,
    paddingValues: PaddingValues,
    weekView: WeekView,
    dateToGo: String?,
    adapter: MyCustomPagingAdapter
) {

    val scope = rememberCoroutineScope()
    val calendarEntityEventState = viewModel.calendarEntities
    val showAddTrainingModal = viewModel.showAddTrainingModal
    val showTrainingDetailModal = viewModel.showTrainingDetailModal
    val clientList = viewModel.clients.collectAsState(initial = emptyList())

    if (showAddTrainingModal) {
        AddTrainingModalWindow(
            onDismiss = {
                viewModel.onEvent(CalendarScreenEvent.OnAddTrainingModalDismiss)
            },
            state = viewModel.addTrainingFormState,
            choices = clientList,
            eventHandler = viewModel::onAddTrainingEvent,
        )
    }

    if (showTrainingDetailModal) {
        TrainingDetailModalWindow(
            onDismiss = {
                viewModel.onTrainingDetailEvent(TrainingDetailEvent.OnDismiss)
            },
            eventHandler = viewModel::onTrainingDetailEvent,
            state = viewModel.trainingDetailFormState
        )
    }

    AndroidView(
        modifier = Modifier.padding(paddingValues),
        factory = {
            weekView.setDateFormatter {
                val day = it[Calendar.DAY_OF_MONTH]
                val month = it[Calendar.MONTH] + 1  // months indexed from 0
                val year = it[Calendar.YEAR]

                val dateString = String.format("%d-%d-%d", year, month, day)
                val date = SimpleDateFormat("yyyy-M-d", Locale.getDefault()).parse(dateString)
                val dayName = SimpleDateFormat("EE", Locale.getDefault()).format(date!!)

                "${dayName.uppercase()}\n$day.$month"
            }

            weekView.setTimeFormatter { hour: Int ->
                val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                val localTime = LocalTime.of(hour, 0)
                localTime.format(timeFormatter)
            }

            Log.d(TAG, "WeekViewCalendar: dateToGo: $dateToGo")

            if (dateToGo != null) {
                weekView.also {
                    Log.d(TAG, "WeekViewCalendar: got date to go to")
                    it.post {
                        Log.d(TAG, "WeekViewCalendar: going to date $dateToGo")
                        it.scrollToClosestStartDayOfWeek(LocalDateTime.parse(dateToGo))
                    }
                }
            }
            else {
                weekView.also {
                    it.post {
                        it.scrollToDateTime(LocalDateTime.now())
                    }
                }
            }
            // return the view
            weekView
        }
    ) {
        scope.launch {
            adapter.submitList(calendarEntityEventState.entities)
        }
    }
}
