package online.bacovsky.trainingmanagement.data.repository

import android.util.Log
import androidx.room.Transaction
import online.bacovsky.trainingmanagement.data.data_source.ClientDao
import online.bacovsky.trainingmanagement.data.data_source.ClientPaymentDao
import online.bacovsky.trainingmanagement.data.data_source.TrainingDao
import online.bacovsky.trainingmanagement.domain.model.Client
import online.bacovsky.trainingmanagement.domain.model.ClientPayment
import online.bacovsky.trainingmanagement.domain.model.ClientWithScheduledTrainings
import online.bacovsky.trainingmanagement.domain.model.Training
import online.bacovsky.trainingmanagement.domain.model.TrainingWithClient
import online.bacovsky.trainingmanagement.util.TransactionProvider
import java.time.LocalDateTime


class TrainingRepositoryImpl (
    private val trainingDao: TrainingDao,
    private val clientPaymentDao: ClientPaymentDao,
    private val clientDao: ClientDao,
    private val transactionProvider: TransactionProvider
): TrainingRepository {

    override suspend fun insert(training: Training) {
        trainingDao.insert(training)
    }

    override suspend fun update(training: Training): Int {
        return trainingDao.update(training)
    }

    override suspend fun cancelTraining(training: Training): Int {
        return trainingDao.update(training.copy(isCanceled = true))
    }

    override suspend fun delete(training: Training) {
        trainingDao.delete(training)
    }

    override suspend fun getTrainingById(trainingId: Long): TrainingWithClient? {
        return trainingDao.getTrainingById(trainingId)
    }

    override suspend fun getAllTrainings(): List<TrainingWithClient> {
        return trainingDao.getAllTrainings()
    }

    @Transaction
    override suspend fun cancelTrainingAndLogPayment(
        trainingWithClient: TrainingWithClient,
        paymentNote: String
    ) {
        Log.d(TAG, "cancelTrainingAndLogPayment: Canceling training ${trainingWithClient.training}")
        transactionProvider.runAsTransaction {
            cancelTraining(trainingWithClient.training)

            Log.d(TAG, "cancelTrainingAndLogPayment: clients current balance: ${trainingWithClient.client.balance}")
            Log.d(TAG, "cancelTrainingAndLogPayment: returning ${trainingWithClient.training.price}")
            // return money for canceled training
            val updatedClient = trainingWithClient.client.copy(
                balance = (trainingWithClient.client.balance + trainingWithClient.training.price)
            )
            clientDao.updateClient(updatedClient)

            Log.d(TAG, "cancelTrainingAndLogPayment: inserting ClientPayment")
            clientPaymentDao.insert(
                ClientPayment(
                    clientId = trainingWithClient.client.id!!,
                    amount = trainingWithClient.training.price,
                    note = paymentNote
                )
            )
        }
    }

    @Transaction
    override suspend fun insertTrainingAndLogTransaction(training: TrainingWithClient, paymentNote: String) {
        transactionProvider.runAsTransaction {
            // insert training
            trainingDao.insert(training.training)

            // change clients balance
            val updatedClient = training.client.copy(
                balance = (training.client.balance - training.client.trainingPrice)
            )
            clientDao.updateClient(updatedClient)

            // insert payment log
            clientPaymentDao.insert(
                ClientPayment(
                    clientId = training.client.id!!,
                    amount = training.training.price * -1,
                    note = paymentNote
                )
            )
        }
    }

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