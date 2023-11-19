package online.bacovsky.trainingmanagement.presentation.calendar_screen

import online.bacovsky.trainingmanagement.domain.model.CalendarEntity

data class CalendarEntityEventState(
    val entities: List<CalendarEntity> = emptyList(),
)