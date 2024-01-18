package online.bacovsky.trainingmanagement.presentation.calendar_screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Sms
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.domain.model.Client
import online.bacovsky.trainingmanagement.domain.model.setBackgroundByBalance
import online.bacovsky.trainingmanagement.presentation.main_screen.SelectedClientState
import kotlinx.coroutines.launch
import online.bacovsky.trainingmanagement.presentation.client_list_screen.ClientListEvent
import online.bacovsky.trainingmanagement.util.UiEvent
import online.bacovsky.trainingmanagement.util.UiText

@Composable
fun CalendarDrawerSheetContent(
    items: State<List<Client>>,
    selectedClient: MutableState<SelectedClientState>,
    drawerState: DrawerState,
    onEvent: (ClientListEvent) -> Unit,
    onNavigate: (UiEvent.Navigate) -> Unit,
    uiEvents: Flow<UiEvent>
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        uiEvents.collect {event ->
            when(event) {
                is UiEvent.Navigate -> {
                    onNavigate(event)
                }
                else -> {}
            }

        }
    }


    Text(
        text = stringResource(id = R.string.client_list),
        maxLines = 1,
        modifier = Modifier.padding(16.dp)
    )

    Divider()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(items.value) { client ->
                NavigationDrawerItem(
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = client.setBackgroundByBalance(),
                        selectedContainerColor = client.setBackgroundByBalance()
                    ),
                    modifier = Modifier
                        .padding(NavigationDrawerItemDefaults.ItemPadding),
                    icon = {
                        if (client.id == selectedClient.value.id) {
                            Icon(
                                imageVector = Icons.Outlined.Done,
                                contentDescription = "Selected Icon"
                            )

                        }
                        else {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = "Person Icon"
                            )
                        }
                    },
                    label = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = client.name)
                            Box(
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.FitnessCenter,
                                    contentDescription = "Number of trainings",
                                )
                                Text(
                                    text = (client.balance / client.trainingPrice).toString(),
//                                color = client.setBalanceTextColor(),
                                    modifier = Modifier.padding(start = 30.dp)
                                )
                            }
                        }
                    },
                    selected = client.id == selectedClient.value.id,
                    onClick = {
                        Log.d(TAG, "CalendarDrawerSheetContent: Clicked on ${client.id}")
                        if (client.id == selectedClient.value.id) {
                            // item already selected, so deselect it
                            selectedClient.value = selectedClient.value.copy(id = -1, name = "")
                        }
                        else {
                            selectedClient.value = selectedClient.value.copy(id = client.id!!, name = client.name)
                        }
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
//            item {
//                Text(
//                    text = stringResource(id = R.string.other_options),
//                    maxLines = 1,
//                    modifier = Modifier.padding(16.dp)
//                )
//                Divider()
//            }
//            item {
//                NavigationDrawerItem(
//                    modifier = Modifier
//                        .padding(NavigationDrawerItemDefaults.ItemPadding),
//                    icon = {
//                        Icon(
//                            imageVector = Icons.Outlined.Sms,
//                            contentDescription = "Send SMS"
//                        )
//                    },
//                    label = { Text(text = UiText.StringResource(R.string.sms).asString()) },
//                    selected = false,
//                    onClick = {
//                        scope.launch {
//                            onEvent(ClientListEvent.OnSmsSendClick)
//                            drawerState.close()
//                        }
//                    }
//                )
//            }
        }
    }
}
