package fr.pentagon.android.mobistory.backend.entity

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import fr.pentagon.android.mobistory.backend.Event

@Entity(
    tableName = "favorites",
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
    val favoriteEventId: Int,
    val customLabel: String
)

data class FavoriteEventAndEvent(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "favoriteEventId"
    )
    val favoriteEvent: FavoriteEvent
)

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEvent)

    @Query("SELECT * FROM event INNER JOIN favorites ON event.eventId = favorites.favoriteEventId")
    suspend fun getAllFavorite(): List<FavoriteEventAndEvent>

    @Query("DELETE FROM favorites WHERE favoriteEventId = :eventId")
    suspend fun deleteFavorite(eventId: Int)

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE favoriteEventId = :eventId)")
    suspend fun isFavorited(eventId: Int): Boolean
}