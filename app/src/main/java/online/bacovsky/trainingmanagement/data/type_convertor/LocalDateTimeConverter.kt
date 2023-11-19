package online.bacovsky.trainingmanagement.data.type_convertor

import androidx.room.TypeConverter
import java.time.LocalDateTime

class LocalDateTimeConverter {
    @TypeConverter
    fun timeToString(time: LocalDateTime): String {
        return time.toString()
    }

    @TypeConverter
    fun stringToTime(time: String): LocalDateTime {
        return LocalDateTime.parse(time)
    }
}