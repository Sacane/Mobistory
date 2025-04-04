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
data class Country(
    val label: String,

    @PrimaryKey
    val countryId: UUID = UUID.randomUUID()
) {
    init {
        require(label != "")
    }
}

@Dao
interface CountryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(country: Country)

    @Query("SELECT * FROM country WHERE countryId = :id")
    suspend fun findById(id: UUID): Country?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(countries: Iterable<Country>)

    @Query("SELECT * FROM country")
    suspend fun getAll(): List<Country>

    @Query("SELECT * FROM country WHERE label = :label")
    suspend fun findByLabel(label: String): Country?

    @Transaction
    @Query("SELECT COUNT(*) FROM Country c WHERE c.label = :label")
    suspend fun existsByLabel(label: String): Boolean
}

@Entity(
    tableName = "event_country_join",
    primaryKeys = ["eventId", "countryId"],
    foreignKeys = [
        ForeignKey(
            entity = Event::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Country::class,
            parentColumns = ["countryId"],
            childColumns = ["countryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CountryEventJoin(
    val eventId: Int,
    val countryId: UUID
)

@Dao
interface CountryEventJoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(crossRef: CountryEventJoin)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(crossRefs: Iterable<CountryEventJoin>)
}

data class EventWithCountry(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "countryId",
        associateBy = Junction(CountryEventJoin::class)
    )
    val countries: List<Country>
)

fun String.asCountry(id: UUID = UUID.randomUUID()) = Country(countryId = id, label = this)