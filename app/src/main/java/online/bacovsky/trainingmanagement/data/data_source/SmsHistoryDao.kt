package online.bacovsky.trainingmanagement.data.data_source

import androidx.room.*
import online.bacovsky.trainingmanagement.domain.model.SmsHistory

@Dao
interface SmsHistoryDao {

    @Insert
    suspend fun insert(sms: SmsHistory)

}