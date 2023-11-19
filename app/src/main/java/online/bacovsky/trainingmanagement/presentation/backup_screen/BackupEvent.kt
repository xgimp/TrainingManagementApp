package online.bacovsky.trainingmanagement.presentation.backup_screen

import android.content.Context
import de.raphaelebner.roomdatabasebackup.core.RoomBackup

sealed class BackupEvent {
    data class OnCreateBackupClick(
        var roomBackup: RoomBackup,
        var context: Context
    ) : BackupEvent()

    data class OnRestoreBackupClick(
        var roomBackup: RoomBackup,
        var context: Context,
    ) : BackupEvent()

    data class RestartAppRequest(
        var roomBackup: RoomBackup,
        var context: Context,
    ): BackupEvent()
}
