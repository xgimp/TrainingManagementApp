package online.bacovsky.trainingmanagement.data.repository

import androidx.room.Transaction
import online.bacovsky.trainingmanagement.domain.model.Training
import online.bacovsky.trainingmanagement.domain.model.TrainingWithClient

interface TrainingRepository {

    suspend fun insert(training: Training)

    suspend fun update(training: Training): Int

    suspend fun cancelTraining(training: Training): Int

    suspend fun delete(training: Training)

    suspend fun getTrainingById(trainingId: Long): TrainingWithClient?

    suspend fun getAllTrainings(): List<TrainingWithClient>

    @Transaction
    suspend fun cancelTrainingAndLogPayment(trainingWithClient: TrainingWithClient, paymentNote: String)
    
    @Transaction
    suspend fun insertTrainingAndLogTransaction(training: TrainingWithClient, paymentNote: String)
}