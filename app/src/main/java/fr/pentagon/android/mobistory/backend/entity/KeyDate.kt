package fr.pentagon.android.mobistory.backend.entity

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.Date
import java.util.UUID

@Entity(tableName = "key_date")
data class KeyDate(
    @PrimaryKey
    val keyDateId: UUID = UUID.randomUUID(),
    val date: Date,
    val eventId: Int
)

@Dao
interface KeyDateDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun save(keyDate: KeyDate)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun saveAll(keyDates: Iterable<KeyDate>)
}

@Entity(
    tableName = "event_keyDate_join",
    primaryKeys = ["eventId", "keyDateId"],
    foreignKeys = [
        ForeignKey(
            entity = Event::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = KeyDate::class,
            parentColumns = ["keyDateId"],
            childColumns = ["keyDateId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EventKeyDateJoin(
    val eventId: UUID,
    val keyDateId: UUID
)

@Dao
interface EventKeyDateJoinDao {
    @Insert
    suspend fun save(crossRef: EventKeyDateJoin)
}

data class EventWithKeyDate(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventId"
    )
    val keyDates: List<KeyDate>
)