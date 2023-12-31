package online.bacovsky.trainingmanagement.data.repository

import online.bacovsky.trainingmanagement.data.data_source.TrainingDao
import online.bacovsky.trainingmanagement.domain.model.Client
import online.bacovsky.trainingmanagement.domain.model.ClientWithScheduledTrainings
import online.bacovsky.trainingmanagement.domain.model.Training
import online.bacovsky.trainingmanagement.domain.model.TrainingWithClient
import java.time.LocalDateTime

class SmsDataRepositoryImpl(
    private val trainingDao: TrainingDao
): SmsDataRepository {
    override suspend fun getClientListWithTrainingsBetweenTime(startTime: LocalDateTime, endTime: LocalDateTime): List<ClientWithScheduledTrainings> {
        val clientTrainingMap: MutableMap<Client, MutableList<Training>> = mutableMapOf()

        val trainingWithClientList =  trainingDao.getTrainingsBetweenTime(startTime, endTime)
        trainingWithClientList.forEach { trainingWithClient: TrainingWithClient ->
            val client = trainingWithClient.client
            val training = trainingWithClient.training
            
            if (!clientTrainingMap.containsKey(client)) {
                clientTrainingMap[client] = mutableListOf()
            }
            clientTrainingMap[client]?.add(training)
        }
        return clientTrainingMap.map {
            ClientWithScheduledTrainings(
                client = it.key,
                trainings = it.value
            )
        }.sortedBy { it.client.name }
    }
}
