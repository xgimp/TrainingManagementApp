package online.bacovsky.trainingmanagement.presentation.backup_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.presentation.MainActivity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(
    viewModel: BackupScreenViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    val mainActivity = context as MainActivity

    LaunchedEffect(key1 = true) {
        viewModel.backupEvents.collect {event ->
            when(event) {
                is BackupScreenViewModel.BackupRestoreStatus.Result -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT).show()
                    if (event.isSuccessful) {
                        viewModel.onEvent(BackupEvent.RestartAppRequest(mainActivity.roomBackup, context))
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.screen_title_backup)
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    viewModel.onEvent(BackupEvent.OnCreateBackupClick(mainActivity.roomBackup, context))
                },
            ) {
                Text(
                    text = stringResource(id = R.string.create_backup).uppercase()
                )
            }
            Button(
                onClick = {
                    viewModel.onEvent(BackupEvent.OnRestoreBackupClick(mainActivity.roomBackup, context))
                },
            ) {
                Text(text = stringResource(id = R.string.restore_backup).uppercase())
            }
        }
    }
}