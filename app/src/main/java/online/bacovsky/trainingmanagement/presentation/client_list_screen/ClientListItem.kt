package online.bacovsky.trainingmanagement.presentation.client_list_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Sms
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import online.bacovsky.trainingmanagement.domain.model.Client
import online.bacovsky.trainingmanagement.domain.model.setBackgroundByBalance
import online.bacovsky.trainingmanagement.util.currencySymbol


@Composable
fun ClientListItem(
    client: Client,
//    onEvent: (ClientListEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = client.setBackgroundByBalance(),
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {

                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Person Icon",
                )
                Text(
                    text = client.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.7f)
                        .padding(start = 5.dp, end = 10.dp)
                )
//                IconButton(
//                    onClick = {}
//                ) {
//                    Icon(
//                        imageVector = Icons.Outlined.Sms,
//                        contentDescription = ""
//                    )
//                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Start
            ) {
                // number of trainings
                Box(
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FitnessCenter,
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = "Number of remaining trainings icon"
                    )
                    Text(
                        text = (client.balance / client.trainingPrice).toString(),
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(start = 30.dp),
                    )
                }

                Box {
                    // Clients balance
                    Icon(
                        imageVector = Icons.Outlined.AccountBalanceWallet,
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = "Client balance icon"
                    )
                    Text(
                        text = "${client.balance} $currencySymbol",
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(start = 30.dp),
                    )
                }
            }
        }
    }
}
