package fr.pentagon.android.mobistory.backend.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import fr.pentagon.android.mobistory.backend.Event
import java.util.UUID

@Entity
data class Keyword(
    val label: String,
    @PrimaryKey
    val id: UUID = UUID.randomUUID()
)

@Entity(tableName = "keyword_event_join",
    primaryKeys = ["eventId", "keywordId"],
    foreignKeys = [
        ForeignKey(entity = Event::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Keyword::class,
            parentColumns = ["id"],
            childColumns = ["keywordId"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class KeywordEventJoin(
    val eventId: UUID,
    val keywordId: UUID
)