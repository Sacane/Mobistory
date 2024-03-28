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
import androidx.room.Transaction
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
interface KeywordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(keyword: Keyword)

    @Query("SELECT * FROM keyword WHERE keywordId = :id")
    suspend fun findById(id: UUID): Keyword?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(keywords: Iterable<Keyword>)

    @Query("SELECT * FROM keyword")
    suspend fun getAll(): List<Keyword>

    @Transaction
    @Query("SELECT * FROM keyword k WHERE k.label = :keyword")
    suspend fun findByLabel(keyword: String): Keyword?
}

@Entity(
    tableName = "keyword_event_join",
    primaryKeys = ["eventId", "keywordId"],
    foreignKeys = [
        ForeignKey(
            entity = Event::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Keyword::class,
            parentColumns = ["keywordId"],
            childColumns = ["keywordId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class KeywordEventJoin(
    val eventId: Int,
    val keywordId: UUID
)

@Dao
interface KeywordEventJoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(crossRef: KeywordEventJoin)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(crossRefs: Iterable<KeywordEventJoin>)
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