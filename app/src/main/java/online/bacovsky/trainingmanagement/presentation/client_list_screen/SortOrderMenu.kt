package online.bacovsky.trainingmanagement.presentation.client_list_screen

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.util.UiText


@Composable
fun ClientSortOrderMenu(
    onClick: (ClientListEvent) -> Unit,
    isExpanded: Boolean
) {
    val context = LocalContext.current

    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = {
            onClick(ClientListEvent.OnSortButtonClick)
        }
    ) {
        DropdownMenuItem(
            text = {
                Text(text = UiText.StringResource(resourceId = R.string.sort_by_name_asc).asString(context))
            },
            onClick = {
                onClick(ClientListEvent.OnSortByNameAscClick(UiText.StringResource(resourceId = R.string.sort_by_name_asc).asString(context)))
                onClick(ClientListEvent.OnSortButtonClick)
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = UiText.StringResource(resourceId = R.string.sort_by_name_desc).asString(context))
            },
            onClick = {
                onClick(ClientListEvent.OnSortByNameDescClick(UiText.StringResource(resourceId = R.string.sort_by_name_desc).asString(context)))
                onClick(ClientListEvent.OnSortButtonClick)
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = UiText.StringResource(resourceId = R.string.sort_by_closest_training_desc).asString(context))
            },
            onClick = {
                onClick(ClientListEvent.OnSortByClosestTrainingClick(UiText.StringResource(resourceId = R.string.sort_by_closest_training_desc).asString(context)))
                onClick(ClientListEvent.OnSortButtonClick)
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = UiText.StringResource(resourceId = R.string.sort_by_payment_desc).asString(context))
            },
            onClick = {
                onClick(ClientListEvent.OnSortByLastPaymentDescClick(UiText.StringResource(resourceId = R.string.sort_by_payment_desc).asString(context)))
                onClick(ClientListEvent.OnSortButtonClick)
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = UiText.StringResource(resourceId = R.string.sort_by_available_training_desc).asString(context))
            },
            onClick = {
                onClick(ClientListEvent.OnSortByAvailableTrainingsDescClick(UiText.StringResource(resourceId = R.string.sort_by_available_training_desc).asString(context)))
                onClick(ClientListEvent.OnSortButtonClick)
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = UiText.StringResource(resourceId = R.string.sort_by_available_training_asc).asString(context))
            },
            onClick = {
                onClick(ClientListEvent.OnSortByAvailableTrainingsAscClick(UiText.StringResource(resourceId = R.string.sort_by_available_training_asc).asString(context)))
                onClick(ClientListEvent.OnSortButtonClick)
            }
        )
    }
}