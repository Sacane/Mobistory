package fr.pentagon.android.mobistory.backend

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction
import fr.pentagon.android.mobistory.backend.entity.EventWithAlias
import fr.pentagon.android.mobistory.backend.entity.EventWithCoordinate
import fr.pentagon.android.mobistory.backend.entity.EventWithCountry
import fr.pentagon.android.mobistory.backend.entity.EventWithImages
import fr.pentagon.android.mobistory.backend.entity.EventWithKeywords
import fr.pentagon.android.mobistory.backend.entity.Participant
import java.io.Serializable
import java.util.Date
import java.util.UUID

@Entity(tableName = "event")
data class Event(
    val label: String,
    val startDate: Date,
    val endDate: Date? = null, // It means the event can have only 1 reference date
    val description: String? = null,
    val wikipedia: String,
    val keyDates: KeyDatesContainer? = null,
    @PrimaryKey
    val eventId: UUID = UUID.randomUUID()
): Serializable {
    override fun equals(other: Any?): Boolean {
        return if (other is Event) {
            eventId == other.eventId
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return eventId.hashCode()
    }
}

data class KeyDatesContainer(
    @ColumnInfo(name = "keyDates")
    val keyDates: List<Date>
)

@Dao
interface EventDao{
    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :eventId")
    suspend fun findById(eventId: UUID): Event?

    @Insert
    suspend fun save(event: Event)

    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :eventId")
    suspend fun getImagesByEventId(eventId: UUID): EventWithImages

    @Transaction
    @Query("SELECT * FROM event")
    suspend fun getAll(): List<Event>


    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :uuid")
    suspend fun findEventWithImageById(uuid: UUID): EventWithImages?

    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :uuid")
    suspend fun findEventWithKeywordById(uuid: UUID): EventWithKeywords?

    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :uuid")
    suspend fun findEventWithCountryById(uuid: UUID): EventWithCountry?

    @Transaction
    @Query("SELECT participant.* FROM participant INNER JOIN event_participant_join " +
            "ON participant.participantId = event_participant_join.participantId " +
            "WHERE event_participant_join.eventId = :eventId")
    suspend fun findParticipantsByEventId(eventId: UUID): List<Participant>


    @Transaction
    @Query("SELECT e.keyDates FROM event e WHERE e.eventId = :eventId")
    suspend fun findAllKeyDate(eventId: UUID): KeyDatesContainer

    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :eventId")
    fun findEventWithAliasesById(eventId: UUID): EventWithAlias

    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :eventId")
    fun findEventWithCoordinateById(eventId: UUID): EventWithCoordinate
}