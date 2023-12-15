package online.bacovsky.trainingmanagement.presentation.client_list_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
    isSortMenuExpanded: MutableState<Boolean>,
    currentSortOrder: MutableState<SortOrder>
) {

    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.client_list),
                maxLines = 1
            )
        },
        actions = {
            Text(text = currentSortOrder.value.displayName)
            Box {
                IconButton(
                    onClick = {
                        isSortMenuExpanded.value = !isSortMenuExpanded.value
                    }
                ) {
                    Icon(Icons.Outlined.FilterList, contentDescription = "Filter List")
                    ClientSortOrderMenu(isExpanded = isSortMenuExpanded, currentSortOrder = currentSortOrder)
                }
            }
        }
    )
}


@Composable
fun ClientSortOrderMenu(
    isExpanded: MutableState<Boolean>,
    currentSortOrder: MutableState<SortOrder>,
) {
    DropdownMenu(
        expanded = isExpanded.value,
        onDismissRequest = {
            isExpanded.value = false
        }
    ) {
        DropdownMenuItem(
            text = {
                Text(text = SortOrder.NameAsc.displayName)
            },
            onClick = {
                currentSortOrder.value = SortOrder.NameAsc
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = SortOrder.NameDesc.displayName)
            },
            onClick = {
                currentSortOrder.value = SortOrder.NameDesc
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = SortOrder.ClosestTraining.displayName)
            },
            onClick = {
                currentSortOrder.value = SortOrder.ClosestTraining
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = SortOrder.LastPaymentDesc.displayName)
            },
            onClick = {
                currentSortOrder.value = SortOrder.LastPaymentDesc
            }
        )
    }
}