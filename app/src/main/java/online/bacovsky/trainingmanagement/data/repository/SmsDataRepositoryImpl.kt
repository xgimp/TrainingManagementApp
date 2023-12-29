package online.bacovsky.trainingmanagement.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import online.bacovsky.trainingmanagement.data.data_source.TrainingDao
import online.bacovsky.trainingmanagement.domain.model.Client
import online.bacovsky.trainingmanagement.domain.model.Training
import online.bacovsky.trainingmanagement.domain.model.TrainingWithClient
import java.time.LocalDateTime
import kotlin.math.log

class SmsDataRepositoryImpl(
    private val trainingDao: TrainingDao
): SmsDataRepository {
    override suspend fun getClientListWithTrainingsBetweenTime(startTime: LocalDateTime, endTime: LocalDateTime): Flow<Map<Client, List<Training>>> {
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
        clientTrainingMap.forEach { (client, trainings) ->
            Log.d(TAG, "getClientListWithTrainings: trainings for client $client:")
            trainings.forEach {
                Log.d(TAG, "getClientListWithTrainings: ${it.startTime} - ${it.endTime}")
            }
        }
        return flowOf(clientTrainingMap.mapValues { it.value.toList() })
    }
}