package online.bacovsky.trainingmanagement.data.data_source

import androidx.room.*
import online.bacovsky.trainingmanagement.domain.model.Training
import online.bacovsky.trainingmanagement.domain.model.TrainingWithClient
import java.time.LocalDateTime

@Dao
interface TrainingDao {

    @Insert
    suspend fun insert(training: Training)

    @Update
    @Transaction
    suspend fun update(training: Training): Int

    @Delete
    suspend fun delete(training: Training)

    @Transaction
    @Query(
        "SELECT Training.* FROM Training " +
        "JOIN Client ON Client.id == Training.clientId " +
        "WHERE Training.startTime " +
        "AND Training.isCanceled = 0 " +
        "AND Client.isDeleted = 0"
    )
    suspend fun getAllTrainings(): List<TrainingWithClient>

    @Transaction
    @Query(
        "SELECT Training.* from Training " +
        "JOIN Client on Client.id == Training.clientId " +
        "WHERE Training.startTime " +
        "BETWEEN :startTime AND :endTime " +
        "AND Training.isCanceled = 0 " +
        "AND Client.isDeleted = 0"
    )
    suspend fun getTrainingsBetweenTime(
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): List<TrainingWithClient>

    @Transaction
    @Query("SELECT * FROM Training WHERE id = :id")
    suspend fun getTrainingById(id: Long): TrainingWithClient?

}