package online.bacovsky.trainingmanagement.presentation.sms_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.util.UiText

@Composable
fun SendSmsConfirmDialog(
    isShown: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogText: String,
    smsToSendNumber: Int,
    currentProgress: Float
) {
    var showProgress by remember {
        mutableStateOf(false)
    }

    when {
        isShown -> {
            Dialog(
                onDismissRequest = { onDismissRequest() },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    usePlatformDefaultWidth = false
                )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 2.dp),
                            text = UiText.StringResource(R.string.sms_confirm_dialog_header).asString(),
                            maxLines = 1,
                            style = MaterialTheme.typography.headlineSmall,
                        )

                        IconButton(
                            onClick = {
                                showProgress = false
                                onDismissRequest()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "Close"
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = dialogText)

                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (showProgress) {

                                val isDone =  currentProgress.toInt() == smsToSendNumber

                                val progressText = when {
                                    isDone -> UiText.StringResource(R.string.done).asString()
                                    else -> UiText.StringResource(
                                        R.string.sms_sending_progress,
                                        args = arrayOf(currentProgress.toInt(), smsToSendNumber)).asString()
                                }

                                CircularDeterminateIndicator((currentProgress / smsToSendNumber))
                                Text(
                                    modifier = Modifier.padding(start = 16.dp),
                                    text = progressText,
                                    maxLines = 1
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {

                            TextButton(
                                enabled = !showProgress,
                                onClick = {
                                    showProgress = false
                                    onDismissRequest()
                                }
                            ) {
                                Text(text = stringResource(id = R.string.no))
                            }

                            TextButton(
                                enabled = !showProgress,
                                onClick = {
                                    showProgress = true
                                    onConfirmation()
                                }
                            ) {
                                Text(text = stringResource(id = R.string.yes))
                            }
                        }
                    }
                }
            }
        }
    }
}