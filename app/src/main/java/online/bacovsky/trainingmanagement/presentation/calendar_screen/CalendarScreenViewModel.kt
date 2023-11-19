package online.bacovsky.trainingmanagement.presentation.calendar_screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import online.bacovsky.trainingmanagement.R
import online.bacovsky.trainingmanagement.data.repository.CalendarEventRepository
import online.bacovsky.trainingmanagement.data.repository.ClientRepository
import online.bacovsky.trainingmanagement.data.repository.TrainingRepository
import online.bacovsky.trainingmanagement.domain.model.CalendarEntity
import online.bacovsky.trainingmanagement.domain.model.Training
import online.bacovsky.trainingmanagement.domain.model.TrainingWithClient
import online.bacovsky.trainingmanagement.domain.model.getEventId
import online.bacovsky.trainingmanagement.presentation.calendar_screen.add_training_modal.AddTrainingEvent
import online.bacovsky.trainingmanagement.presentation.calendar_screen.add_training_modal.AddTrainingFormState
import online.bacovsky.trainingmanagement.presentation.calendar_screen.training_detail_modal.TrainingDetailEvent
import online.bacovsky.trainingmanagement.presentation.calendar_screen.training_detail_modal.TrainingDetailFormState
import online.bacovsky.trainingmanagement.util.UiEvent
import online.bacovsky.trainingmanagement.util.UiText
import online.bacovsky.trainingmanagement.util.toCalendar
import online.bacovsky.trainingmanagement.util.toLocalDateTime
import online.bacovsky.trainingmanagement.util.validation.ValidateClient
import online.bacovsky.trainingmanagement.util.validation.ValidateTrainingStartDate
import online.bacovsky.trainingmanagement.util.validation.ValidateTrainingStartTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CalendarScreenViewModel @Inject constructor(
    private val clientRepository: ClientRepository,
    private var eventsRepository: CalendarEventRepository,
    private var trainingRepository: TrainingRepository,
    private val validateClient: ValidateClient,
    private val validateTrainingStartTime: ValidateTrainingStartTime,
    private val validateTrainingStartDate: ValidateTrainingStartDate
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val cancelTrainingEventChannel = Channel<TrainingCanceledEvent>()
    val canceledTrainingEvents = cancelTrainingEventChannel.receiveAsFlow()
    var calendarEntities by mutableStateOf(CalendarEntityEventState())
        private set

    var addTrainingFormState by mutableStateOf(AddTrainingFormState())
        private set

    var trainingDetailFormState by mutableStateOf(TrainingDetailFormState())
        private set

    // Choices for Client dropdown
    val clients = clientRepository.getAllActiveClients()

    var showAddTrainingModal by mutableStateOf(false)
        private set

    var showTrainingDetailModal by mutableStateOf(false)
        private set
    
    fun onEvent(event: CalendarScreenEvent) {
        when(event) {
            is CalendarScreenEvent.OnEventClick -> {
                showTrainingDetailModal = true
                val trainingId = event.calendarEntity.getEventId()
                viewModelScope.launch {
                    val trainingEntity = trainingRepository.getTrainingById(trainingId)
                    trainingDetailFormState = trainingDetailFormState.copy(training =  trainingEntity)
                }
            }
            is CalendarScreenEvent.OnLoadMore -> {
                Log.d(TAG, "onEvent: OnLoadMore")    
                fetchEvents(event.startDate, event.endDate)
            }
            is CalendarScreenEvent.OnRangeChanged -> {
                Log.d(TAG, "onEvent: OnRangeChanged")
                fetchEvents(startTime = event.startDate, event.endDate)
            }
            is CalendarScreenEvent.OnEmptyViewClick -> {
                showAddTrainingModal = true
                addTrainingFormState = addTrainingFormState.copy(
                    dateTime = event.time,
                    clientError = null,
                    dateError = null,
                    timeError = null,
                    paymentNote = ""
                )
            }
            is CalendarScreenEvent.OnAddTrainingModalDismiss -> {
                showAddTrainingModal = false
                addTrainingFormState = addTrainingFormState.copy(client = null)
            }
            is CalendarScreenEvent.OnQuickCreate -> {
                Log.d(TAG, "onEvent: selectedclientid ${event.clientId}")
                Log.d(TAG, "onEvent: inserting training")
                insertTraining(event.clientId, event.time, event.paymentNote)
            }

        }
    }
    
    fun onAddTrainingEvent(event: AddTrainingEvent) {
        when(event) {
            is AddTrainingEvent.ClientChanged -> {
                Log.d(TAG, "onAddTrainingEvent: client changed! ${event.client}")
                addTrainingFormState = addTrainingFormState.copy(client = event.client)
            }
            is AddTrainingEvent.Submit -> {
                addTrainingFormState = addTrainingFormState.copy(paymentNote = event.paymentNote)
                submitForm()
            }
        }
    }

    fun onTrainingDetailEvent(event: TrainingDetailEvent) {
        when(event) {
            is TrainingDetailEvent.OnDismiss -> {
                showTrainingDetailModal = false
                trainingDetailFormState = trainingDetailFormState.copy(training = null, isCanceled = false, paymentNote = "")
            }
            is TrainingDetailEvent.OnCanceledClick -> {
                trainingDetailFormState = trainingDetailFormState.copy(isCanceled = event.isCanceled)
            }
            is TrainingDetailEvent.OnChargeFee -> {
                cancelTraining(chargeFee = true)
            }
            is TrainingDetailEvent.OnNotChargeFee -> {
                trainingDetailFormState = trainingDetailFormState.copy(paymentNote = event.paymentNote)
                cancelTraining(chargeFee = false)
            }
        }
    }

    private fun fetchEvents(startTime: LocalDateTime, endTime: LocalDateTime) {
        Log.d(TAG, "fetchEvents: fetching events...")
        Log.d(TAG, "startTime: $startTime. EndTime: $endTime")

        viewModelScope.launch {
            eventsRepository.getEventsBetweenTime(startTime = startTime, endTime = endTime) { entities ->
                calendarEntities = calendarEntities.copy(entities = entities)
                Log.d(TAG, "fetchEvents: fetched events: ${entities.size}")
            }
        }
    }

    fun handleDrag(id: Long, newStartTime: LocalDateTime, newEndTime: LocalDateTime) {
        val existingEntity = calendarEntities.entities
            .filterIsInstance<CalendarEntity.Event>()
            .first { it.id == id }

        val newEntity = existingEntity.copy(
            startTime = newStartTime.toCalendar(),
            endTime = newEndTime.toCalendar(),
        )
        updateEntity(newEntity)
    }

    private fun updateEntity(newEntity: CalendarEntity.Event) {
        viewModelScope.launch {
            val trainingToUpdate = trainingRepository.getTrainingById(newEntity.id)
            val training = trainingToUpdate?.training

            val newTraining = training?.copy(
                startTime = newEntity.startTime.toLocalDateTime(),
                endTime = newEntity.endTime.toLocalDateTime()
            )
            newTraining?.let { updatedTraining ->
                trainingRepository.update(updatedTraining)
                Log.d(TAG, "updateEntity: entity $updatedTraining updated")

                fetchEvents(updatedTraining.startTime, updatedTraining.endTime)
            }
        }
    }

    private fun insertTraining(clientId: Long, time: LocalDateTime, paymentNote: String) {
        viewModelScope.launch {
            val startTime = time.withMinute(0)

            val client = clientRepository.getClientById(clientId)
            val training = Training(
                clientId = client?.id!!,
                startTime = startTime,
                endTime = startTime.plusHours(1),
                price = client.trainingPrice
            )
            trainingRepository.insertTrainingAndLogTransaction(
                training = TrainingWithClient(
                    client = client,
                    training = training
                ),
                paymentNote = paymentNote
            )
             fetchEvents(time, time)
        }
    }

    private fun submitForm() {
        val clientResult = validateClient.execute(addTrainingFormState.client)
        val startDateResult = validateTrainingStartDate.execute(addTrainingFormState.dateTime?.toLocalDate())
        val startTimeResult = validateTrainingStartTime.execute(addTrainingFormState.dateTime?.toLocalTime())

        addTrainingFormState = addTrainingFormState.copy(
            clientError = clientResult.errorMessage,
            dateError = startDateResult.errorMessage,
            timeError = startTimeResult.errorMessage
        )

        val hasError = listOf(clientResult, startTimeResult, startDateResult).any { !it.success }
        Log.d(TAG, "submitForm: invalid form data: $hasError")
        if (hasError) { return }

        // form is valid
        Log.d(TAG, "submitForm: data are valid :-)")
        viewModelScope.launch {
            val startDateTime = addTrainingFormState.dateTime?.withMinute(0)
            val endDateTime = startDateTime?.plusHours(1L)

            val training = Training(
                clientId = addTrainingFormState.client!!.id!!,
                startTime = startDateTime!!,
                endTime = endDateTime!!,
                price = addTrainingFormState.client!!.trainingPrice
            )
            trainingRepository.insertTrainingAndLogTransaction(
                training = TrainingWithClient(
                    client = addTrainingFormState.client!!,
                    training = training
                ),
                paymentNote = addTrainingFormState.paymentNote
            )

            showAddTrainingModal = false
            addTrainingFormState = addTrainingFormState.copy(
                client = null,
                clientError = null,
                dateError = null,
                timeError = null,
                dateTime = null,
                paymentNote = ""
            )
            fetchEvents(startDateTime, endDateTime)
//            validationEventChannel.send(ValidationEvent.Success)
        }
    }


    private fun cancelTraining(chargeFee: Boolean) {
        viewModelScope.launch {
            if (chargeFee) {
                // we actually doesn't have to log transaction because training is payed when created
                trainingRepository.cancelTraining(trainingDetailFormState.training!!.training)


                val validationMessage = UiText.StringResource(
                    resourceId = R.string.fee_charged_message,
                    args = arrayOf(
                        trainingDetailFormState.training!!.training.price,
                        trainingDetailFormState.training!!.client.name
                    )
                )
                cancelTrainingEventChannel.send(
                    TrainingCanceledEvent.Success(
                        message = validationMessage
                    )
                )
            }
            else {
                // no fee, so we have to return money for this
                trainingRepository.cancelTrainingAndLogPayment(trainingDetailFormState.training!!, trainingDetailFormState.paymentNote)


                val validationMessage = UiText.StringResource(
                    resourceId = R.string.no_fee_charged_message,
                    args = arrayOf(
                        trainingDetailFormState.training!!.training.price,
                        trainingDetailFormState.training!!.client.name
                    )
                )
                cancelTrainingEventChannel.send(
                    TrainingCanceledEvent.Success(
                        message = validationMessage
                    )
                )
            }
            showTrainingDetailModal = false
            fetchEvents(trainingDetailFormState.training!!.training.startTime, trainingDetailFormState.training!!.training.endTime)

            trainingDetailFormState = trainingDetailFormState.copy(
                training = null,
                isCanceled = false,
                paymentNote = ""
            )
        }
    }

    sealed class TrainingCanceledEvent {
        data class Success(val message: UiText.StringResource? = null): TrainingCanceledEvent()
    }
}