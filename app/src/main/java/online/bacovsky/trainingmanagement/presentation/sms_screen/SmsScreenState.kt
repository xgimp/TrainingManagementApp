package online.bacovsky.trainingmanagement.presentation.sms_screen

import online.bacovsky.trainingmanagement.domain.model.ClientWithScheduledTrainings

data class SmsScreenState(
    val smsToSendList: List<ClientWithScheduledTrainings> = emptyList(),

    val showConfirmDialog: Boolean = false,

    val numberOfSentSms: Float = 0f
)
