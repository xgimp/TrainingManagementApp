package online.bacovsky.trainingmanagement.domain.model

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.text.style.TypefaceSpan
import com.alamkanak.weekview.WeekViewEntity
import java.util.Calendar


sealed class CalendarEntity {

    data class Event(
        val id: Long,
        val title: CharSequence,
        val startTime: Calendar,
        val endTime: Calendar,
        val color: Int,
        val isCanceled: Boolean
    ) : CalendarEntity()

}

fun CalendarEntity.getEventId() : Long {
    /**
        Gets Event id. Event id == Training id
    */
    return when (this) {
        is CalendarEntity.Event -> {
            this.id
        }
    }
}

fun CalendarEntity.toWeekViewEntity(): WeekViewEntity {
    return when (this) {
        is CalendarEntity.Event -> toWeekViewEntity()
    }
}

fun CalendarEntity.Event.toWeekViewEntity(): WeekViewEntity {
    val backgroundColor = if (!isCanceled) color else Color.WHITE
    val textColor = if (!isCanceled) Color.WHITE else color

    val style = WeekViewEntity.Style.Builder()
        .setTextColor(textColor)
        .setBackgroundColor(backgroundColor)
        .build()

    val title = SpannableStringBuilder(title).apply {
        val titleSpan = TypefaceSpan("sans-serif-medium")
        setSpan(titleSpan, 0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        if (isCanceled) {
            setSpan(StrikethroughSpan(), 0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    return WeekViewEntity.Event.Builder(this)
        .setId(id)
        .setTitle(title)
        .setStartTime(startTime)
        .setEndTime(endTime)
        .setStyle(style)
        .build()
}
