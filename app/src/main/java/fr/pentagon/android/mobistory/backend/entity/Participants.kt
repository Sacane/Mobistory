package fr.pentagon.android.mobistory.backend.entity

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import fr.pentagon.android.mobistory.backend.Event
import java.util.UUID

@Entity(tableName = "participant")
data class Participant(
    @PrimaryKey val participantId: UUID = UUID.randomUUID(),
    val name: String,
    val firstName: String? = null
)

@Dao
interface ParticipantDao{
    @Insert
    suspend fun save(participant: Participant)

    @Transaction
    @Query("SELECT * FROM participant WHERE participantId = :uuid")
    suspend fun findById(uuid: UUID): Participant
}

@Entity(
    tableName = "event_participant_join",
    primaryKeys = ["eventId", "participantId"],
    foreignKeys = [
        ForeignKey(
            entity = Event::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Participant::class,
            parentColumns = ["participantId"],
            childColumns = ["participantId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EventParticipantJoin(
    val eventId: UUID,
    val participantId: UUID
)

@Dao
interface EventParticipantJoinDao {
    @Insert
    fun save(eventParticipantJoin: EventParticipantJoin)
}

data class EventWithParticipants(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "participantId",
        associateBy = Junction(EventParticipantJoin::class)
    )
    val participants: List<Participant>
)