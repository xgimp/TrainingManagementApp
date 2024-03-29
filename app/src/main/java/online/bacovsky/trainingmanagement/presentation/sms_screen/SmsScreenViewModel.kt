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
import online.bacovsky.trainingmanagement.domain.model.ClientWithScheduledTrainings
import online.bacovsky.trainingmanagement.domain.model.SmsHistory
import online.bacovsky.trainingmanagement.util.md5
import javax.inject.Inject

const val TAG = "SmsScreenViewModel"

@HiltViewModel
class SmsScreenViewModel @Inject constructor(
    private val trainingRepository: TrainingRepository,
    private val smsRepository: SmsRepository
): ViewModel() {

    //TODO: maybe expose SMSPreview instead of List<ClientWithScheduledTrainings> and clean up
    // SmsScreen.kt

    var state by mutableStateOf(SmsScreenState())
        private set

    var smsHistory = smsRepository.getSmsSentInTimeRange(state.nexMonday, state.nextSunday)
        private set

    init {
        viewModelScope.launch {

            val clientTrainingList = trainingRepository.getClientListWithTrainingsBetweenTime(
                startTime = state.nexMonday,
                endTime = state.nextSunday
            )
            state = state.copy(smsToSendList = clientTrainingList.sortedBy { it.client.name })
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
                        saveToHistory(clientWithScheduledTrainings, smsText)
                        state = state.copy(numberOfSentSms = index.toFloat() + 1f)
                    }
                }
            }
        }
    }

    private fun sendSms(telNumber: String, smsText: String) {
        smsRepository.sendSms(telNumber = telNumber, smsText = smsText)
    }

    private suspend fun saveToHistory(clientWithScheduledTrainings: ClientWithScheduledTrainings, smsText: String) {
        smsRepository.saveToHistory(
            SmsHistory(
                sentToClient = clientWithScheduledTrainings.client.id!!,
                startDate = state.nexMonday,
                endDate = state.nextSunday,
                smsText = smsText,
                smsTextHash = smsText.md5()
            )
        )
    }
}