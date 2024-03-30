package fr.pentagon.android.mobistory.backend.entity

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import java.util.UUID

@Entity(tableName = "coordinate")
data class Coordinate(
    val latitude : Double,
    val longitude: Double,
    val eventId: Int,
    @PrimaryKey val coordinateId: UUID = UUID.randomUUID()
)

@Dao
interface CoordinateDao {
    @Insert(onConflict = ABORT)
    suspend fun save(coordinate: Coordinate)

    @Insert(onConflict = ABORT)
    suspend fun saveAll(coordinates: Iterable<Coordinate>)

    @Query("""
    SELECT * FROM event INNER JOIN coordinate WHERE
    6371 * ACOS(
        COS(RADIANS(coordinate.latitude)) * COS(RADIANS(:latitude)) *
        COS(RADIANS(:longitude) - RADIANS(coordinate.longitude)) +
        SIN(RADIANS(coordinate.latitude)) * SIN(RADIANS(:latitude))
    ) <= 15;
    """)
    suspend fun nearbyEventFrom(latitude: Double, longitude: Double): List<Event>
}

data class EventWithCoordinate(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventId"
    )
    val coordinates: List<Coordinate>
)