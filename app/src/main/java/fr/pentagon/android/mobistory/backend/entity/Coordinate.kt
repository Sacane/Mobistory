package fr.pentagon.android.mobistory.backend.entity

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

@Entity(tableName = "coordinate")
data class Coordinate(
    val value: String,
    val eventId: Int,
    @PrimaryKey val coordinateId: UUID = UUID.randomUUID()
)

@Dao
interface CoordinateDao {
    @Insert(onConflict = ABORT)
    suspend fun save(coordinate: Coordinate)
}

data class EventWithCoordinate(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventId"
    )
    val coordinates: List<Coordinate>
)