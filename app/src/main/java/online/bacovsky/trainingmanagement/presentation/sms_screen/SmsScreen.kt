package online.bacovsky.trainingmanagement.presentation.sms_screen

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.domain.model.ClientWithScheduledTrainings
import online.bacovsky.trainingmanagement.domain.model.SmsHistory
import online.bacovsky.trainingmanagement.util.UiEvent
import online.bacovsky.trainingmanagement.util.UiText
import online.bacovsky.trainingmanagement.util.toLocalizedDateTimeFormat
import online.bacovsky.trainingmanagement.util.toLocalizedFormat

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SmsScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: SmsScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val smsHistory by viewModel.smsHistory.collectAsState(initial = emptyList())
    val clientsWithScheduledTrainings = state.smsToSendList
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
                    smsPermissionState.launchPermissionRequest()

                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = UiText.StringResource(R.string.sms_permission_required_message).asString(context),
                            actionLabel = UiText.StringResource(R.string.go_to_settings).asString(context)
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
                snackbarHostState.currentSnackbarData?.dismiss()
//                Toast.makeText(context, "permission granted", Toast.LENGTH_LONG).show()
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
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        viewModel.onEvent(SmsScreenEvent.OnPreviousWeekButtonClicked)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Preview Week Button"
                        )
                    }
                    Text(
                        text = "${state.nexMonday.toLocalDate().toLocalizedFormat()} - ${state.nextSunday.toLocalDate().toLocalizedFormat()}",
                        style = MaterialTheme.typography.titleLarge,
                        fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                        textAlign = TextAlign.Center,
                    )
                    IconButton(onClick = {
                        viewModel.onEvent(SmsScreenEvent.OnNextWeekButtonClicked)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowForward,
                            contentDescription = "Next Week Button"
                        )
                    }
                }



                CategorizedLazyColumn(
                    items = clientsWithScheduledTrainings,
                    smsHistory = smsHistory
                )
                SendSmsConfirmDialog(
                    isShown = state.showConfirmDialog,
                    dialogText = UiText.StringResource(
                        R.string.sms_confirm_dialog,
                        args = arrayOf(clientsWithScheduledTrainings.size)
                    ).asString(),
                    onDismissRequest = {
                        viewModel.onEvent(SmsScreenEvent.OnBulkSmsSendDismissButtonClicked)
                    },
                    onConfirmation = {
                        viewModel.onEvent(SmsScreenEvent.OnConfirmSendClicked(context))
                    },
                    smsToSendNumber = clientsWithScheduledTrainings.size,
                    currentProgress = state.numberOfSentSms
                )
            }

        }
    }
}

@Composable
fun Header(
    text: String,
    text2: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            maxLines = 1,
            fontSize = MaterialTheme.typography.titleSmall.fontSize
        )
        Text(
            text = if(text2.isNotEmpty()) "${UiText.StringResource(R.string.sms_was_sent).asString()} $text2" else text2,
            maxLines = 1,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            color = MaterialTheme.colorScheme.secondary
        )
    }

}


@Composable
fun SMSPreviewItem(
    item: ClientWithScheduledTrainings,
    sentTime: String,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth()
    ) {
        val context = LocalContext.current
        Header(
            text = item.client.name,
            text2 = sentTime
        )
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
    modifier: Modifier = Modifier,
    smsHistory: List<SmsHistory>
) {
    LazyColumn(modifier) {
        items(items) { smsToPreview ->

            val wasSentTime = smsHistory.lastOrNull { smsHistory ->
                smsHistory.sentToClient == smsToPreview.client.id
            }?.sentAt?.toLocalizedDateTimeFormat().orEmpty()

            SMSPreviewItem(
                item = smsToPreview,
                sentTime = wasSentTime
            )
        }
    }
}