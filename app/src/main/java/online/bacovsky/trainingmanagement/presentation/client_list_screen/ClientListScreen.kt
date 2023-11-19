package online.bacovsky.trainingmanagement.presentation.client_list_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.util.UiEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: ClientListViewModel = hiltViewModel()
) {

    val clients = viewModel.clients.collectAsState(initial = emptyList())
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.iuEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                is UiEvent.ShowSnackbar -> {
                    scope.launch {
                        // close previous snackbar(s) if exists
                        snackbarHostState.currentSnackbarData?.dismiss()

                        val snackbarResult = snackbarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action,
                            withDismissAction = true
                        )
                        when(snackbarResult) {
                            SnackbarResult.ActionPerformed -> {
                                viewModel.onEvent(ClientListEvent.OnUndoDeleteClick)

                            }
                            else -> {}
                        }
                    }
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.client_list),
                        maxLines = 1
                    )
                },
//                actions = {
//                    IconButton(
//                        onClick = {/* TODO */}
//                    ) {
//                        Icon(Icons.Outlined.FilterList, contentDescription = "Filter List")
//                    }
//                }
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
            items(clients.value) { client ->
                ClientListItem(
                    client = client,
//                    onEvent = viewModel::onEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .clickable {
                            viewModel.onEvent(
                                ClientListEvent.OnClickOnClientRowItem(clientId = client.id)
                            )
                        }
                )
            }
        }
    }
}
