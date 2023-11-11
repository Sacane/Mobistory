package fr.pentagon.android.mobistory.backend

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant
import java.util.Date
import java.util.UUID


@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var db: Database
    private lateinit var eventDao: EventDao
    @Before
    fun init() {
        val context: Context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            context,
            Database::class.java
        ).build()
        eventDao = db.eventDao()
    }
}