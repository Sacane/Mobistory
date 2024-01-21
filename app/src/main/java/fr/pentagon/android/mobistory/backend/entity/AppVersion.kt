package fr.pentagon.android.mobistory.backend.entity

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
data class AppVersion(
    @PrimaryKey val id: Long = 1,
    val version: String
)

@Dao
interface AppVersionDao{
    @Query("SELECT version FROM appversion WHERE id = 1")
    suspend fun getVersion(): String?

    @Insert(onConflict = REPLACE)
    suspend fun save(appVersion: AppVersion)
}