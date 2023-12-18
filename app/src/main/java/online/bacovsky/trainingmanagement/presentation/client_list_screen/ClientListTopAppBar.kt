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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientListTopAppBar(
    onClick: (ClientListEvent) -> Unit,
    isMenuExpanded: Boolean,
    currentSortOrderDisplayName: String
) {

    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.client_list),
                maxLines = 1
            )
        },
        actions = {
            Text(text = currentSortOrderDisplayName)
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
                    )
                }
            }
        }
    )
}
