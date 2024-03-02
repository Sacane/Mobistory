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
import fr.pentagon.android.mobistory.backend.entity.EventWithKeyDate
import fr.pentagon.android.mobistory.backend.entity.EventWithKeywords
import fr.pentagon.android.mobistory.backend.entity.EventWithTypes
import fr.pentagon.android.mobistory.backend.entity.Participant
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.random.Random

@Entity(tableName = "event")
data class Event(
    val label: String,
    val startDate: Date? = null,
    val endDate: Date? = null, // It means the event can have only 1 reference date
    val description: String? = null,
    val wikipedia: String? = null,
    @PrimaryKey
    val eventId: Int = Random.nextInt(),
    val popularity: Int = 0
): Serializable {
    val title: String
        get() = label.split("||").first().ifEmpty { label.replace("||", "") }
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

    fun getFrenchDescription(): String {
        return if (this.description == null) "<empty description>" else this.description.split("||")[0]
    }

    fun getFormatStartDate(): String {
        val formatDate = SimpleDateFormat("MM/yyyy")
        return if (this.startDate == null) "<empty date>" else formatDate.format(this.startDate)
    }

    fun getFormatEndDate(): String {
        val formatDate = SimpleDateFormat("MM/yyyy")
        return if (this.endDate == null) "<empty date>" else formatDate.format(this.endDate)
    }

    fun getCleanDate(): String {
        val startDate = getFormatStartDate()
        if( startDate == "<empty date>") return startDate
        val endDate = getFormatEndDate()
        if(endDate == "<empty date>") return startDate
        return "$startDate - $endDate"
    }
}

data class KeyDatesContainer(
    @ColumnInfo(name = "keyDates")
    val keyDates: List<Date>
)

@Dao
interface EventDao{

    @Transaction
    @Query("SELECT COUNT(*) FROM event e WHERE e.label = :label")
    suspend fun existsByLabel(label: String): Boolean

    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :eventId")
    suspend fun findById(eventId: Int): Event?

    @Insert
    suspend fun save(event: Event)

    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :eventId")
    suspend fun getImagesByEventId(eventId: Int): EventWithImages

    @Transaction
    @Query("SELECT * FROM event")
    suspend fun getAll(): List<Event>


    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :uuid")
    suspend fun findEventWithImageById(uuid: Int): EventWithImages?

    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :uuid")
    suspend fun findEventWithKeywordById(uuid: Int): EventWithKeywords?

    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :uuid")
    suspend fun findEventWithCountryById(uuid: Int): EventWithCountry?

    @Transaction
    @Query("SELECT participant.* FROM participant INNER JOIN event_participant_join " +
            "ON participant.participantId = event_participant_join.participantId " +
            "WHERE event_participant_join.eventId = :eventId")
    suspend fun findParticipantsByEventId(eventId: Int): List<Participant>

    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :eventId")
    fun findEventWithAliasesById(eventId: Int): EventWithAlias

    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :eventId")
    fun findEventWithCoordinateById(eventId: Int): EventWithCoordinate


    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :eventId")
    fun findEventWithKeyDateById(eventId: Int): EventWithKeyDate

    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :eventId")
    fun findEventWithTypeById(eventId: Int): EventWithTypes

    @Transaction
    @Query("SELECT * FROM event LIMIT 50")
    suspend fun findEventsLimitOf50(): List<Event>
    @Transaction
    @Query("SELECT * FROM event WHERE startDate IS NOT NULL ORDER BY startDate")
    suspend fun findEventsOrderedAscendingDateLimit50(): List<Event>
    @Transaction
    @Query("SELECT * FROM event WHERE startDate IS NOT NULL ORDER BY startDate DESC")
    suspend fun findEventsOrderedDescendingDateLimit50(): List<Event>
    @Transaction
    @Query("SELECT * FROM event WHERE label LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%'")
    suspend fun findEventsContainsSearchQuery(searchQuery: String): List<Event>
    @Transaction
    @Query("SELECT * FROM event WHERE (label LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%') AND startDate IS NOT NULL AND startDate <> 'null' ORDER BY startDate")
    suspend fun findEventsContainsSearchQueryOrderedAscendingDate(searchQuery: String): List<Event>
    @Transaction
    @Query("SELECT * FROM event WHERE (label LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%') AND startDate IS NOT NULL AND startDate <> 'null' ORDER BY startDate DESC")
    suspend fun findEventsContainsSearchQueryOrderedDescendingDate(searchQuery: String): List<Event>
}