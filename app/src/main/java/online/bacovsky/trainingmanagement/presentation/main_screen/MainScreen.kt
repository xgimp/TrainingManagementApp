package online.bacovsky.trainingmanagement.presentation.main_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import online.bacovsky.trainingmanagement.presentation.calendar_screen.CalendarDrawerSheetContent
import online.bacovsky.trainingmanagement.presentation.client_list_screen.ClientListViewModel
import online.bacovsky.trainingmanagement.ui.navigation.AppNavGraph
import online.bacovsky.trainingmanagement.ui.navigation.BottomNav
import online.bacovsky.trainingmanagement.util.Routes

@Composable
fun MainScreen(
    viewModel: ClientListViewModel = hiltViewModel()
) {

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val selectedClient = remember {
        mutableStateOf(SelectedClientState())
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet {
                val clients = viewModel.clients.collectAsState(initial = emptyList())
                CalendarDrawerSheetContent(
                    drawerState = drawerState,
                    items = clients,
                    selectedClient = selectedClient,
                    onEvent = viewModel::onEvent,
                    uiEvents = viewModel.iuEvent,
                    onNavigate = {
                        navController.navigate(Routes.SMS_SCREEN)
                    }
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                BottomNav(navController = navController)
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                AppNavGraph(
                    drawerState = drawerState,
                    selectedClient = selectedClient,
                    navController = navController
                )
            }
        }
    }
}
