package fr.pentagon.android.mobistory.backend

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import fr.pentagon.android.mobistory.backend.entity.EventImageJoin
import fr.pentagon.android.mobistory.backend.entity.Image
import fr.pentagon.android.mobistory.backend.entity.Keyword
import fr.pentagon.android.mobistory.backend.entity.KeywordEventJoin
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
    val id: UUID = UUID.randomUUID(),
    @Relation(
        parentColumn = "id",
        entity = Image::class,
        entityColumn = "id",
        associateBy = Junction(EventImageJoin::class)
    )
    val images: List<Image>,
    @Relation(
        parentColumn = "id",
        entity = Keyword::class,
        entityColumn = "id",
        associateBy = Junction(KeywordEventJoin::class)
    )
    val keywords: List<Keyword>
)

@Dao
interface EventDao{
    @Transaction
    @Query("SELECT * FROM event WHERE id = :eventId")
    suspend fun getAll(eventId: UUID): List<Event>

    @Insert
    suspend fun save()
}