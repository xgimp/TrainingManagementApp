package online.bacovsky.trainingmanagement.data.repository

import online.bacovsky.trainingmanagement.domain.model.CalendarEntity
import java.time.LocalDateTime

interface CalendarEventRepository {

    suspend fun getEventsBetweenTime(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        onSuccess: (List<CalendarEntity>) -> Unit
    )

}