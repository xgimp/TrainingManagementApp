package online.bacovsky.trainingmanagement.presentation.sms_screen


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import online.bacovsky.trainingmanagement.data.repository.SmsRepository
import online.bacovsky.trainingmanagement.data.repository.TrainingRepository
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

const val TAG = "SmsScreenViewModel"

@HiltViewModel
class SmsScreenViewModel @Inject constructor(
    private val trainingRepository: TrainingRepository,
    private val smsRepository: SmsRepository
): ViewModel() {

    var state by mutableStateOf(SmsScreenState())
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

            val clientTrainingList = trainingRepository.getClientListWithTrainingsBetweenTime(
                startTime = nexMonday,
                endTime = nextSunday
            )
            state = state.copy(smsToSendList = clientTrainingList)
        }
    }

    fun onEvent(event: SmsScreenEvent) {
        when(event) {
            is SmsScreenEvent.OnBulkSmsSendButtonClicked -> {
                state = state.copy(showConfirmDialog = !state.showConfirmDialog)
            }
            is SmsScreenEvent.OnBulkSmsSendDismissButtonClicked -> {
                state = state.copy(showConfirmDialog = false)
            }
            is SmsScreenEvent.OnConfirmSendClicked -> {
                state.smsToSendList.forEachIndexed { index, clientWithScheduledTrainings ->
                    viewModelScope.launch {
                        val telNumber = clientWithScheduledTrainings.client.telephoneNumber
                        val smsText: String = clientWithScheduledTrainings.trainingsToSmsText(event.context)

                        // we can send only one SMS per second
                        delay(index * 1_100L)
                        sendSms(telNumber, smsText)
                        state = state.copy(numberOfSentSms = index.toFloat() + 1f)
                    }
                }
                if (state.numberOfSentSms == state.smsToSendList.size.toFloat()) {
                    viewModelScope.launch {
                        delay(1_000 * 5)
                        state = state.copy(showConfirmDialog = !state.showConfirmDialog)
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