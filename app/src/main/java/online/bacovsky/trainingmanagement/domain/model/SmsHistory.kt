package online.bacovsky.trainingmanagement.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    foreignKeys = [ForeignKey(
        entity = Client::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("clientId"),
        onDelete = ForeignKey.RESTRICT
    )]
)
data class SmsHistory(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val smsText: String,

    val sentAt: LocalDateTime = LocalDateTime.now(),

//    @ColumnInfo(index = true)
    val sentToClient: Long
)
