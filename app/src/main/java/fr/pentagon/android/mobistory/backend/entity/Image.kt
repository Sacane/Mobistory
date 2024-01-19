package fr.pentagon.android.mobistory.backend.entity

import android.net.Uri
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import fr.pentagon.android.mobistory.backend.Event
import java.util.UUID


@Entity(
    tableName = "image",
)
data class Image(
    val link: Uri,
    @PrimaryKey
    val imageId: UUID = UUID.randomUUID(),
    val eventId: UUID
)

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertImage(image: Image)

    @Transaction
    @Query("SELECT * FROM image WHERE imageId = :uuid")
    suspend fun findById(uuid: UUID): Image
}

@Entity(
    tableName = "event_image_join",
    primaryKeys = ["eventId", "imageId"],
    foreignKeys = [
        ForeignKey(
            entity = Event::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Image::class,
            parentColumns = ["imageId"],
            childColumns = ["imageId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EventImageJoin(
    val eventId: UUID,
    val imageId: UUID
)

@Dao
interface EventImageJoinDao{
    @Insert
    fun save(eventImageJoin: EventImageJoin)
}

data class EventWithImages(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventId"
    )
    val images: List<Image>
)