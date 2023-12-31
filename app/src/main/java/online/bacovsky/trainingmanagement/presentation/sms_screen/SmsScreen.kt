package online.bacovsky.trainingmanagement.presentation.sms_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import online.bacovsky.trainingmanagement.domain.model.ClientWithScheduledTrainings
import online.bacovsky.trainingmanagement.domain.model.Training
import online.bacovsky.trainingmanagement.util.UiEvent

@Composable
fun SmsScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: SmsScreenViewModel = hiltViewModel()
) {
    val clientsWithScheduledTrainings = viewModel.clientTrainingList
    Scaffold(
        topBar = {
            SmsScreenTopAppbar(onNavigate = onNavigate)
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
fun Item(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategorizedLazyColumn(
    items: List<ClientWithScheduledTrainings>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        items.forEach { item: ClientWithScheduledTrainings ->
            stickyHeader {
                Header(text = item.client.name)
            }
            items(item.trainings) {training: Training ->
                Item(text = training.startTime.toString())
            }
        }
    }
}