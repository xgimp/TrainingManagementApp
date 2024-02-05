package online.bacovsky.trainingmanagement.presentation.sms_screen

import android.content.Context

sealed class SmsScreenEvent {
    data object OnBulkSmsSendButtonClicked: SmsScreenEvent()

    data object OnBulkSmsSendDismissButtonClicked: SmsScreenEvent()

    data class OnConfirmSendClicked(val context: Context): SmsScreenEvent()
}