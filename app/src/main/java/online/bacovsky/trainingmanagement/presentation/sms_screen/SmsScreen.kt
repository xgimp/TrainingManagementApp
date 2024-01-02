package online.bacovsky.trainingmanagement.presentation.sms_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import online.bacovsky.trainingmanagement.domain.model.ClientWithScheduledTrainings
import online.bacovsky.trainingmanagement.util.UiEvent

@Composable
fun SmsScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: SmsScreenViewModel = hiltViewModel()
) {
    val clientsWithScheduledTrainings = viewModel.clientTrainingList

    Scaffold(
        topBar = {
            SmsScreenTopAppbar(
                onNavigate = onNavigate,
                onEvent = viewModel::onEvent,
            )
        },
    ) { paddingValues: PaddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            CategorizedLazyColumn(items = clientsWithScheduledTrainings)
        }
    }
}

@Composable
fun Header(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        maxLines = 1,
        fontSize = MaterialTheme.typography.titleSmall.fontSize,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
    )

}


@Composable
fun SMSPreviewItem(
    item: ClientWithScheduledTrainings,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        val context = LocalContext.current

        Header(text = item.client.name)
        Text(
            modifier = modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp
                ),
            text = item.trainingsToSmsText(context)
        )
    }
    Spacer(modifier = modifier.height(8.dp))

}

@Composable
fun CategorizedLazyColumn(
    items: List<ClientWithScheduledTrainings>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        items(items) {
            SMSPreviewItem(item = it)
        }
    }
}