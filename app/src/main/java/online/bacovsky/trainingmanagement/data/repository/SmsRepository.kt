package online.bacovsky.trainingmanagement.data.repository

import kotlinx.coroutines.flow.Flow
import online.bacovsky.trainingmanagement.domain.model.SmsHistory
import java.time.LocalDateTime

interface SmsRepository {
    suspend fun saveToHistory(sms: SmsHistory)

    fun getSmsSentInTimeRange(startDate: LocalDateTime, endTime: LocalDateTime): Flow<List<SmsHistory>>
}