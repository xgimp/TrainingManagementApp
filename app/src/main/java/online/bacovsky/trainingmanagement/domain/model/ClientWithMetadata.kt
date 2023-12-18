package online.bacovsky.trainingmanagement.domain.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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
    val color = if (this.clientBalance <= this.clientTrainingPrice) {
        MaterialTheme.colorScheme.errorContainer
    } else if (this.clientBalance <= this.clientTrainingPrice * 4) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        Color.Unspecified
    }
    return color
}