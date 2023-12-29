package online.bacovsky.trainingmanagement.presentation.sms_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import online.bacovsky.trainingmanagement.data.repository.SmsDataRepository
import online.bacovsky.trainingmanagement.domain.model.Client
import online.bacovsky.trainingmanagement.domain.model.Training
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

    var myList: Flow<Map<Client, List<Training>>> = emptyFlow()
    init {
        viewModelScope.launch {
            myList = smsDataRepository.getClientListWithTrainingsBetweenTime(
                startTime = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).toLocalDate().atStartOfDay(),
                endTime = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).toLocalDate().atTime(LocalTime.MAX)
            )
        }
    }

//    private val _uiEvent = Channel<UiEvent>()
//    val iuEvent = _uiEvent.receiveAsFlow()

//    private fun sendUiEvent(event: UiEvent) {
//        viewModelScope.launch {
//            _uiEvent.send(event)
//        }
//    }

}