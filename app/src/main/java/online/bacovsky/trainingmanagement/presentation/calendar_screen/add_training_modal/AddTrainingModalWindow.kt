package online.bacovsky.trainingmanagement.presentation.calendar_screen.add_training_modal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.domain.model.Client
import online.bacovsky.trainingmanagement.domain.model.setBackgroundByBalance
import online.bacovsky.trainingmanagement.util.UiText
import online.bacovsky.trainingmanagement.util.toLocalizedDateTimeFormat
import online.bacovsky.trainingmanagement.util.toLocalizedFormat

@Composable
fun AddTrainingModalWindow(
    onDismiss: () -> Unit,
    choices: State<List<Client>>,
    state: AddTrainingFormState,
    eventHandler: (AddTrainingEvent) -> Unit
) {

    val paymentNote = UiText.StringResource(
        resourceId = R.string.training_payment,
        args = arrayOf(
            state.dateTime
                ?.withMinute(0)
                ?.toLocalizedDateTimeFormat()
                .orEmpty()
        )
    ).asString()

    Dialog(
        onDismissRequest = { onDismiss() },
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
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 2.dp),
                        text = stringResource(id = R.string.add_training),
                        maxLines = 1,
                        style = MaterialTheme.typography.headlineSmall,
                    )

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

            }  // Column

            Box(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp
                    )
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {

                    SelectClientDropdown(
                        choices = choices,
                        state = state,
                        eventHandler = eventHandler
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier.padding(top = 8.dp),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Schedule,
                                contentDescription = "Time"
                            )
                            Text(
                                modifier = Modifier.padding(start = 28.dp),
                                text = state.dateTime?.toLocalTime()
                                    ?.withMinute(0)
                                    .toString() +
                                        " - " +
                                        state.dateTime?.toLocalTime()
                                            ?.plusHours(1)
                                            ?.withMinute(0).toString()
                            )
                        }
                        Box(
                            modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Icon(imageVector = Icons.Outlined.CalendarMonth, contentDescription = "Date")
                            Text(
                                modifier = Modifier.padding(start = 28.dp),
                                text = state.dateTime?.toLocalDate()!!.toLocalizedFormat(),
                                maxLines = 1
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {

                        Button(
                            modifier = Modifier
                                .padding(
                                    top = 16.dp,
                                    bottom = 16.dp
                                ),
                            onClick = {
                                eventHandler(AddTrainingEvent.Submit(paymentNote = paymentNote))
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.save)
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun SelectClientDropdown(
    choices: State<List<Client>>,
    state: AddTrainingFormState,
    eventHandler: (AddTrainingEvent) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    val icon = if (isExpanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    Box {
        OutlinedTextField(
            value = state.client?.name ?: "",
            isError = state.clientError != null,
            enabled = false,
            readOnly = true,
            placeholder = {
                Text(
                    text = stringResource(id = R.string.select_client)
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.select_client)
                )
            },
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { cords -> textFieldSize = cords.size.toSize() }
                .clickable { isExpanded = !isExpanded },
            trailingIcon = {
                Icon(
                    icon,
                    "Dropdown Trailing icon",
                    Modifier
                        .clickable {
                            isExpanded = !isExpanded
                        }
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = MaterialTheme.colorScheme.secondary,
                disabledLabelColor = MaterialTheme.colorScheme.secondary,
                disabledTextColor = MaterialTheme.colorScheme.secondary,
            )
        )

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) {textFieldSize.width.toDp()})
        ) {
            choices.value.forEach { client ->
                DropdownMenuItem(
                    modifier = Modifier
                        .background(client.setBackgroundByBalance()),
                    leadingIcon = {
                          Icon(
                              imageVector = Icons.Outlined.Person,
                              contentDescription = "Person Icon"
                          )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.FitnessCenter,
                            contentDescription = ""
                        )
                        Text(
                            modifier = Modifier.padding(start = 32.dp),
                            text = (client.balance / client.trainingPrice).toString()
                        )

                    },
                    text = { Text(text = client.name) },
                    onClick = {
                        eventHandler(AddTrainingEvent.ClientChanged(client))
                        isExpanded = false
                    }
                )
            }
        }
    }
    if (state.clientError != null) {
        Text(
            text = stringResource(id = R.string.select_client),
            color = MaterialTheme.colorScheme.error
        )
    }
}
