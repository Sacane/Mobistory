package fr.pentagon.android.mobistory.backend.entity

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fr.pentagon.android.mobistory.backend.Database
import fr.pentagon.android.mobistory.backend.Event
import fr.pentagon.android.mobistory.backend.EventDao
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant
import java.util.Date
import java.util.UUID
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class FavoriteTest {
    private lateinit var db: Database
    private lateinit var eventDao: EventDao
    private lateinit var favoriteDao: FavoriteDao
    @Before
    fun init() {
        val context: Context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            context,
            Database::class.java
        ).build()
        eventDao = db.eventDao()
        favoriteDao = db.favoriteDao()
    }

    @Test
    fun simpleAddFavoriteTest() = runTest {
        val id = Random.nextInt()
        val event = Event(eventId = id, label = "Hello", startDate =  Date.from(Instant.now()), endDate = Date.from(
            Instant.now().minusSeconds(403820)), wikipedia = "I don't know")
        eventDao.save(event)
        assertNotNull(eventDao.findById(id))
        favoriteDao.addFavorite(FavoriteEvent(
            favoriteEventId = id
        ))
        assertEquals(1, favoriteDao.getAllFavorite().size)
    }

    @Test
    fun deleteFavoriteTest() = runTest {
        val id = Random.nextInt()
        val event = Event(eventId = id, label = "Hello", startDate =  Date.from(Instant.now()), endDate = Date.from(
            Instant.now().minusSeconds(403820)), wikipedia = "I don't know")
        eventDao.save(event)
        favoriteDao.addFavorite(FavoriteEvent(
            favoriteEventId = id
        ))

        favoriteDao.deleteFavorite(id)
        assertEquals(0, favoriteDao.getAllFavorite().size)
    }
}