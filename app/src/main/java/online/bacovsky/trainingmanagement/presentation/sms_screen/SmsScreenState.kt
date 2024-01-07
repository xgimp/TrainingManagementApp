package online.bacovsky.trainingmanagement.presentation.sms_screen

import online.bacovsky.trainingmanagement.domain.model.ClientWithScheduledTrainings
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

data class SmsScreenState(

    val smsToSendList: List<ClientWithScheduledTrainings> = emptyList(),

    val showConfirmDialog: Boolean = false,

    val numberOfSentSms: Float = 0f,

    val nexMonday: LocalDateTime = LocalDateTime.now()
        .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
        .toLocalDate()
        .atStartOfDay(),

    val nextSunday: LocalDateTime = nexMonday.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
        .toLocalDate()
        .atTime(LocalTime.MAX),
)
