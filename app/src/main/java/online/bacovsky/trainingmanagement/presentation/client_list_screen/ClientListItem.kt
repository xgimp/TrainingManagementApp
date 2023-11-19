package online.bacovsky.trainingmanagement.presentation.client_list_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import online.bacovsky.trainingmanagement.domain.model.Client
import online.bacovsky.trainingmanagement.domain.model.setBackgroundByBalance


@Composable
fun ClientListItem(
    client: Client,
//    onEvent: (ClientListEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
//            .padding(start = 10.dp, end = 10.dp)
            .background(client.setBackgroundByBalance()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Icon(
            imageVector = Icons.Outlined.Person,
            contentDescription = "Person Icon",
            modifier = Modifier
                .padding(start = 10.dp)
        )

        Text(
            text = client.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
                .padding(start = 10.dp, end = 10.dp)
        )
        Column(
//            modifier = Modifier
//                .background(androidx.compose.ui.graphics.Color.Red),
//                .weight(0.3f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .padding(end = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.FitnessCenter,
                    contentDescription = ""
                )
                Text(
                    text = (client.balance / client.trainingPrice).toString(),
//                    color = client.setBalanceTextColor(),
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                )
            }

            Row(
                modifier = Modifier
                    .padding(end = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccountBalanceWallet,
                    contentDescription = ""
                )
                Text(
                    text = client.balance.toString(),
                    modifier = Modifier.padding(start = 10.dp),
//                    color = client.setBalanceTextColor()
                )
            }
        }
//        IconButton(
//            onClick = {
//                onEvent(ClientListEvent.OnDeleteClick(client))
//            }
//        ) {
//            Icon(
//                imageVector = Icons.Outlined.DeleteOutline,
//                contentDescription = ""
//            )
//        }
    }
}