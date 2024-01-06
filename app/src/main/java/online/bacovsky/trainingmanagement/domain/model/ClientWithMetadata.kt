package online.bacovsky.trainingmanagement.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import online.bacovsky.trainingmanagement.ui.theme.inDebtEventColor
import online.bacovsky.trainingmanagement.ui.theme.lowBalanceEventColor
import online.bacovsky.trainingmanagement.ui.theme.zeroBalanceEventColor
import online.bacovsky.trainingmanagement.util.addTransparencyBy
import java.time.LocalDateTime

data class ClientWithMetadata(

    val clientId: Long,

    val clientName: String,

    val clientBalance: Long,

    val clientTrainingPrice: Long,

    val availableTrainings: Long,

    val closestTrainingStartAt: LocalDateTime?,

    val lastPaymentAt: LocalDateTime,

)

@Composable
fun ClientWithMetadata.setBackgroundByBalance(): Color {
    // TODO: Unify with Client.setBackgroundByBalance()
    // TODO: extend MaterialTheme with custom colors
    val isInDebt = this.clientBalance < 0
    val isZeroBalance = this.clientBalance == 0L
    val isLowBalance = this.clientBalance <= this.clientTrainingPrice

    return when {
        isInDebt -> inDebtEventColor.addTransparencyBy(60f)
        isZeroBalance -> zeroBalanceEventColor.addTransparencyBy(60f)
        isLowBalance -> lowBalanceEventColor.addTransparencyBy(60f)
        else -> Color.Unspecified
    }
}