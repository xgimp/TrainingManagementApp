package online.bacovsky.trainingmanagement.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.util.UiText

@Composable
fun YesNoDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onDismissRequestButtonClick: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    val confirmText = UiText.StringResource(resourceId = R.string.yes).asString()
    val dismissText = UiText.StringResource(resourceId = R.string.no).asString()

    var openAlertDialog by remember { mutableStateOf(showDialog) }

    when {
        openAlertDialog -> {
            SimpleDialog(
                confirmText = confirmText,
                dismissText = dismissText,
                onDismissRequest = {
                    openAlertDialog = false
                    onDismissRequest()
               },
                onDismissRequestButtonClick = {
                    openAlertDialog = false
                    onDismissRequestButtonClick()
                },
                onConfirmation = {
                    openAlertDialog = false
                    onConfirmation()
                },
                dialogTitle = dialogTitle,
                dialogText = dialogText,
            )
        }
    }
}


@Composable
fun SimpleDialog(
    onDismissRequest: () -> Unit,
    onDismissRequestButtonClick: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    confirmText: String = "Yes",
    dismissText: String = "No"
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequestButtonClick()
                }
            ) {
                Text(text = dismissText)
            }
        }
    )
}