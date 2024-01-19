package fr.pentagon.android.mobistory.backend

import android.content.Context
import android.net.Uri
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import fr.pentagon.android.mobistory.backend.entity.Alias
import fr.pentagon.android.mobistory.backend.entity.AliasDao
import fr.pentagon.android.mobistory.backend.entity.Country
import fr.pentagon.android.mobistory.backend.entity.CountryDao
import fr.pentagon.android.mobistory.backend.entity.CountryEventJoin
import fr.pentagon.android.mobistory.backend.entity.CountryEventJoinDao
import fr.pentagon.android.mobistory.backend.entity.EventImageJoinDao
import fr.pentagon.android.mobistory.backend.entity.EventKeyDateJoin
import fr.pentagon.android.mobistory.backend.entity.EventKeyDateJoinDao
import fr.pentagon.android.mobistory.backend.entity.EventParticipantJoin
import fr.pentagon.android.mobistory.backend.entity.EventParticipantJoinDao
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
import fr.pentagon.android.mobistory.backend.entity.Participant
import fr.pentagon.android.mobistory.backend.entity.ParticipantDao
import fr.pentagon.android.mobistory.backend.entity.StringConverter
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
        KeyDate::class,
        EventKeyDateJoin::class,
        FavoriteEvent::class,
        Participant::class,
        EventParticipantJoin::class,
        Alias::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateConverter::class,
    UriConverter::class,
    StringConverter::class
)
abstract class Database : RoomDatabase() {
    private var isInitialized: Boolean = false
    companion object {
        private const val UNINITIALIZED_MESSAGE = "Database instance has not been initialized yet"
        private lateinit var INSTANCE: Database
        val isInitialized: Boolean
            get() = INSTANCE.isInitialized
        fun open(context: Context){
            INSTANCE = Room.databaseBuilder(
                context,
                Database::class.java,
                "mobistory.db"
            ).build()
            INSTANCE.isInitialized = true
        }
        fun close() {
            require(::INSTANCE.isInitialized){ UNINITIALIZED_MESSAGE }
            INSTANCE.close()
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
    }
    abstract fun imageDao(): ImageDao
    abstract fun aliasDao(): AliasDao
//    abstract fun eventImageJoinDao(): EventImageJoinDao
//    abstract fun keywordDao(): KeywordEventJoinDao
    abstract fun eventDao(): EventDao
    abstract fun keywordDao(): KeywordDao
    abstract fun keywordEventJoinDao(): KeywordEventJoinDao
    abstract fun countryDao(): CountryDao
    abstract fun eventCountryJoinDao(): CountryEventJoinDao
    abstract fun keyDateDao(): KeyDateDao
    abstract fun eventKeyDateJoinDao(): EventKeyDateJoinDao
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
    fun stringListToString(list: List<String>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun stringToStringList(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
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