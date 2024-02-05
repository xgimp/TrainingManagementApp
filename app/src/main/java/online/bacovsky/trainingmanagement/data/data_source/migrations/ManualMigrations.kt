package online.bacovsky.trainingmanagement.data.data_source.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
                ALTER TABLE Client 
                    ADD COLUMN telephoneNumber TEXT NOT NULL DEFAULT '0'
            """.trimIndent()
        )
        db.execSQL(
            """
                CREATE TABLE IF NOT EXISTS SmsHistory (
                    id INTEGER PRIMARY KEY AUTOINCREMENT, 
                    sentToClient INTEGER NOT NULL, 
                    startDate TEXT NOT NULL, 
                    endDate TEXT NOT NULL, 
                    smsText TEXT NOT NULL, 
                    smsTextHash TEXT NOT NULL, 
                    sentAt TEXT NOT NULL, 
                    FOREIGN KEY (sentToClient) REFERENCES Client(id) ON DELETE RESTRICT
                )
            """.trimIndent()
        )
        db.execSQL(
            """
                CREATE INDEX IF NOT EXISTS index_SmsHistory_sentToClient ON SmsHistory(sentToClient)
            """.trimIndent()
        )
        db.execSQL(
            """
                CREATE INDEX IF NOT EXISTS index_SmsHistory_smsTextHash ON SmsHistory(smsTextHash)
            """.trimIndent()
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {

    override fun migrate(db: SupportSQLiteDatabase) {
        // Fix Client.balance == 0
        // find all clients where  balance != sum of payments
        // set balance to sum of payments
        db.execSQL("""
            UPDATE Client
            SET balance = (
                SELECT COALESCE(SUM(amount), 0)
                FROM ClientPayment
                WHERE clientId = Client.id
            )
            WHERE balance != (
                SELECT COALESCE(SUM(amount), 0)
                FROM ClientPayment
                WHERE clientId = Client.id
            );
        """.trimIndent())
    }
}