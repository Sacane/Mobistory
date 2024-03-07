package fr.pentagon.android.mobistory.backend

import android.content.Context
import android.net.Uri
import androidx.room.AutoMigration
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import fr.pentagon.android.mobistory.backend.entity.Alias
import fr.pentagon.android.mobistory.backend.entity.AliasDao
import fr.pentagon.android.mobistory.backend.entity.AppVersion
import fr.pentagon.android.mobistory.backend.entity.AppVersionDao
import fr.pentagon.android.mobistory.backend.entity.Coordinate
import fr.pentagon.android.mobistory.backend.entity.CoordinateDao
import fr.pentagon.android.mobistory.backend.entity.Country
import fr.pentagon.android.mobistory.backend.entity.CountryDao
import fr.pentagon.android.mobistory.backend.entity.CountryEventJoin
import fr.pentagon.android.mobistory.backend.entity.CountryEventJoinDao
import fr.pentagon.android.mobistory.backend.entity.EventLocationJoin
import fr.pentagon.android.mobistory.backend.entity.EventLocationJoinDao
import fr.pentagon.android.mobistory.backend.entity.EventParticipantJoin
import fr.pentagon.android.mobistory.backend.entity.EventParticipantJoinDao
import fr.pentagon.android.mobistory.backend.entity.EventTypeJoin
import fr.pentagon.android.mobistory.backend.entity.EventTypeJoinDao
import fr.pentagon.android.mobistory.backend.entity.FavoriteDao
import fr.pentagon.android.mobistory.backend.entity.FavoriteEvent
import fr.pentagon.android.mobistory.backend.entity.Image
import fr.pentagon.android.mobistory.backend.entity.ImageDao
import fr.pentagon.android.mobistory.backend.entity.KeyDate
import fr.pentagon.android.mobistory.backend.entity.KeyDateDao
import fr.pentagon.android.mobistory.backend.entity.Keyword
import fr.pentagon.android.mobistory.backend.entity.KeywordDao
import fr.pentagon.android.mobistory.backend.entity.KeywordEventJoin
import fr.pentagon.android.mobistory.backend.entity.KeywordEventJoinDao
import fr.pentagon.android.mobistory.backend.entity.Location
import fr.pentagon.android.mobistory.backend.entity.LocationDao
import fr.pentagon.android.mobistory.backend.entity.Participant
import fr.pentagon.android.mobistory.backend.entity.ParticipantDao
import fr.pentagon.android.mobistory.backend.entity.Type
import fr.pentagon.android.mobistory.backend.entity.TypeDao
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@androidx.room.Database(
    entities = [
        Event::class,
        Image::class,
        KeywordEventJoin::class,
        Keyword::class,
        Country::class,
        CountryEventJoin::class,
        FavoriteEvent::class,
        Participant::class,
        EventParticipantJoin::class,
        Alias::class,
        Coordinate::class,
        Location::class,
        EventLocationJoin::class,
        Type::class,
        EventTypeJoin::class,
        KeyDate::class,
        AppVersion::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateConverter::class,
    UriConverter::class
)
abstract class Database : RoomDatabase() {
    private var isInitialized: Boolean = false
    companion object {
        private const val UNINITIALIZED_MESSAGE = "Database instance has not been initialized yet"
        private lateinit var INSTANCE: Database
        val isInitialized: Boolean
            get() = ::INSTANCE.isInitialized
        fun open(context: Context){
            INSTANCE = Room.databaseBuilder(
                context,
                Database::class.java,
                "mobistory.db"
            ).build()
            INSTANCE.isInitialized = true
        }
        private fun requireInitialized() {
            require(INSTANCE.isInitialized){ UNINITIALIZED_MESSAGE}
        }
        fun close() {
            require(::INSTANCE.isInitialized){ UNINITIALIZED_MESSAGE }
            INSTANCE.close()
            INSTANCE.isInitialized = false
        }
        fun appVersionDao(): AppVersionDao{
            require(::INSTANCE.isInitialized) { UNINITIALIZED_MESSAGE }
            return INSTANCE.appVersionDao()
        }
        fun imageDao(): ImageDao {
            require(::INSTANCE.isInitialized) { UNINITIALIZED_MESSAGE }
            return INSTANCE.imageDao()
        }
        fun eventDao(): EventDao {
            require(::INSTANCE.isInitialized) { UNINITIALIZED_MESSAGE }
            return INSTANCE.eventDao()
        }
        fun keywordDao(): KeywordDao {
            require(::INSTANCE.isInitialized) { UNINITIALIZED_MESSAGE }
            return INSTANCE.keywordDao()
        }
        fun keywordEventJoinDao(): KeywordEventJoinDao {
            require(::INSTANCE.isInitialized) { UNINITIALIZED_MESSAGE }
            return INSTANCE.keywordEventJoinDao()
        }
        fun aliasDao(): AliasDao{
            require(::INSTANCE.isInitialized) { UNINITIALIZED_MESSAGE }
            return INSTANCE.aliasDao()
        }
        fun coordinateDao(): CoordinateDao {
            require(::INSTANCE.isInitialized) { UNINITIALIZED_MESSAGE }
            return INSTANCE.coordinateDao()
        }
        fun locationDao(): LocationDao{
            require(::INSTANCE.isInitialized) { UNINITIALIZED_MESSAGE }
            return INSTANCE.locationDao()
        }
        fun eventLocationJoinDao(): EventLocationJoinDao{
            require(::INSTANCE.isInitialized) { UNINITIALIZED_MESSAGE }
            return INSTANCE.eventLocationJoinDao()
        }
        fun typeDao(): TypeDao{
            require(::INSTANCE.isInitialized) { UNINITIALIZED_MESSAGE }
            return INSTANCE.typeDao()
        }
        fun eventTypeJoinDao(): EventTypeJoinDao{
            require(::INSTANCE.isInitialized) { UNINITIALIZED_MESSAGE }
            return INSTANCE.eventTypeJoinDao()
        }
        fun keyDateDao(): KeyDateDao{
            require(::INSTANCE.isInitialized) { UNINITIALIZED_MESSAGE }
            return INSTANCE.keyDateDao()
        }
        fun countryDao(): CountryDao {
            require(::INSTANCE.isInitialized) { UNINITIALIZED_MESSAGE }
            return INSTANCE.countryDao()
        }
        fun eventCountryJoinDao(): CountryEventJoinDao {
            require(::INSTANCE.isInitialized) { UNINITIALIZED_MESSAGE }
            return INSTANCE.eventCountryJoinDao()
        }

        fun participantDao(): ParticipantDao {
            require(::INSTANCE.isInitialized) { UNINITIALIZED_MESSAGE }
            return INSTANCE.participantDao()
        }
        fun eventParticipantJoinDao(): EventParticipantJoinDao{
            requireInitialized()
            return INSTANCE.eventParticipantJoinDao()
        }

        fun favoriteDao(): FavoriteDao{
            requireInitialized()
            return INSTANCE.favoriteDao()
        }

        fun clearAllTables() {
            requireInitialized()
            INSTANCE.clearAllTables()
        }
    }

    abstract fun appVersionDao(): AppVersionDao
    abstract fun imageDao(): ImageDao
    abstract fun aliasDao(): AliasDao
    abstract fun locationDao(): LocationDao
    abstract fun typeDao(): TypeDao
    abstract fun eventTypeJoinDao(): EventTypeJoinDao
    abstract fun eventLocationJoinDao(): EventLocationJoinDao
    abstract fun keyDateDao(): KeyDateDao
    abstract fun eventDao(): EventDao
    abstract fun coordinateDao(): CoordinateDao
    abstract fun keywordDao(): KeywordDao
    abstract fun keywordEventJoinDao(): KeywordEventJoinDao
    abstract fun countryDao(): CountryDao
    abstract fun eventCountryJoinDao(): CountryEventJoinDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun participantDao(): ParticipantDao
    abstract fun eventParticipantJoinDao(): EventParticipantJoinDao
}

class DateConverter {
    @TypeConverter
    fun stringToDate(value: String?): Date? {
        return value?.let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it) }
    }

    @TypeConverter
    fun dateToString(date: Date?): String? {
        return date?.let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it) }
    }
    @TypeConverter
    fun stringToDates(value: String?): KeyDatesContainer? {
        if (value.isNullOrBlank()) {
            return null
        }
        return KeyDatesContainer(value.split(",").mapNotNull {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.trim())
        })
    }

    @TypeConverter
    fun datesToString(dates: KeyDatesContainer?): String? {
        if(dates == null) return null
        return dates.keyDates.joinToString(",") {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it)
        }
    }
}

class UriConverter {
    @TypeConverter
    fun fromUri(uri: Uri): String = uri.toString()

    @TypeConverter
    fun toUri(uri: String): Uri = Uri.parse(uri)
}