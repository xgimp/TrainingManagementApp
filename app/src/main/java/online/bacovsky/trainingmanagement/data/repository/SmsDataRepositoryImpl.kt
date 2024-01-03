package online.bacovsky.trainingmanagement.data.repository

import android.content.Context
import android.telephony.SmsManager

class SmsDataRepositoryImpl(
    private val context: Context
): SmsRepository {

    private val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)

    override fun sendSms(telNumber: String, smsText: String) {
        TODO("Not yet implemented")
    }

}
