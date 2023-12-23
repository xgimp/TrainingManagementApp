package online.bacovsky.trainingmanagement.data.data_source.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {
        // add Column telephoneNumber to client table
        db.execSQL(
            "ALTER TABLE Client " +
                    "ADD COLUMN telephoneNumber TEXT NOT NULL DEFAULT '0'"
        )
    }
}
