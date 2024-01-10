package online.bacovsky.trainingmanagement.data.data_source.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
                ALTER TABLE 
                  Client 
                ADD 
                  COLUMN telephoneNumber TEXT NOT NULL DEFAULT '0' 

                CREATE TABLE IF NOT EXISTS SmsHistory (
                    id INTEGER PRIMARY KEY AUTOINCREMENT, 
                    sentToClient INTEGER NOT NULL, 
                    startDate TEXT, 
                    endDate TEXT, 
                    smsText TEXT NOT NULL, 
                    smsTextHash TEXT NOT NULL, 
                    sentAt TEXT NOT NULL FOREIGN KEY (sentToClient) REFERENCES Client(id) ON DELETE RESTRICT
                  );
                CREATE INDEX IF NOT EXISTS index_SmsHistory_smsTextHash ON SmsHistory(smsTextHash);

            """.trimIndent()
        )
    }
}