package fr.pentagon.android.mobistory.backend.entity

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.pentagon.android.mobistory.backend.Event
import java.util.UUID

@Entity(tableName = "favorites",
    foreignKeys = [ForeignKey(
        entity = Event::class,
        parentColumns = ["eventId"],
        childColumns = ["favoriteEventId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("favoriteEventId")]
)
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val favoriteEventId: UUID
)

@Dao
interface FavoriteDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEvent)

    @Query("SELECT * FROM event INNER JOIN favorites ON event.eventId = favorites.favoriteEventId")
    suspend fun getAllFavorite(): List<Event>

    @Query("DELETE FROM favorites WHERE favoriteEventId = :eventId")
    suspend fun deleteFavorite(eventId: UUID)
}