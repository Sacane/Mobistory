package fr.pentagon.android.mobistory.backend.entity

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import java.util.UUID

@Entity
data class Location(
    @PrimaryKey val locationId: UUID = UUID.randomUUID(),
    val location: String
)

@Dao
interface LocationDao {

    @Transaction
    @Query("SELECT COUNT(*) FROM location l WHERE l.location = :label")
    suspend fun existsByLabel(label: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(location: Location)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(location: Iterable<Location>)

    @Transaction
    @Query("SELECT * FROM Location l WHERE l.location = :label")
    suspend fun findByLabel(label: String): Location?
}

@Entity(
    tableName = "event_location_join",
    primaryKeys = ["eventId", "locationId"],
    foreignKeys = [
        ForeignKey(
            entity = Event::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = Location::class,
            parentColumns = ["locationId"],
            childColumns = ["locationId"],
            onDelete = CASCADE
        )
    ]
)
data class EventLocationJoin(
    val eventId: Int,
    val locationId: UUID
)

@Dao
interface EventLocationJoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(eventLocationJoin: EventLocationJoin)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(eventLocationJoins: Iterable<EventLocationJoin>)
}

data class EventWithLocations(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "participantId",
        associateBy = Junction(EventLocationJoin::class)
    )
    val locations: List<Location>
)