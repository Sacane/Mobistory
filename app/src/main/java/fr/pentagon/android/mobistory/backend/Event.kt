package fr.pentagon.android.mobistory.backend

import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import fr.pentagon.android.mobistory.backend.entity.EventImageJoin
import fr.pentagon.android.mobistory.backend.entity.Image
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
    val images: List<Image>
)
