package fr.pentagon.android.mobistory.backend.entity

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Relation
import fr.pentagon.android.mobistory.backend.Event
import java.util.Date
import java.util.UUID

@Entity
data class KeyDate(
    @PrimaryKey
    val keyDateId: UUID = UUID.randomUUID(),
    val date: Date
)

@Dao
interface KeyDateDao{
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun save(keyDate: KeyDate)
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
interface EventKeyDateJoinDao{
    @Insert
    suspend fun save(crossRef: EventKeyDateJoin)
}

data class EventWithKeyDate(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "keyDateId",
        associateBy = Junction(EventKeyDateJoin::class)
    )
    val keyDates: List<KeyDate>
)