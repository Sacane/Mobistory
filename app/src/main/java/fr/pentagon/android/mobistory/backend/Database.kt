package fr.pentagon.android.mobistory.backend

import android.content.Context
import android.net.Uri
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import fr.pentagon.android.mobistory.backend.entity.EventImageJoin
import fr.pentagon.android.mobistory.backend.entity.EventImageJoinDao
import fr.pentagon.android.mobistory.backend.entity.Image
import fr.pentagon.android.mobistory.backend.entity.ImageDao
import fr.pentagon.android.mobistory.backend.entity.Keyword
import fr.pentagon.android.mobistory.backend.entity.KeywordDao
import fr.pentagon.android.mobistory.backend.entity.KeywordEventJoin
import fr.pentagon.android.mobistory.backend.entity.KeywordEventJoinDao
import java.util.Date

@androidx.room.Database(
    entities = [
        Event::class,
        Image::class,
        KeywordEventJoin::class,
        EventImageJoin::class,
        Keyword::class
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
//        fun imageDao(): ImageDao {
//            require(::INSTANCE.isInitialized) { UNINITIALIZED_MESSAGE }
//            return INSTANCE.imageDao()
//        }
//        fun eventImageJoinDao(): EventImageJoinDao{
//            require(::INSTANCE.isInitialized) { UNINITIALIZED_MESSAGE }
//            return INSTANCE.eventImageJoinDao()
//        }
    }
    abstract fun imageDao(): ImageDao
//    abstract fun eventImageJoinDao(): EventImageJoinDao
//    abstract fun keywordDao(): KeywordEventJoinDao
    abstract fun eventDao(): EventDao
    abstract fun eventImageJoinDao(): EventImageJoinDao
    abstract fun keywordDao(): KeywordDao
    abstract fun keywordEventJoinDao(): KeywordEventJoinDao
}

class DateConverter {

    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun toDate(date: Long): Date = Date(date)

}

class UriConverter {
    @TypeConverter
    fun fromUri(uri: Uri): String = uri.toString()

    @TypeConverter
    fun toUri(uri: String): Uri = Uri.parse(uri)
}