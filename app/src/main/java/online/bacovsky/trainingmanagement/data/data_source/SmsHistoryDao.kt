package online.bacovsky.trainingmanagement.data.data_source

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import online.bacovsky.trainingmanagement.domain.model.SmsHistory
import java.time.LocalDateTime

@Dao
interface SmsHistoryDao {

    @Insert
    suspend fun insert(sms: SmsHistory)

    @Query(
        "SELECT * FROM SmsHistory " +
        "WHERE sentAt BETWEEN :startDate AND :endDate"
    )
    fun getSmsSentInTimeRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<SmsHistory>>
}