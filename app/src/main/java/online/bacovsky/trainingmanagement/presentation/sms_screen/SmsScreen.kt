package online.bacovsky.trainingmanagement.presentation.sms_screen

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch
import online.bacovsky.trainingmanagement.domain.model.ClientWithScheduledTrainings
import online.bacovsky.trainingmanagement.util.UiEvent

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SmsScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: SmsScreenViewModel = hiltViewModel()
) {
    val clientsWithScheduledTrainings = viewModel.clientTrainingList
    val smsPermissionState = rememberPermissionState(permission = Manifest.permission.SEND_SMS)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(smsPermissionState.status) {
        when(smsPermissionState.status) {
            is PermissionStatus.Denied -> {
                if (smsPermissionState.status.shouldShowRationale) {
                        smsPermissionState.launchPermissionRequest()
//                    Toast.makeText(context, "show  rationale", Toast.LENGTH_LONG).show()
                }
                else {
//                    show modal and then permission request
//                    smsPermissionState.launchPermissionRequest()

                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "SMS permission is required",
                            actionLabel = "Go to settings"
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.packageName, null)
                            )
                            startActivity(context, intent, null)
                        }
                    }
                }
            }
            is PermissionStatus.Granted -> {
                Toast.makeText(context, "permission granted", Toast.LENGTH_LONG).show()
            }
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SmsScreenTopAppbar(
                onNavigate = onNavigate,
                onEvent = viewModel::onEvent,
                isSendButtonEnabled = smsPermissionState.status.isGranted && (clientsWithScheduledTrainings.isNotEmpty())
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