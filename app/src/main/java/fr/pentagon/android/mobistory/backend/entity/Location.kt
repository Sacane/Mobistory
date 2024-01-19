package fr.pentagon.android.mobistory.backend.entity

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import fr.pentagon.android.mobistory.backend.Event
import java.util.UUID

@Entity
data class Location(
    @PrimaryKey val locationId: UUID = UUID.randomUUID(),
    val location: String
)

@Dao
interface LocationDao{

    @Insert
    suspend fun save(location: Location)
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
    val eventId: UUID,
    val locationId: UUID
)

@Dao
interface EventLocationJoinDao{

    @Insert
    fun save(eventLocationJoin: EventLocationJoin)
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