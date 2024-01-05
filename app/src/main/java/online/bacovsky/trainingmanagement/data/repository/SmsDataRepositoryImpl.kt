package online.bacovsky.trainingmanagement.data.repository

import android.content.Context
import android.os.Build
import android.telephony.SmsManager
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts

class SmsDataRepositoryImpl(
    context: Context
): SmsRepository {

    private val smsManager: SmsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        context.getSystemService(SmsManager::class.java)
    } else {
        SmsManager.getDefault()
    }

    override fun sendSms(telNumber: String, smsText: String) {
        smsManager.sendTextMessage(telNumber, null, smsText, null, null)
        Log.d(TAG, "sendSms: sending sms to: $telNumber")
        Log.d(TAG, "sendSms: sms text: $smsText")
    }

}
