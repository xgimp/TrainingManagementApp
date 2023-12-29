package online.bacovsky.trainingmanagement.presentation.sms_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import online.bacovsky.trainingmanagement.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "SmsScreenViewModel"

@HiltViewModel
class SmsScreenViewModel @Inject constructor(

): ViewModel() {

//    private val _uiEvent = Channel<UiEvent>()
//    val iuEvent = _uiEvent.receiveAsFlow()

//    private fun sendUiEvent(event: UiEvent) {
//        viewModelScope.launch {
//            _uiEvent.send(event)
//        }
//    }

}