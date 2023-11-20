package fr.pentagon.android.mobistory.backend.entity

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fr.pentagon.android.mobistory.backend.Database
import fr.pentagon.android.mobistory.backend.Event
import fr.pentagon.android.mobistory.backend.EventDao
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant
import java.util.Date
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class CountryTest {
    private lateinit var db: Database
    private lateinit var eventDao: EventDao
    private lateinit var eventCountryJoinDao: CountryEventJoinDao
    private lateinit var countryDao: CountryDao


    @Before
    fun init() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            ctx,
            Database::class.java
        ).build()
        eventDao = db.eventDao()
        eventCountryJoinDao = db.eventCountryJoinDao()
        countryDao = db.countryDao()
    }

    @Test
    fun simpleInsertCountryTest() = runTest {
        val id = UUID.randomUUID()
        val country = Country(countryId = id, label = "FRANCE")
        countryDao.save(country)
        val countryInDb = countryDao.findById(id)
        assertEquals(country, countryInDb)
    }

    @Test
    fun registeredCountriesCanBeRetrieveWithEvent() = runTest{

        val idCountry1 = UUID.randomUUID()
        val idCountry2 = UUID.randomUUID()
        val idCountry3 = UUID.randomUUID()

        val country1 = "France".asCountry(idCountry1)
        val country2 = "Eng".asCountry(idCountry2)
        val country3 = "Dutch".asCountry(idCountry3)

        val eventId = UUID.randomUUID()
        val event = Event(eventId = eventId, label = "Hello", startDate =  Date.from(Instant.now()), endDate = Date.from(
            Instant.now().minusSeconds(403820)), wikipedia = "I don't know")


        val join = CountryEventJoin(eventId, idCountry1)
        val join2 = CountryEventJoin(eventId, idCountry2)
        val join3 = CountryEventJoin(eventId, idCountry3)

        eventDao.save(event)
        countryDao.save(country1)
        countryDao.save(country2)
        countryDao.save(country3)
        eventCountryJoinDao.insert(join)
        eventCountryJoinDao.insert(join2)
        eventCountryJoinDao.insert(join3)

        val eventWithCountries = eventDao.findEventWithCountryById(eventId)

        assertNotNull(eventWithCountries)

        val countries: List<Country> = eventWithCountries?.countries!!

        assertTrue(country1 in countries)
        assertTrue(country2 in countries)
        assertTrue(country3 in countries)
    }
}