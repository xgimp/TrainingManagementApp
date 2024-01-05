package online.bacovsky.trainingmanagement.presentation.sms_screen


import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.bacovsky.trainingmanagement.data.repository.SmsRepository
import online.bacovsky.trainingmanagement.data.repository.TrainingRepository
import online.bacovsky.trainingmanagement.domain.model.ClientWithScheduledTrainings
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

const val TAG = "SmsScreenViewModel"

@OptIn(ExperimentalPermissionsApi::class)
@HiltViewModel
class SmsScreenViewModel @Inject constructor(
    private val trainingRepository: TrainingRepository,
    private val smsRepository: SmsRepository
): ViewModel() {

    var clientTrainingList: List<ClientWithScheduledTrainings> = emptyList()
        private set

    init {
        viewModelScope.launch {
            
            val nexMonday = LocalDateTime.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .toLocalDate()
                .atStartOfDay()

            val nextSunday = nexMonday.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
                .toLocalDate()
                .atTime(LocalTime.MAX)

            clientTrainingList = trainingRepository.getClientListWithTrainingsBetweenTime(
                startTime = nexMonday,
                endTime = nextSunday
            )
        }
    }

    fun onEvent(event: SmsScreenEvent) {
        when(event) {
            is SmsScreenEvent.OnBulkSmsSendClick -> {
                clientTrainingList.forEach {
                    viewModelScope.launch {
                        val telNumber = it.client.telephoneNumber
                        val smsText: String = it.trainingsToSmsText(event.context)
                        sendSms(telNumber, smsText)
                        delay(1000)
                    }

                }
            }
        }
    }

    private fun sendSms(telNumber: String, smsText: String) {
        smsRepository.sendSms(telNumber = telNumber, smsText = smsText)
    }

//    private val _uiEvent = Channel<UiEvent>()
//    val iuEvent = _uiEvent.receiveAsFlow()

//    private fun sendUiEvent(event: UiEvent) {
//        viewModelScope.launch {
//            _uiEvent.send(event)
//        }
//    }
}