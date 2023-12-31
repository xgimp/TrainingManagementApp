package online.bacovsky.trainingmanagement.data.repository

import kotlinx.coroutines.flow.Flow
import online.bacovsky.trainingmanagement.domain.model.Client
import online.bacovsky.trainingmanagement.domain.model.ClientWithScheduledTrainings
import online.bacovsky.trainingmanagement.domain.model.Training
import java.time.LocalDateTime

interface SmsDataRepository {

    suspend fun getClientListWithTrainingsBetweenTime(
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ) : List<ClientWithScheduledTrainings>

}