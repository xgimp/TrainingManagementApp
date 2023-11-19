package online.bacovsky.trainingmanagement.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class ClientWithPayment(
    @Embedded val client: Client,
    @Relation(
        parentColumn = "id",
        entityColumn = "clientId"
    )
    val payments: List<ClientPayment>
)

