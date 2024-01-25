package online.bacovsky.trainingmanagement.presentation.client_list_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import online.bacovsky.trainingmanagement.util.UiEvent
import kotlinx.coroutines.launch

@Composable
fun ClientListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: ClientListViewModel = hiltViewModel()
) {

    val clients = viewModel.clientsWithMetadata.collectAsState(initial = emptyList())
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val currentSortOrderDisplayName = viewModel.currentSortOrderDisplayName

    val sortedClientList = when (viewModel.currentSortOrder) {
        SortOrder.ClosestTraining -> clients.value.sortedWith(compareBy(nullsLast()) { it.closestTrainingStartAt })
        SortOrder.LastPaymentDesc -> clients.value.sortedByDescending { it.lastPaymentAt }
        SortOrder.NameAsc -> clients.value.sortedBy { it.clientName.lowercase() }
        SortOrder.NameDesc -> clients.value.sortedByDescending { it.clientName.lowercase() }
        SortOrder.AvailableTrainingsAsc -> clients.value.sortedBy { it.availableTrainings }
        SortOrder.AvailableTrainingsDesc -> clients.value.sortedByDescending { it.availableTrainings }
    }


    LaunchedEffect(key1 = true) {
        viewModel.iuEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                is UiEvent.ShowSnackbar -> {
                    scope.launch {
                        // close previous snackbar(s) if exists
                        snackbarHostState.currentSnackbarData?.dismiss()

                        snackbarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action,
                            withDismissAction = true
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ClientListTopAppBar(
                onClick = viewModel::onEvent,
                isMenuExpanded = viewModel.isSortOrderMenuExpanded,
                currentSortOrderDisplayName = currentSortOrderDisplayName
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(ClientListEvent.OnAddClientClick)
                }
            ) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add client button")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(
                items = sortedClientList,
                key = {
                    it.clientId
                }
            ) { client ->
                ClientListItem(
                    client = client,
//                    onEvent = viewModel::onEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .clickable {
                            viewModel.onEvent(
                                ClientListEvent.OnClickOnClientRowItem(clientId = client.clientId)
                            )
                        }
                )
            }
        }
    }
}
