package online.bacovsky.trainingmanagement.util

import androidx.room.withTransaction
import online.bacovsky.trainingmanagement.data.data_source.AppDatabase

class TransactionProvider(
    private val db: AppDatabase
) {
    suspend fun <R> runAsTransaction(block: suspend () -> R): R {
        return db.withTransaction(block)
    }
}