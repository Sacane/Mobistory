package fr.pentagon.android.mobistory.backend.entity

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction
import fr.pentagon.android.mobistory.backend.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
    val popularity: Int = 0,
) : Serializable {
    val title: String
        get() = label.split("||").first()
            .replaceFirstChar { it.uppercase() }
            .ifEmpty { label.replace("||", "") }.replace(",", "")

    val brief: String
        get() = description?.split("||")?.first()
            ?.replaceFirstChar { it.uppercase() }
            ?.ifEmpty { description.replace("||", "") }
            ?: "No description available"

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

    fun getQuestionLabel(): String {
        return title
    }

    fun getQuestionDescription(): String {
        return if (getFrenchDescription().trim() != "") getFrenchDescription().trim() else description!!.split(
            "||"
        )[1]
    }

    private fun getFormatStartDate(): String {
        val formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        return if (this.startDate == null) "<empty date>" else formatDate.format(this.startDate)
    }

    private fun getFormatEndDate(): String {
        val formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        return if (this.endDate == null) "<empty date>" else formatDate.format(this.endDate)
    }


    suspend fun getCleanDate(): String {
        val startDate = getFormatStartDate()
        if (startDate == "<empty date>"){
            val id = this.eventId
            val formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
            val keyDates = withContext(Dispatchers.IO){
                Database.eventDao().findEventWithKeyDateById(id).keyDates
            }
            return if(keyDates.isEmpty()) "<empty date>" else formatDate.format(keyDates.first().date)
        }
        val endDate = getFormatEndDate()
        if (endDate == "<empty date>") return startDate
        return "$startDate - $endDate"
    }
}

data class KeyDatesContainer(
    @ColumnInfo(name = "keyDates")
    val keyDates: List<Date>
)

@Dao
interface EventDao {

    @Transaction
    @Query("SELECT COUNT(*) FROM event e WHERE e.label = :label")
    suspend fun existsByLabel(label: String): Boolean

    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :eventId")
    suspend fun findById(eventId: Int): Event?

    @Insert
    suspend fun save(event: Event)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun saveAll(events: Iterable<Event>)

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
    @Query(
        "SELECT participant.* FROM participant INNER JOIN event_participant_join " +
                "ON participant.participantId = event_participant_join.participantId " +
                "WHERE event_participant_join.eventId = :eventId"
    )
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
    @Query("SELECT * FROM event ORDER BY popularity LIMIT 50")
    suspend fun findEventsOrderedByPopularityLimit50(): List<Event>

    @Transaction
    @Query("SELECT * FROM event WHERE label LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%'")
    suspend fun getEventsContainsSearchQuery(searchQuery: String): List<Event>

    @Transaction
    @Query("SELECT * FROM event WHERE strftime('%d/%m', startDate) = :day || '/' || :month ORDER BY popularity DESC LIMIT 5")
    suspend fun findTop5EventByDay(day: String, month: String): List<Event>

    @Transaction
    @Query("SELECT * FROM event WHERE strftime('%m', startDate) = :month ORDER BY popularity DESC LIMIT 5")
    suspend fun findTop5EventByMonth(month: String): List<Event>

    @Transaction
    @Query("SELECT * FROM event WHERE label LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%'")
    suspend fun findEventsContainsSearchQuery(searchQuery: String): List<Event>

    @Transaction
    @Query("SELECT * FROM event WHERE (label LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%') AND startDate IS NOT NULL AND startDate <> 'null' ORDER BY startDate")
    suspend fun findEventsContainsSearchQueryOrderedAscendingDate(searchQuery: String): List<Event>

    @Transaction
    @Query("SELECT * FROM event WHERE (label LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%') AND startDate IS NOT NULL AND startDate <> 'null' ORDER BY startDate DESC")
    suspend fun findEventsContainsSearchQueryOrderedDescendingDate(searchQuery: String): List<Event>

    @Transaction
    @Query("SELECT * FROM event WHERE label LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%' ORDER BY popularity")
    suspend fun findEventsContainsSearchOrderedByPopularity(searchQuery: String): List<Event>
}