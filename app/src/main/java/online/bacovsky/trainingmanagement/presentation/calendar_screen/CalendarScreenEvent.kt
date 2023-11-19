package online.bacovsky.trainingmanagement.presentation.calendar_screen

import online.bacovsky.trainingmanagement.domain.model.CalendarEntity
import java.time.LocalDateTime

sealed class CalendarScreenEvent {
    data class OnEventClick(var calendarEntity: CalendarEntity) : CalendarScreenEvent()
    data class OnLoadMore(
        var startDate: LocalDateTime,
        var endDate: LocalDateTime
    ) : CalendarScreenEvent()

    data class OnRangeChanged(
        var startDate: LocalDateTime,
        var endDate: LocalDateTime
    ) : CalendarScreenEvent()

    data class OnEmptyViewClick(var time: LocalDateTime): CalendarScreenEvent()
    
    data object OnAddTrainingModalDismiss: CalendarScreenEvent()

    data class OnQuickCreate(val clientId: Long, val time: LocalDateTime, val paymentNote: String): CalendarScreenEvent()
}



