package online.bacovsky.trainingmanagement.presentation.sms_screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import online.bacovsky.trainingmanagement.domain.model.Client
import online.bacovsky.trainingmanagement.domain.model.Training
import online.bacovsky.trainingmanagement.util.UiEvent

@Composable
fun SmsScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: SmsScreenViewModel = hiltViewModel()
) {
    val clientsWithScheduledTrainings = viewModel.myList.collectAsState(initial = emptyMap())
    Scaffold(
        topBar = {
            SmsScreenTopAppbar(onNavigate = onNavigate)
        },
    ) { paddingValues: PaddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            Column {
                clientsWithScheduledTrainings.value.forEach{ (client: Client, listOfTrainings: List<Training>) ->

                    Text(text = client.name)
                    Spacer(modifier = Modifier.height(16.dp))

                    Column {
                        listOfTrainings.forEach {
                            Text(text = it.startTime.toString())
                        }
                    }
                }
            }
        }
    }
}