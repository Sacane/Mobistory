package fr.pentagon.android.mobistory.frontend.component

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.jsoup.Jsoup

@Composable
fun EventDetail(label: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {

        }
    }
}

fun findContentPageFromUrl(
    context: Context,
    url: String,
    onRetrieve: (String) -> Unit = { content ->
    Log.i(
        "Content",
        content
    )
}) {
    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        url, {response ->
            val document = Jsoup.parse(response)
            val mainContentDiv = document.select("div.mw-body-content")
            if(mainContentDiv.isNotEmpty()) {
                mainContentDiv.first()?.let {
                    onRetrieve(it.text())
                }
            } else {
                Log.w("NO CONTENT", "There is not final content...")
            }
        }
    ) {error ->
        Log.w("error at retrieving -> ", error.message ?: "unknown")
    }
    queue.add(stringRequest)
}