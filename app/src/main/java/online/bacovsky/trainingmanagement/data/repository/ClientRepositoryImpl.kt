package online.bacovsky.trainingmanagement.data.repository

import online.bacovsky.trainingmanagement.domain.model.Client
import online.bacovsky.trainingmanagement.data.data_source.ClientDao
import online.bacovsky.trainingmanagement.data.data_source.ClientPaymentDao
import online.bacovsky.trainingmanagement.domain.model.ClientPayment
import online.bacovsky.trainingmanagement.util.TransactionProvider
import kotlinx.coroutines.flow.Flow
import online.bacovsky.trainingmanagement.domain.model.ClientWithMetadata

class ClientRepositoryImpl(
    private val clientDao: ClientDao,
    private val clientPaymentDao: ClientPaymentDao,
    private val transactionProvider: TransactionProvider
): ClientRepository {

    override suspend fun insert(client: Client) {
        clientDao.insertClient(client)
    }

    override suspend fun insertClientAndLogPayment(client: Client, paymentNote: String) {
        transactionProvider.runAsTransaction {
            val clientId = clientDao.insertClient(client)

            clientPaymentDao.insert(
                ClientPayment(
                    clientId = clientId,
                    amount = client.balance,
                    note = paymentNote
                )
            )
        }
    }

    override suspend fun updateBalanceAndLogPayment(client: Client, newFunds: Long, fundsNote: String) {
        transactionProvider.runAsTransaction {
            clientDao.updateClient(client)

            clientPaymentDao.insert(
                ClientPayment(
                    clientId = client.id!!,
                    amount = newFunds,
                    note = fundsNote
                )
            )
        }
    }

    override suspend fun delete(client: Client) {
        clientDao.updateClient(client.copy(isDeleted = true))
    }

    override suspend fun getClientById(id: Long): Client? {
        return clientDao.getClientById(id)
    }

    override fun getAllActiveClients(): Flow<List<Client>> {
        return clientDao.getAllActiveClients()
    }

    override suspend fun update(client: Client) {
        clientDao.updateClient(client)
    }

    override suspend fun getBalanceByClientId(clientId: Long): Long {
       return clientPaymentDao.getBalanceByClientId(clientId)
    }

    override fun getPaymentsByClientId(clientId: Long): Flow<List<ClientPayment>> {
        return clientPaymentDao.getPaymentsByClientId(clientId)
    }

    override fun getClientsWithMetadata(): Flow<List<ClientWithMetadata>> {
        return clientDao.getClientsWithMetadata()
    }

    override suspend fun getAllPhoneNumbers(): List<String> {
        return clientDao.getAllPhoneNumbers()
    }

}