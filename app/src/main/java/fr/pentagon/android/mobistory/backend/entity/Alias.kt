package fr.pentagon.android.mobistory.backend.entity

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import fr.pentagon.android.mobistory.backend.Event
import java.util.UUID

@Entity(tableName = "alias")
data class Alias(
    val label: String,
    val eventId: Int,
    @PrimaryKey val aliasId: UUID = UUID.randomUUID()
)

@Dao
interface AliasDao {
    @Insert(onConflict = ABORT)
    suspend fun insertAlias(alias: Alias)

    @Transaction
    @Query("SELECT * FROM alias a WHERE a.label = :label")
    suspend fun findByAlias(label: String): Alias?
}

data class EventWithAlias(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventId"
    )
    val aliases: List<Alias>
)