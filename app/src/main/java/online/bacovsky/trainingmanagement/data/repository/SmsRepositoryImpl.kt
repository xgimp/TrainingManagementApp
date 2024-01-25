package online.bacovsky.trainingmanagement.data.repository

import kotlinx.coroutines.flow.Flow
import online.bacovsky.trainingmanagement.data.data_source.SmsHistoryDao
import online.bacovsky.trainingmanagement.domain.model.SmsHistory
import java.time.LocalDateTime


class SmsRepositoryImpl(
    private val smsHistoryDao: SmsHistoryDao
): SmsRepository {

    override suspend fun saveToHistory(sms: SmsHistory) {
        smsHistoryDao.insert(sms)
    }

    override fun getSmsSentInTimeRange(
        startDate: LocalDateTime,
        endTime: LocalDateTime
    ): Flow<List<SmsHistory>> {
        return smsHistoryDao.getSmsSentInTimeRange(startDate, endTime)
    }

}
