package fr.pentagon.android.mobistory.backend

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction
import fr.pentagon.android.mobistory.backend.entity.EventWithCountry
import fr.pentagon.android.mobistory.backend.entity.EventWithImages
import fr.pentagon.android.mobistory.backend.entity.EventWithKeywords
import java.io.Serializable
import java.util.Date
import java.util.UUID

@Entity(tableName = "event")
data class Event(
    val label: String,
    val startDate: Date,
    val endDate: Date,
    val description: String? = null,
    val wikipedia: String,
    @PrimaryKey
    val eventId: UUID = UUID.randomUUID()
): Serializable

@Dao
interface EventDao{
    @Transaction
    @Query("SELECT * FROM event WHERE eventId = :eventId")
    suspend fun findById(eventId: UUID): Event

    @Insert
    suspend fun save(event: Event)

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
}