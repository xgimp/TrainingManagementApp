package online.bacovsky.trainingmanagement.presentation.client_payment_history_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

private const val TAG = "ClientPaymentHistoryScreen"

@Composable
fun ClientPaymentHistoryScreen(
    viewModel: ClientPaymentHistoryViewModel = hiltViewModel()
) {
    val payments = viewModel.payments.collectAsState(initial = emptyList())

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
//            Text(
//                text = "Amount",
//                style = MaterialTheme.typography.headlineSmall
//            )
//            Text(
//                text = "Note",
//                style = MaterialTheme.typography.headlineSmall
//            )
        }
        Row {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(payments.value) { payment ->
                    PaymentItem(payment)
                }
            }
        }
    }
}