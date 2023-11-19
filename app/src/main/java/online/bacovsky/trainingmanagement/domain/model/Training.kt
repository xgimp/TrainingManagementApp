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
        onDelete = ForeignKey.CASCADE
    )]
)
data class Training(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo(index = true)
    var clientId: Long,

    val startTime: LocalDateTime,

    val endTime: LocalDateTime,

    val price: Long,

    val isCanceled: Boolean = false

)