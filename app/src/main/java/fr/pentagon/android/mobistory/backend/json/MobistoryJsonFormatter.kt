package fr.pentagon.android.mobistory.backend.json

import android.content.Context
import android.util.Log

fun Context.loadJSONFile(resourceId: Int): String? {
    return try {
        val inputStream = this.resources.openRawResource(resourceId)
        inputStream.bufferedReader().useLines { lines ->
            lines.joinToString(separator = "\n")
        }
    } catch (e: Exception) {
        Log.e("exception", null, e)
        null
    }
}

