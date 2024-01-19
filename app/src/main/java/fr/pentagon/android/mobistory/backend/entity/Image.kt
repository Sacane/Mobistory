package fr.pentagon.android.mobistory.backend.entity

import android.net.Uri
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
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

data class EventWithImages(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventId"
    )
    val images: List<Image>
)