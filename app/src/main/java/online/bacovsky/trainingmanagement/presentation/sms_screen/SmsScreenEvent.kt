package online.bacovsky.trainingmanagement.presentation.sms_screen

import android.content.Context

sealed class SmsScreenEvent {
    data class OnBulkSmsSendClick(val context: Context): SmsScreenEvent()
}