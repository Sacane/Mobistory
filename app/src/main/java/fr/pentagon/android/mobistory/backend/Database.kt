package fr.pentagon.android.mobistory.backend

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

@androidx.room.Database(
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    companion object {
        private var INSTANCE: Database? = null
        fun open(context: Context){
            INSTANCE = Room.databaseBuilder(
                context,
                Database::class.java,
                "mobistory.db"
            ).build()
        }
        fun close() {
            require(INSTANCE != null){}
            INSTANCE?.close()
        }
    }
}