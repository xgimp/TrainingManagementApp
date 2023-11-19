package online.bacovsky.trainingmanagement.presentation.backup_screen

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.data.data_source.AppDatabase
import online.bacovsky.trainingmanagement.presentation.MainActivity
import online.bacovsky.trainingmanagement.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

import javax.inject.Inject


const val TAG = "BackupScreenViewModel"

@HiltViewModel
class BackupScreenViewModel @Inject constructor(
    private var dbInstance: AppDatabase
): ViewModel() {

    private val backupEventChannel = Channel<BackupRestoreStatus>()
    val backupEvents = backupEventChannel.receiveAsFlow()

    fun onEvent(event: BackupEvent) {
        when (event) {
            is BackupEvent.OnCreateBackupClick -> {
                createBackup(event.roomBackup)
            }
            is BackupEvent.OnRestoreBackupClick -> {
                restoreBackup(event.roomBackup)
            }
            is BackupEvent.RestartAppRequest -> {
                restartApp(event.roomBackup, event.context)

            }
        }
    }

    private fun restoreBackup(roomBackup: RoomBackup) {
        roomBackup
            .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
            .database(dbInstance)
            .onCompleteListener { success, _, _ ->
                val resultMessage = if (success)
                    UiText.StringResource(R.string.restore_success)
                else
                    UiText.StringResource(R.string.restore_fail)

                viewModelScope.launch {
                    if (success) {
                        backupEventChannel.send(
                            BackupRestoreStatus.Result(
                                isSuccessful = true,
                                message = resultMessage
                            )
                        )
                    }
                    else {
                        backupEventChannel.send(
                            BackupRestoreStatus.Result(
                                isSuccessful = false,
                                message = resultMessage
                            )
                        )
                    }

                }
            }
        roomBackup.restore()
    }

    private fun createBackup(roomBackup: RoomBackup) {
        roomBackup
            .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
            .database(dbInstance)
            .onCompleteListener { success, _, _ ->
                val resultMessage = if (success)
                    UiText.StringResource(R.string.backup_success)

                else
                    UiText.StringResource(R.string.backup_fail)
                viewModelScope.launch {
                    if (success) {
                        backupEventChannel.send(
                            BackupRestoreStatus.Result(
                                isSuccessful = true,
                                message = resultMessage
                            )
                        )
                    }
                    else {
                        backupEventChannel.send(
                            BackupRestoreStatus.Result(
                                isSuccessful = false,
                                message = resultMessage
                            )
                        )
                    }
                }
            }
        roomBackup.backup()
    }

    private fun restartApp(roomBackup: RoomBackup, context: Context) {
        // needed after backup or restore
        roomBackup.restartApp(Intent(context, MainActivity::class.java))
    }

    sealed class BackupRestoreStatus {
        data class Result(
            val isSuccessful: Boolean,
            val message: UiText.StringResource
        ): BackupRestoreStatus()
    }
}

