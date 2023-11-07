package fr.pentagon.android.mobistory.backend.entity

import android.net.Uri
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import fr.pentagon.android.mobistory.backend.Event
import java.util.UUID


@Entity(
    tableName = "image",
)
data class Image(
    val link: Uri,
    @PrimaryKey
    val id: UUID = UUID.randomUUID()
)

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: Image)
}

@Entity(tableName = "event_image_join",
    primaryKeys = ["eventId", "imageId"],
    foreignKeys = [
        ForeignKey(entity = Event::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Image::class,
            parentColumns = ["id"],
            childColumns = ["imageId"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class EventImageJoin(
    val eventId: UUID,
    val imageId: UUID
)
@Dao
interface EventImageJoinDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(crossRef: EventImageJoin)
}