package online.bacovsky.trainingmanagement.presentation.sms_screen

import android.content.Context
import android.telephony.SmsManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

            val nextSunday = nexMonday.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
                .toLocalDate()
                .atTime(LocalTime.MAX)

            clientTrainingList = smsDataRepository.getClientListWithTrainingsBetweenTime(
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
                        sendSms(telNumber = telNumber, smsText = smsText, context = event.context)
                        Log.d(TAG, "onEvent: sms was send")
                        Log.d(TAG, "sleeping...")
                        delay(1000)
                        Log.d(TAG, "onEvent: done")
                    }

                }
            }
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