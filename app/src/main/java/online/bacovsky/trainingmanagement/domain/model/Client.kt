package online.bacovsky.trainingmanagement.domain.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Client(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val name: String,

    val isDeleted: Boolean = false,

    val trainingPrice: Long,

    val balance: Long = 0L,

    val telephoneNumber: String = "0",

    val createdAt: LocalDateTime = LocalDateTime.now()

)

@Composable
fun Client.setBackgroundByBalance(): Color {
    val isReachingLowBalance = (this.balance <= this.trainingPrice * 4)
    val isLowBalance =  (this.balance <= this.trainingPrice)

    return when{
        isReachingLowBalance -> MaterialTheme.colorScheme.secondaryContainer
        isLowBalance -> MaterialTheme.colorScheme.errorContainer
        else -> Color.Unspecified
    }
}