package online.bacovsky.trainingmanagement.presentation.calendar_screen

import android.graphics.RectF
import android.util.Log
import androidx.compose.runtime.MutableState
import online.bacovsky.trainingmanagement.domain.model.CalendarEntity
import online.bacovsky.trainingmanagement.domain.model.toWeekViewEntity
import online.bacovsky.trainingmanagement.presentation.main_screen.SelectedClientState
import online.bacovsky.trainingmanagement.util.toLocalizedDateTimeFormat
import com.alamkanak.weekview.WeekViewEntity
import com.alamkanak.weekview.jsr310.WeekViewPagingAdapterJsr310
import java.time.LocalDate
import java.time.LocalDateTime

class MyCustomPagingAdapter(
    private val eventHandler: (event: CalendarScreenEvent) -> Unit,
    private val dragHandler: (Long, LocalDateTime, LocalDateTime) -> Unit,
    private val selectedClient: MutableState<SelectedClientState>,
    private val defaultPaymentNote: String
) : WeekViewPagingAdapterJsr310<CalendarEntity>() {

    override fun onCreateEntity(item: CalendarEntity): WeekViewEntity = item.toWeekViewEntity()

    override fun onLoadMore(startDate: LocalDate, endDate: LocalDate) {
        eventHandler(
            CalendarScreenEvent.OnLoadMore(
                startDate = startDate.atStartOfDay(),
                endDate = endDate.atStartOfDay()
            )
        )
    }

    override fun onRangeChanged(firstVisibleDate: LocalDate, lastVisibleDate: LocalDate) {
        super.onRangeChanged(firstVisibleDate, lastVisibleDate)
        Log.d(TAG, "onRangeChanged: range changed to $firstVisibleDate - $lastVisibleDate")
        eventHandler(
            CalendarScreenEvent.OnRangeChanged(
                startDate = firstVisibleDate.atStartOfDay(),
                endDate = lastVisibleDate.atStartOfDay()
            )
        )
    }
    override fun onEventClick(data: CalendarEntity, bounds: RectF) {
        if (data is CalendarEntity.Event) {
            eventHandler(CalendarScreenEvent.OnEventClick(data))
        }
    }

    override fun onEmptyViewClick(time: LocalDateTime) {
        if (selectedClient.value.id != -1L) {
            val paymentNote = defaultPaymentNote.replace("{time}", time.withMinute(0).toLocalizedDateTimeFormat())
            eventHandler(CalendarScreenEvent.OnQuickCreate(selectedClient.value.id, time, paymentNote))
        }
        else {
            eventHandler(CalendarScreenEvent.OnEmptyViewClick(time))
        }
    }

    override fun onDragAndDropFinished(data: CalendarEntity, newStartTime: LocalDateTime, newEndTime: LocalDateTime) {
        if (data is CalendarEntity.Event) {
            dragHandler(data.id, newStartTime, newEndTime)
        }
    }

}