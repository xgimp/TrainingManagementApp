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
        childColumns = arrayOf("sentToClient"),
        onDelete = ForeignKey.RESTRICT
    )],
)
data class SmsHistory(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo(index = true)
    val sentToClient: Long,

    val startDate: LocalDateTime,

    val endDate: LocalDateTime,

    val smsText: String,

    @ColumnInfo(index = true)
    val smsTextHash: String,

    val sentAt: LocalDateTime = LocalDateTime.now(),

)