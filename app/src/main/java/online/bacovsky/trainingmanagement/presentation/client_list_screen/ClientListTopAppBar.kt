package online.bacovsky.trainingmanagement.presentation.client_list_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.util.UiText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientListTopAppBar(
    onClick: (ClientListEvent) -> Unit,
    currentSortOrder: SortOrder,
    isMenuExpanded: Boolean
) {

    val currentOrderText = when(currentSortOrder) {
        SortOrder.ClosestTraining -> UiText.StringResource(resourceId = R.string.sort_by_closest_training_desc).asString()
        SortOrder.LastPaymentDesc -> UiText.StringResource(resourceId = R.string.sort_by_payment_desc).asString()
        SortOrder.NameAsc -> UiText.StringResource(resourceId = R.string.sort_by_name_asc).asString()
        SortOrder.NameDesc -> UiText.StringResource(resourceId = R.string.sort_by_name_desc).asString()
        SortOrder.AvailableTrainingsAsc -> UiText.StringResource(resourceId = R.string.sort_by_available_training_asc).asString()
        SortOrder.AvailableTrainingsDesc -> UiText.StringResource(resourceId = R.string.sort_by_available_training_desc).asString()
    }

    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.client_list),
                maxLines = 1
            )
        },
        actions = {
            Text(text = currentOrderText)
            Box {
                IconButton(
                    onClick = {
                        onClick(ClientListEvent.OnSortButtonClick)
                    }
                ) {
                    Icon(Icons.Outlined.FilterList, contentDescription = "Filter List")
                    ClientSortOrderMenu(
                        onClick = onClick,
                        isExpanded = isMenuExpanded,
                        currentSortOrder = currentSortOrder
                    )
                }
            }
        }
    )
}
