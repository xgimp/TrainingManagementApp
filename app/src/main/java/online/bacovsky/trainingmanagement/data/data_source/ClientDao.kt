package online.bacovsky.trainingmanagement.data.data_source

import androidx.room.*
import online.bacovsky.trainingmanagement.domain.model.Client
import kotlinx.coroutines.flow.Flow
import online.bacovsky.trainingmanagement.domain.model.ClientWithMetadata

@Dao
interface ClientDao {

    @Insert
    suspend fun insertClient(client: Client): Long

    @Update
    suspend fun updateClient(client: Client): Int

    @Delete
    suspend fun deleteClient(client: Client)

    @Query("SELECT * FROM Client WHERE id = :id")
    suspend fun getClientById(id: Long): Client?

    @Query(
        "SELECT * " +
        "FROM Client " +
        "WHERE Client.isDeleted = 0 " +
        "ORDER BY Client.name COLLATE NOCASE ASC"
    )
    fun getAllActiveClients(): Flow<List<Client>>

    @Query("WITH ClosestTraining AS(\n" +
            "  SELECT \n" +
            "    c.id AS client_id, \n" +
            "    c.name AS client_name, \n" +
            "    c.balance AS client_balance, \n" +
            "    c.trainingPrice AS client_training_price, \n" +
            "    CASE WHEN MIN(t.id) IS NOT NULL THEN MIN(\n" +
            "      CASE WHEN t.startTime >= datetime('now') \n" +
            "      AND t.isCanceled = 0 THEN t.id ELSE NULL END\n" +
            "    ) ELSE NULL END AS closest_training_id \n" +
            "  FROM \n" +
            "    Client AS c \n" +
            "    LEFT JOIN Training AS t ON c.id = t.clientId \n" +
            "  WHERE \n" +
            "    c.isDeleted = 0 \n" +
            "  GROUP BY \n" +
            "    c.id, \n" +
            "    c.name\n" +
            "), \n" +
            "LastClientPayment AS (\n" +
            "  SELECT \n" +
            "    clientId AS client_id, \n" +
            "    MAX(createdAt) AS last_payment_time \n" +
            "  FROM \n" +
            "    ClientPayment \n" +
            "  GROUP BY \n" +
            "    clientId\n" +
            ") \n" +
            "SELECT \n" +
            "  c.client_id AS clientId, \n" +
            "  c.client_name AS clientName, \n" +
            "  c.client_balance AS clientBalance, \n" +
            "  c.client_training_price AS clientTrainingPrice, \n" +
            "  (c.client_balance /  c.client_training_price) AS availableTrainings,\n" +
            "  t.startTime AS closestTrainingStartAt, \n" +
            "  p.last_payment_time AS lastPaymentAt \n" +
            "FROM \n" +
            "  ClosestTraining AS c \n" +
            "  LEFT JOIN Training AS t ON c.closest_training_id = t.id \n" +
            "  LEFT JOIN LastClientPayment AS p ON c.client_id = p.client_id")
    fun getClientsWithMetadata(): Flow<List<ClientWithMetadata>>
}