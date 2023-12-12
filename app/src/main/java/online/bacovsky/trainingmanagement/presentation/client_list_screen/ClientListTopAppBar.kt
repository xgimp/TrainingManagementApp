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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import online.bacovsky.trainingmanagement.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientListTopAppBar() {
    val isMenuExpanded = rememberSaveable { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.client_list),
                maxLines = 1
            )
        },
        actions = {
            Box {
                IconButton(
                    onClick = {
                        isMenuExpanded.value = !isMenuExpanded.value
                    }
                ) {
                    Icon(Icons.Outlined.FilterList, contentDescription = "Filter List")
                    ClientFilterMenu(isExpanded = isMenuExpanded)
                }
            }
        }
    )
}


@Composable
fun ClientFilterMenu(
    isExpanded: MutableState<Boolean>
) {
    DropdownMenu(
        expanded = isExpanded.value,
        onDismissRequest = {
            isExpanded.value = false
        }
    ) {
        DropdownMenuItem(
            text = {
                   Text(text = "Item 1")
            },
            onClick = { isExpanded.value = false }
        )
        DropdownMenuItem(
            text = {
                   Text(text = "Item 2")
            },
            onClick = { /*TODO*/ }
        )
    }
}