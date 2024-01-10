package online.bacovsky.trainingmanagement.data.data_source

import androidx.room.*
import online.bacovsky.trainingmanagement.domain.model.SmsHistory
import java.time.LocalDateTime

@Dao
interface SmsHistoryDao {

    @Insert
    suspend fun insert(sms: SmsHistory)

    @Query(
        "SELECT * FROM SmsHistory " +
        "WHERE startDate = :startDate " +
        "AND endDate = :endDate"
    )
    suspend fun getSmsSentInTimeRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<SmsHistory>
}