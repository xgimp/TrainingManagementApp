package online.bacovsky.trainingmanagement.data.data_source.migrations

import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.migration.AutoMigrationSpec

@DeleteColumn(columnName = "startDate", tableName = "SmsHistory")
@DeleteColumn(columnName = "endDate", tableName = "SmsHistory")
class MIGRATION_3_4: AutoMigrationSpec


