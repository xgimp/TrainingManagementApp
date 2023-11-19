package online.bacovsky.trainingmanagement.data.repository

import android.util.Log
import online.bacovsky.trainingmanagement.data.data_source.TrainingDao
import online.bacovsky.trainingmanagement.domain.model.CalendarEntity
import online.bacovsky.trainingmanagement.util.toCalendarEntity
import java.time.LocalDateTime


const val TAG = "CalendarEventRepositoryImpl"

class CalendarEventRepositoryImpl(
    private val dao: TrainingDao
): CalendarEventRepository {

    override suspend fun getEventsBetweenTime(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        onSuccess: (List<CalendarEntity>) -> Unit
    ) {
        val newStartTime = startTime.minusMonths(1)
        val newEndTime = endTime.plusMonths(2)
        Log.d(TAG, "getEventsBetweenTime: getting events for dates $newStartTime - $newEndTime")

        val calendarEntities = mutableListOf<CalendarEntity>()
        dao.getTrainingsBetweenTime(
            startTime = newStartTime,
            endTime = newEndTime
        ).forEach {
            calendarEntities.add(it.toCalendarEntity())
        }
        onSuccess(calendarEntities.toList())
    }
}

