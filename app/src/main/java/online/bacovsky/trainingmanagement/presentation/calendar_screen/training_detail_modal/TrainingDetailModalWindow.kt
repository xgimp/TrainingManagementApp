package online.bacovsky.trainingmanagement.presentation.calendar_screen.training_detail_modal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.util.UiText
import online.bacovsky.trainingmanagement.util.toLocalizedDateTimeFormat
import online.bacovsky.trainingmanagement.util.toLocalizedTimeFormat

@Composable
fun TrainingDetailModalWindow(
    onDismiss: () -> Unit,
    eventHandler: (TrainingDetailEvent) -> Unit,
    state: TrainingDetailFormState,
) {


    val paymentNote = UiText.StringResource(
        resourceId = R.string.fee_returned,
        args = arrayOf(
            state.training
                ?.training
                ?.startTime
                ?.toLocalizedDateTimeFormat()
                .orEmpty()
        )
    ).asString()

    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
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

                state.training?.client?.name?.let {clientName: String ->
                    Text(
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 2.dp),
                        text =clientName,
                        maxLines = 1,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
                IconButton(
                    onClick = {
                        onDismiss()
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

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = stringResource(id = R.string.training_start_time))
                    },
                    value = state.training?.training?.startTime?.toLocalizedTimeFormat() ?: "",
                    readOnly = true,
                    onValueChange = {}
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = stringResource(id = R.string.training_end_time))
                    },
                    value = state.training?.training?.endTime?.toLocalizedTimeFormat() ?: "",
                    readOnly = true,
                    onValueChange = {}
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            eventHandler(TrainingDetailEvent.OnCanceledClick(!state.isCanceled))
                        }
                    ,
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.isCanceled,
                        onCheckedChange = {
                            eventHandler(TrainingDetailEvent.OnCanceledClick(it))
                        }
                    )
                    Text(
                        text = stringResource(id = R.string.cancel)
                    )
                }

                if (state.isCanceled) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {

                        TextButton(
                            onClick = {
                                eventHandler(TrainingDetailEvent.OnChargeFee)
                            }
                        ) {
                            Text(text = stringResource(id = R.string.charge_fee_confirm))
                        }

                        TextButton(
                            onClick = {
                                eventHandler(TrainingDetailEvent.OnNotChargeFee(paymentNote = paymentNote))
                            }
                        ) {
                            Text(text = stringResource(id = R.string.charge_fee_dismiss))
                        }
                    }
                }
            }
        }
    }
}