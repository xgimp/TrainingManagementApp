package online.bacovsky.trainingmanagement.data.data_source

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import online.bacovsky.trainingmanagement.data.data_source.migrations.MigrationFrom2To3
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
        SmsHistory::class
   ],
    autoMigrations = [
        AutoMigration(from = 2, to = 3, spec = MigrationFrom2To3::class)
   ],
   version = 3,
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract val clientDao: ClientDao

    abstract val trainingDao: TrainingDao

    abstract val clientPaymentDao: ClientPaymentDao

    abstract val smsHistoryDao: SmsHistoryDao

}