package online.bacovsky.trainingmanagement.util

import android.util.Log
import androidx.compose.ui.graphics.toArgb
import online.bacovsky.trainingmanagement.domain.model.CalendarEntity
import online.bacovsky.trainingmanagement.domain.model.TrainingWithClient
import online.bacovsky.trainingmanagement.ui.theme.futureCalendarEvent
import online.bacovsky.trainingmanagement.ui.theme.lowBalanceEventColor
import com.alamkanak.weekview.WeekView
import online.bacovsky.trainingmanagement.ui.theme.inDebtEventColor
import online.bacovsky.trainingmanagement.ui.theme.zeroBalanceEventColor
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar
import java.util.Date

private const val TAG = "DateExtensions"

internal fun Calendar.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(timeInMillis).atZone(ZoneId.systemDefault()).toLocalDate()
}

internal fun Calendar.toLocalDateTime(): LocalDateTime {
    return Instant.ofEpochMilli(timeInMillis).atZone(ZoneId.systemDefault()).toLocalDateTime()
}

internal fun LocalDate.toCalendar(): Calendar {
    val instant = atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()
    val calendar = Calendar.getInstance()
    calendar.time = Date.from(instant)
    return calendar
}

internal fun LocalDateTime.toCalendar(): Calendar {
    val instant = atZone(ZoneId.systemDefault()).toInstant()
    val calendar = Calendar.getInstance()
    calendar.time = Date.from(instant)
    return calendar
}

internal fun LocalDateTime.toLocalizedTimeFormat(): String {
    val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
    val localTime =  LocalTime.of(this.hour, this.minute)
    return localTime.format(timeFormatter)
}

internal fun LocalDateTime.toLocalizedDateTimeFormat(): String {
    val timeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
    val localDateTime =  LocalDateTime.of(this.year, this.month, this.dayOfMonth, this.hour, this.minute)
    return localDateTime.format(timeFormatter)
}

internal fun LocalDate.toLocalizedFormat(): String {
    val pattern = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
    return this.format(pattern)
}

fun TrainingWithClient.toCalendarEntity(): CalendarEntity.Event {
    // TODO: extend MaterialTheme with custom colors
    val isInDebt = client.balance < 0
    val isZeroBalance = client.balance == 0L
    val isLowBalance = client.balance <= client.trainingPrice
    val isPastStartTime = training.startTime <= LocalDateTime.now()

    val eventColor = when {
        isZeroBalance && isPastStartTime -> zeroBalanceEventColor.darkenBy(50f)
        isZeroBalance -> zeroBalanceEventColor
        isInDebt && isPastStartTime -> inDebtEventColor.darkenBy(50f)
        isInDebt -> inDebtEventColor
        isLowBalance && !isPastStartTime -> lowBalanceEventColor
        isPastStartTime && isLowBalance -> lowBalanceEventColor.darkenBy(50f)
        isPastStartTime -> futureCalendarEvent.darkenBy(50f)
        else -> futureCalendarEvent
    }


    return CalendarEntity.Event(
        id = training.id!!,
        title = client.name.uppercase(),
        startTime = training.startTime.toCalendar(),
        endTime = training.endTime.toCalendar(),
        color = eventColor.toArgb(),
        isCanceled = training.isCanceled
    )
}

fun WeekView.scrollToClosestStartDayOfWeek(dateTime: LocalDateTime) {
    Log.d(TAG, "scrollToClosestMondayOf: got date: $dateTime")
    val thisWeek = dateTime.toCalendar()

    if (thisWeek[Calendar.DAY_OF_WEEK] == Calendar.SATURDAY || thisWeek[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY) {
        Log.d(TAG, "scrollToClosestMondayOf: Weekday YAY")
        val nextWeek = thisWeek.toLocalDateTime()
            .plusDays(2)
            .toCalendar()

        Log.d(TAG, "scrollToClosestMondayOf: skipped to ${nextWeek.time}")
        nextWeek[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        this.scrollToDate(nextWeek)
        return
    }
    thisWeek[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
    this.scrollToDate(thisWeek)
}