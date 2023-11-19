package online.bacovsky.trainingmanagement.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class TrainingWithClient(
    @Embedded val training: Training,
    @Relation(
        parentColumn = "clientId",
        entityColumn = "id"
    )
    val client: Client
)

