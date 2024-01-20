package fr.pentagon.android.mobistory.backend.entity

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import fr.pentagon.android.mobistory.backend.Event
import java.util.UUID

@Entity
data class Keyword(
    val label: String,
    @PrimaryKey
    val keywordId: UUID = UUID.randomUUID()
) {
    init {
        require(label.isNotBlank() && label.isNotEmpty())
    }
}

@Dao
interface KeywordDao{
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun save(keyword: Keyword)
    @Query("SELECT * FROM keyword WHERE keywordId = :id")
    suspend fun findById(id: UUID): Keyword?

    @Insert
    suspend fun saveAll(keywords: List<Keyword>)
    @Query("SELECT * FROM keyword")
    suspend fun getAll(): List<Keyword>
}

@Entity(tableName = "keyword_event_join",
    primaryKeys = ["eventId", "keywordId"],
    foreignKeys = [
        ForeignKey(entity = Event::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Keyword::class,
            parentColumns = ["keywordId"],
            childColumns = ["keywordId"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class KeywordEventJoin(
    val eventId: Int,
    val keywordId: UUID
)
@Dao
interface KeywordEventJoinDao {
    @Insert
    suspend fun insert(crossRef: KeywordEventJoin)
}

data class EventWithKeywords(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "keywordId",
        associateBy = Junction(KeywordEventJoin::class)
    )
    val keywords: List<Keyword>
)

fun String.asKeyword(id: UUID = UUID.randomUUID()) = Keyword(keywordId = id, label = this)