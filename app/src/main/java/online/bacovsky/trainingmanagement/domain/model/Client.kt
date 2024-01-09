package online.bacovsky.trainingmanagement.domain.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import online.bacovsky.trainingmanagement.ui.theme.inDebtEventColor
import online.bacovsky.trainingmanagement.ui.theme.lowBalanceEventColor
import online.bacovsky.trainingmanagement.ui.theme.zeroBalanceEventColor
import online.bacovsky.trainingmanagement.util.addTransparencyBy
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

    val isInDebt = this.balance < 0
    val isZeroBalance = this.balance == 0L
    val isLowBalance = this.balance <= this.trainingPrice

    return when {
        isInDebt -> inDebtEventColor.addTransparencyBy(60f)
        isZeroBalance -> zeroBalanceEventColor.addTransparencyBy(60f)
        isLowBalance -> lowBalanceEventColor.addTransparencyBy(60f)
        else -> Color.Unspecified
    }
    return color
}