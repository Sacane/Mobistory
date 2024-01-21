package fr.pentagon.android.mobistory.backend.entity

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import fr.pentagon.android.mobistory.backend.Event
import java.util.UUID

@Entity
data class Type(
    @PrimaryKey val typeId: UUID = UUID.randomUUID(),
    val label: String
)

@Dao
interface TypeDao{
    @Insert
    suspend fun save(type: Type)

    @Transaction
    @Query("SELECT * FROM type WHERE typeId = :uuid")
    suspend fun findById(uuid: UUID): Type?

    @Transaction
    @Query("SELECT COUNT(*) FROM type t WHERE t.label = :type")
    suspend fun existsByLabel(type: String): Boolean

    @Transaction
    @Query("SELECT * FROM type s WHERE s.label = :label")
    suspend fun findByLabel(label: String): Type?

}

@Entity(
    tableName = "event_type_join",
    primaryKeys = ["eventId", "typeId"],
    foreignKeys = [
        ForeignKey(
            entity = Event::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Type::class,
            parentColumns = ["typeId"],
            childColumns = ["typeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EventTypeJoin(
    val eventId: Int,
    val typeId: UUID
)

@Dao
interface EventTypeJoinDao {
    @Insert
    fun save(eventTypeJoin: EventTypeJoin)
}

data class EventWithTypes(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "typeId",
        associateBy = Junction(EventTypeJoin::class)
    )
    val types: List<Type>
)