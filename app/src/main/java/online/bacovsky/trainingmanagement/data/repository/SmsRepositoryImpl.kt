package online.bacovsky.trainingmanagement.data.repository

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.telephony.SmsManager
import android.util.Log
import online.bacovsky.trainingmanagement.data.data_source.SmsHistoryDao
import online.bacovsky.trainingmanagement.domain.model.SmsHistory


class SmsRepositoryImpl(
    val context: Context,
    private val smsHistoryDao: SmsHistoryDao
): SmsRepository {

    @Suppress("DEPRECATION")
    private val smsManager: SmsManager = when {
        (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> context.getSystemService(SmsManager::class.java)
        else -> SmsManager.getDefault()
    }

    override suspend fun saveToHistory(sms: SmsHistory) {
        smsHistoryDao.insert(sms)
    }

    override fun sendSms(telNumber: String, smsText: String) {
        smsManager.sendTextMessage(telNumber, null, smsText, null, null)
        Log.d(TAG, "sendSms: sent sms: $smsText to telNumber $telNumber")
//        checkSmsState(telNumber, smsText)
        addSMSToContentProvider(telNumber, smsText)
    }

    private fun addSMSToContentProvider(telNumber: String, smsText: String) {
        // This is necessary for sent SMS to show in default SMS application
        val values = ContentValues()
        values.put("address", telNumber) // phone number to send
        values.put("date", System.currentTimeMillis().toString())
        values.put("read", "1") // if you want to mark is as unread set to 0
        values.put("type", "2") // 2 means sent message
        values.put("body", smsText)

        val uri = Uri.parse("content://sms/")
        context.contentResolver.insert(uri, values)
    }


}