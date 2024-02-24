package fr.pentagon.android.mobistory.frontend.component

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.jsoup.Jsoup


fun retrieveFromLabel(context: Context, url: String) {
    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        url, {response ->
            val document = Jsoup.parse(response)
            val mainContentDiv = document.select("div.mw-body-content")
            if(mainContentDiv.isNotEmpty()) {
                mainContentDiv.first()?.let { Log.i("final content", it.text()) }
            } else {
                Log.w("NO CONTENT", "There is not final content...")
            }
        }
    ) {error ->
        Log.w("error at retrieving -> ", error.message ?: "unknown")
    }
    queue.add(stringRequest)
}