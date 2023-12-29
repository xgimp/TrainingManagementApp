package online.bacovsky.trainingmanagement.ui.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import online.bacovsky.trainingmanagement.presentation.add_client_screen.AddClientScreen
import online.bacovsky.trainingmanagement.presentation.backup_screen.BackupScreen
import online.bacovsky.trainingmanagement.presentation.calendar_screen.CalendarScreen
import online.bacovsky.trainingmanagement.presentation.client_detail_edit_screen.ClientDetailEditScreen
import online.bacovsky.trainingmanagement.presentation.client_list_screen.ClientListScreen
import online.bacovsky.trainingmanagement.presentation.client_payment_history_screen.ClientPaymentHistoryScreen
import online.bacovsky.trainingmanagement.presentation.client_add_funds_screen.ClientPaymentListScreen
import online.bacovsky.trainingmanagement.presentation.main_screen.SelectedClientState
import online.bacovsky.trainingmanagement.presentation.sms_screen.SmsScreen
import online.bacovsky.trainingmanagement.util.Routes
import java.time.LocalDateTime

private const val TAG = "AppNavGraph"

@Composable
fun AppNavGraph(
    navController: NavHostController,
    drawerState: DrawerState,
    selectedClient: MutableState<SelectedClientState>
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.screenRoute
    ) {
        composable(
            route = BottomNavItem.Home.screenRoute,
            arguments = listOf(
                navArgument("{dateToGo}") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            CalendarScreen(
                onNavigate = { navigate ->
                    navController.navigate(navigate.route)
                },
                drawerState = drawerState,
                selectedClient = selectedClient,
                dateToGo = it.arguments?.getString("dateToGo") ?: LocalDateTime.now().toString()
            )
        }
        composable(BottomNavItem.Clients.screenRoute) {
            ClientListScreen(
                onNavigate = {
                    navController.navigate(it.route)
                }
            )
        }
        composable(BottomNavItem.Backups.screenRoute) {
            BackupScreen()
        }
        composable(Routes.ADD_CLIENT) {
            AddClientScreen(
                onNavigate = {
                    navController.navigate(it.route)
                }
            )
        }
        composable(
            route = Routes.CLIENT_DETAIL_EDIT,
            arguments = listOf(
                navArgument("clientId") {
                    type = NavType.LongType
                    nullable = false
                }
            )
        ) { _ ->
            ClientDetailEditScreen(
                onNavigate = {
                    navController.navigate(it.route)
                },
            )
        }
        composable(
            route = Routes.CLIENT_PAYMENT_HISTORY_SCREEN,
            arguments = listOf(
                navArgument("clientId") {
                    type = NavType.LongType
                    nullable = false
                }
            )
        ) { _ ->
            ClientPaymentHistoryScreen(
//                onNavigate = {
//                    navController.navigate(it.route)
//                },
            )
        }
        composable(
            route = Routes.CLIENT_FUNDS_SCREEN,
            arguments = listOf(
                navArgument("clientId") {
                    type = NavType.LongType
                    nullable = false
                }
            )
        ) { _ ->
            ClientPaymentListScreen(
                onNavigate = {
                    navController.navigate(it.route)
                },
            )
        }
        composable(Routes.SMS_SCREEN) {
            SmsScreen(
                onNavigate = {
                    navController.navigate(it.route)
                }
            )
        }
    }
}