package online.bacovsky.trainingmanagement.presentation.client_payment_history_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import online.bacovsky.trainingmanagement.domain.model.ClientPayment
import online.bacovsky.trainingmanagement.ui.theme.positive
import online.bacovsky.trainingmanagement.util.currencySymbol

@Composable
fun PaymentItem(
    item: ClientPayment,
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = if (item.amount > 0) "+${item.amount} $currencySymbol" else "${item.amount} $currencySymbol",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = if (item.amount > 0) positive else MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = item.note,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }

    }
}