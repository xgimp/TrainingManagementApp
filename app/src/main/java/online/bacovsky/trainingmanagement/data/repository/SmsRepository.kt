package online.bacovsky.trainingmanagement.data.repository

import online.bacovsky.trainingmanagement.domain.model.SmsHistory

interface SmsRepository {
    fun sendSms(telNumber: String, smsText: String)

    suspend fun saveToHistory(sms: SmsHistory)
}