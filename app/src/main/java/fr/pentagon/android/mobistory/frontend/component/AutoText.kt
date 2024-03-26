package fr.pentagon.android.mobistory.frontend.component

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun AutoText(text: String, modifier: Modifier = Modifier, color: Color = Color.Black) {
    SubcomposeLayout { constraints ->
        var i = 0
        fun findWellSizedText(range: IntRange): Placeable {
            val med = (range.last + range.first) / 2
            val c = subcompose(i++) {
                Text(text, modifier = modifier, fontSize = med.sp, color = color)
            }[0]
            val placeable = c.measure(constraints)
            Log.i(null, placeable.toString())
            return if (med == range.first) {
                Log.i("AutoText", "Size of text: $med sp")
                placeable
            } else if (placeable.measuredHeight >= constraints.maxHeight || placeable.measuredWidth >= constraints.maxWidth) {
                findWellSizedText(range.first until med)
            } else {
                findWellSizedText(med..range.last)
            }
        }

        val t = findWellSizedText(1..1000)
        layout(constraints.maxWidth, constraints.maxHeight) {
            t.place(0, 0)
        }
    }
}

@Composable
fun AutoTextV2(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = TextStyle(fontSize = 100.sp)
) {
    var resizedTextStyle by remember {
        mutableStateOf(style)
    }
    var shouldDraw by remember { mutableStateOf(false) }

    Text(text = text,
        color = Color.Black,
        modifier = modifier.drawWithContent {
            if (shouldDraw) {
                drawContent()
            }
        },
        softWrap = true,
        style = resizedTextStyle,
        onTextLayout = { result ->
            if (result.didOverflowWidth || result.didOverflowHeight) {
                resizedTextStyle = resizedTextStyle.copy(
                    fontSize = resizedTextStyle.fontSize * 0.95
                )
            } else {
                shouldDraw = true
            }
        })
}
