package fr.pentagon.android.mobistory.backend.entity

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import fr.pentagon.android.mobistory.backend.Database
import kotlinx.coroutines.runBlocking
import java.util.UUID
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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
    SELECT * FROM event INNER JOIN coordinate ON event.eventId = coordinate.eventId WHERE
        coordinate.latitude BETWEEN :minLatitude AND :maxLatitude AND
        coordinate.longitude BETWEEN :minLongitude AND :maxLongitude
    """)
    suspend fun nearbyEvents(
        minLatitude: Double,
        maxLatitude: Double,
        minLongitude: Double,
        maxLongitude: Double
    ): List<Event>


    @Query("SELECT * FROM coordinate WHERE coordinate.eventId = :eventId")
    suspend fun getCoordinateByEventId(eventId: Int): Coordinate?

    companion object {
        fun calculateBounds(latitude: Double, longitude: Double, distance: Double): Map<String, Double> {
            val earthRadius = 6371.0

            val latOffset = distance / earthRadius * (180.0 / Math.PI)
            val lonOffset = distance / (earthRadius * cos(Math.PI * latitude / 180.0)) * (180.0 / Math.PI)

            val minLatitude = latitude - latOffset
            val maxLatitude = latitude + latOffset
            val minLongitude = longitude - lonOffset
            val maxLongitude = longitude + lonOffset

            return mapOf(
                "minLatitude" to minLatitude,
                "maxLatitude" to maxLatitude,
                "minLongitude" to minLongitude,
                "maxLongitude" to maxLongitude
            )
        }
    }
}

suspend fun searchNearbyEvent(latitude: Double, longitude: Double): Event? {
    val bounds = CoordinateDao.calculateBounds(latitude, longitude, 15.0)
    val nearbyEvents = Database.coordinateDao().nearbyEvents(
        bounds["minLatitude"]!!,
        bounds["maxLatitude"]!!,
        bounds["minLongitude"]!!,
        bounds["maxLongitude"]!!
    )

    val sortedEvents = nearbyEvents.sortedBy { event ->
        val coordinate = runBlocking {
            return@runBlocking Database.coordinateDao().getCoordinateByEventId(event.eventId)
        } ?: throw AssertionError()
        calculateDistance(
            latitude,
            longitude,
            coordinate.latitude,
            coordinate.longitude
        )
    }
    return sortedEvents.firstOrNull()
}

private fun calculateDistance(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double
): Double {
    val earthRadius = 6371.0

    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadius * c
}

data class EventWithCoordinate(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventId"
    )
    val coordinates: List<Coordinate>
)