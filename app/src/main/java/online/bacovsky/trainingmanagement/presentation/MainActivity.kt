package online.bacovsky.trainingmanagement.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import online.bacovsky.trainingmanagement.presentation.main_screen.MainScreen
import online.bacovsky.trainingmanagement.ui.theme.TrainingManagementTheme
import dagger.hilt.android.AndroidEntryPoint
import de.raphaelebner.roomdatabasebackup.core.RoomBackup

const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var roomBackup: RoomBackup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // init backup
        roomBackup = RoomBackup(this)

        setContent {
            TrainingManagementTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}
