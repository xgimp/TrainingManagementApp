package online.bacovsky.trainingmanagement.data.data_source

import androidx.room.*
import online.bacovsky.trainingmanagement.domain.model.Client
import kotlinx.coroutines.flow.Flow

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
}