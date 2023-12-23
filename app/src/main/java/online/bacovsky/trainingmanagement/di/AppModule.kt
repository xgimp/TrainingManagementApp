package online.bacovsky.trainingmanagement.di

import android.content.Context
import androidx.room.Room
import online.bacovsky.trainingmanagement.data.data_source.AppDatabase
import online.bacovsky.trainingmanagement.data.repository.CalendarEventRepository
import online.bacovsky.trainingmanagement.data.repository.CalendarEventRepositoryImpl
import online.bacovsky.trainingmanagement.data.repository.ClientRepository
import online.bacovsky.trainingmanagement.data.repository.ClientRepositoryImpl
import online.bacovsky.trainingmanagement.data.repository.TrainingRepository
import online.bacovsky.trainingmanagement.data.repository.TrainingRepositoryImpl
import online.bacovsky.trainingmanagement.util.TransactionProvider
import online.bacovsky.trainingmanagement.util.validation.ValidateClient
import online.bacovsky.trainingmanagement.util.validation.ValidateFunds
import online.bacovsky.trainingmanagement.util.validation.ValidateName
import online.bacovsky.trainingmanagement.util.validation.ValidatePrice
import online.bacovsky.trainingmanagement.util.validation.ValidateTrainingStartDate
import online.bacovsky.trainingmanagement.util.validation.ValidateTrainingStartTime
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import online.bacovsky.trainingmanagement.data.data_source.migrations.MIGRATION_1_2
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app_db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideClientRepository(db: AppDatabase): ClientRepository {
        return ClientRepositoryImpl(db.clientDao, db.clientPaymentDao, TransactionProvider(db))
    }

    @Provides
    @Singleton
    fun provideTrainingRepository(db: AppDatabase): TrainingRepository {
        return TrainingRepositoryImpl(db.trainingDao, db.clientPaymentDao, db.clientDao, TransactionProvider(db))
    }

    @Provides
    @Singleton
    fun provideCalendarEventRepository(db: AppDatabase): CalendarEventRepository {
        return CalendarEventRepositoryImpl(db.trainingDao)
    }

    @Provides
    @Singleton
    fun provideTransactionProvider(db: AppDatabase): TransactionProvider {
        return TransactionProvider(db)
    }
    @Provides
    fun provideNameValidation(): ValidateName {
        return ValidateName()
    }

    @Provides
    fun providePriceValidation(): ValidatePrice {
        return ValidatePrice()
    }

    @Provides
    fun provideFundsValidation(): ValidateFunds {
        return ValidateFunds()
    }

    @Provides
    fun provideClientValidation(): ValidateClient {
        return ValidateClient()
    }

    @Provides
    fun provideTrainingTimeValidation(): ValidateTrainingStartTime {
        return ValidateTrainingStartTime()
    }

    @Provides
    fun provideTrainingDateValidation(): ValidateTrainingStartDate {
        return ValidateTrainingStartDate()
    }

}