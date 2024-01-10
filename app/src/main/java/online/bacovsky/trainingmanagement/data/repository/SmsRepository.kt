package online.bacovsky.trainingmanagement.data.repository

import online.bacovsky.trainingmanagement.domain.model.SmsHistory
import java.time.LocalDateTime

interface SmsRepository {
    fun sendSms(telNumber: String, smsText: String)

    suspend fun saveToHistory(sms: SmsHistory)

    suspend fun getSmsSentInTimeRange(startDate: LocalDateTime, endTime: LocalDateTime): List<SmsHistory>
}