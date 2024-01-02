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


val MIGRATION_2_3 = object : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            """
                CREATE TABLE IF NOT EXISTS SmsHistory (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    smsText TEXT NOT NULL,
                    sentAt TEXT NOT NULL,
                    clientId INTEGER NOT NULL,
                    FOREIGN KEY (clientId) REFERENCES Client(id) ON DELETE RESTRICT
                );

            """.trimIndent()
        )
    }
}
