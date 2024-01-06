package online.bacovsky.trainingmanagement.domain.model

import android.content.Context
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.util.UiText
import online.bacovsky.trainingmanagement.util.removeAccents
import online.bacovsky.trainingmanagement.util.toLocalizedTimeFormat
import java.time.format.TextStyle
import java.util.Locale

data class  ClientWithScheduledTrainings(
    val client: Client,
    val trainings: List<Training>
) {
    fun trainingsToSmsText(context: Context): String {
        val builder = StringBuilder()

        builder
            .append(UiText.StringResource(R.string.sms_greeting).asString(context))
            .append(",")
            .append("\n")

        this.trainings.forEach { training: Training ->
            val dayName = training.startTime.dayOfWeek
                .getDisplayName(TextStyle.SHORT, Locale.getDefault())

            builder
                .append(dayName)
                .append(" ")
                .append(training.startTime.toLocalizedTimeFormat())
                .append("\n")
        }

        builder
            .append(UiText.StringResource(R.string.sms_footer).asString(context))

        return builder
            .toString()
            .removeAccents()
    }
}