package online.bacovsky.trainingmanagement.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import online.bacovsky.trainingmanagement.data.type_convertor.LocalDateTimeConverter
import online.bacovsky.trainingmanagement.domain.model.Client
import online.bacovsky.trainingmanagement.domain.model.ClientPayment
import online.bacovsky.trainingmanagement.domain.model.SmsHistory
import online.bacovsky.trainingmanagement.domain.model.Training

@Database(
    entities = [
        Client::class,
        Training::class,
        ClientPayment::class,
        // SmsHistory::class
   ],
   version = 2,
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract val clientDao: ClientDao

    abstract val trainingDao: TrainingDao

    abstract val clientPaymentDao: ClientPaymentDao

}