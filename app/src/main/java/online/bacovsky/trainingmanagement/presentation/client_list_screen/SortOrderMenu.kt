package online.bacovsky.trainingmanagement.presentation.client_list_screen

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.util.UiText


@Composable
fun ClientSortOrderMenu(
    onClick: (ClientListEvent) -> Unit,
    currentSortOrder: SortOrder,
    isExpanded: Boolean
) {
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = {
            onClick(ClientListEvent.OnSortButtonClick)
        }
    ) {
        DropdownMenuItem(
            text = {
                Text(text = UiText.StringResource(resourceId = R.string.sort_by_name_asc).asString())
            },
            onClick = {
                //currentSortOrder.value = SortOrder.NameAsc
                onClick(ClientListEvent.OnSortButtonClick)
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = UiText.StringResource(resourceId = R.string.sort_by_name_desc).asString())
            },
            onClick = {
                onClick(ClientListEvent.OnSortByNameDescClick(""))
                onClick(ClientListEvent.OnSortButtonClick)
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = UiText.StringResource(resourceId = R.string.sort_by_closest_training_desc).asString())
            },
            onClick = {
                onClick(ClientListEvent.OnSortByClosestTrainingClick(""))
                onClick(ClientListEvent.OnSortButtonClick)
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = UiText.StringResource(resourceId = R.string.sort_by_payment_desc).asString())
            },
            onClick = {
                onClick(ClientListEvent.OnSortByLastPaymentDescClick(""))
                onClick(ClientListEvent.OnSortButtonClick)
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = UiText.StringResource(resourceId = R.string.sort_by_available_training_desc).asString())
            },
            onClick = {
                onClick(ClientListEvent.OnSortByAvailableTrainingsDescClick(""))
                onClick(ClientListEvent.OnSortButtonClick)
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = UiText.StringResource(resourceId = R.string.sort_by_available_training_asc).asString())
            },
            onClick = {
                onClick(ClientListEvent.OnSortByAvailableTrainingsAscClick(""))
                onClick(ClientListEvent.OnSortButtonClick)
            }
        )
    }
}