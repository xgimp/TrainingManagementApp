package online.bacovsky.trainingmanagement.presentation.sms_screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.util.Routes
import online.bacovsky.trainingmanagement.util.UiEvent
import online.bacovsky.trainingmanagement.util.UiText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmsScreenTopAppbar(
    onNavigate: (UiEvent.Navigate) -> Unit,
    onEvent: (SmsScreenEvent) -> Unit,
    isSendButtonEnabled: Boolean,
) {
    TopAppBar(
        title = {
            // TODO: make this translatable
            // TODO: figure out better title
            Text(text = UiText.StringResource(R.string.sms_screen_title).asString())
        },
        navigationIcon = {
//            IconButton(
//                onClick = {
//                    onNavigate(UiEvent.Navigate(Routes.CALENDAR_SCREEN))
//                }
//            ) {
//                Icon(Icons.Outlined.ArrowBack, contentDescription = "")
//            }
        },
        actions = {
            IconButton(
                enabled = isSendButtonEnabled,
                onClick = {
                    onEvent(SmsScreenEvent.OnBulkSmsSendButtonClicked)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Send,
                    contentDescription = "Send SMS"
                )
            }
        }
    )
}
