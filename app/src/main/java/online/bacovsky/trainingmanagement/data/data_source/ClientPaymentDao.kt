package online.bacovsky.trainingmanagement.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import online.bacovsky.trainingmanagement.domain.model.ClientPayment
import kotlinx.coroutines.flow.Flow


@Dao
interface ClientPaymentDao {

    @Transaction
    @Insert
    suspend fun insert(clientPayment: ClientPayment)

    @Query(
        "SELECT sum(amount) " +
        "FROM ClientPayment " +
        "WHERE clientId = :clientId"
    )
    suspend fun getBalanceByClientId(clientId: Long): Long

    @Query(
        "SELECT * " +
        "FROM ClientPayment " +
        "WHERE clientId = :clientId " +
        "ORDER BY createdAt DESC"
    )
    fun getPaymentsByClientId(clientId: Long): Flow<List<ClientPayment>>

}