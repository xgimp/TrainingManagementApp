package online.bacovsky.trainingmanagement.data.repository

interface SmsRepository {
    fun sendSms(telNumber: String, smsText: String)
}