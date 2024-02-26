package online.bacovsky.trainingmanagement.data.repository

import online.bacovsky.trainingmanagement.domain.model.Client
import online.bacovsky.trainingmanagement.domain.model.ClientPayment
import kotlinx.coroutines.flow.Flow
import online.bacovsky.trainingmanagement.domain.model.ClientWithMetadata

interface ClientRepository {

    suspend fun insert(client: Client)

    suspend fun insertClientAndLogPayment(client: Client, paymentNote: String)

    suspend fun updateBalanceAndLogPayment(client: Client, newFunds: Long, fundsNote: String)

    suspend fun delete(client: Client)

    suspend fun getClientById(id: Long): Client?

    fun getAllActiveClients(): Flow<List<Client>>

    suspend fun update(client: Client)

    suspend fun getBalanceByClientId(clientId: Long): Long

    fun getPaymentsByClientId(clientId: Long): Flow<List<ClientPayment>>

    fun getClientsWithMetadata(): Flow<List<ClientWithMetadata>>

    suspend fun getAllPhoneNumbers(): List<String>

}