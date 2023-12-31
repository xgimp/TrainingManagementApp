package online.bacovsky.trainingmanagement.presentation.sms_screen

import android.content.Context
import android.telephony.SmsManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import online.bacovsky.trainingmanagement.data.repository.SmsDataRepository
import online.bacovsky.trainingmanagement.domain.model.ClientWithScheduledTrainings
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

const val TAG = "SmsScreenViewModel"

@HiltViewModel
class SmsScreenViewModel @Inject constructor(
    private val smsDataRepository: SmsDataRepository
): ViewModel() {

    var clientTrainingList: List<ClientWithScheduledTrainings> = emptyList()
        private set

    init {
        viewModelScope.launch {
            val nexMonday = LocalDateTime.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .toLocalDate()
                .atStartOfDay()

            val nextSunday = LocalDateTime.now()
                .with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
                .toLocalDate()
                .atTime(LocalTime.MAX)

            clientTrainingList = smsDataRepository.getClientListWithTrainingsBetweenTime(
                startTime = nexMonday,
                endTime = nextSunday
            )
        }
    }

    private fun sendSms(telNumber: String, smsText: String, context: Context) {
        val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)
        smsManager.sendTextMessage(telNumber, null, smsText, null, null)
    }

//    private val _uiEvent = Channel<UiEvent>()
//    val iuEvent = _uiEvent.receiveAsFlow()

//    private fun sendUiEvent(event: UiEvent) {
//        viewModelScope.launch {
//            _uiEvent.send(event)
//        }
//    }

}