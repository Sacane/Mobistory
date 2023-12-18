package fr.pentagon.android.mobistory.backend.json

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.serialization.json.Json
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class JsonFormatterTest {

    @Test
    fun shouldReturnNullWhenLoadAFakeFile() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        val content = ctx.loadJSONFile(-1)
        assertNull(content)
    }

    @Test
    fun shouldBeAbleToLoadAFileFromAndroidContext() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        val content = ctx.loadJSONFile(fr.pentagon.android.mobistory.test.R.raw.jsontest) ?: "Fail"
        assertEquals("""
            [
              {"id": 630, "label": {"fr": "Consulte de Lyon", "en": "Council of Lyon"}, "start_date": "1802-1-11", "end_date": "1802-1-26"},
              {"id": 4411, "label": {"fr": "bataille de Valmy", "en": "Battle of Valmy"}, "start_date": "1792-9-20"}
            ]
        """.trimIndent(), content
        )

        val events = Json.decodeFromString<List<EventDTO>>(content)
        assertEquals(2, events.size)
    }
}

