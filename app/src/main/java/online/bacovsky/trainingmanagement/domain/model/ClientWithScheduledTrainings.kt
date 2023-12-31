package online.bacovsky.trainingmanagement.domain.model

data class  ClientWithScheduledTrainings(
    val client: Client,
    val trainings: List<Training>
)