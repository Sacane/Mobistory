package fr.pentagon.android.mobistory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme

@Composable
fun EventTimelineDisplayer(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.background(color = Color.Green).weight(4f).fillMaxSize()) {}
        Box(modifier = Modifier.background(color = Color.Green, shape = GenericShape { size, layoutDirection ->
            moveTo(size.width - (size.width / 3), 0f)
            lineTo(size.width, size.height)
            lineTo(size.width, 0f)
            close()
        }).weight(1f).fillMaxSize()) {}
    }
}

@Preview(showBackground = true)
@Composable
fun EventTimelineDisplayerPreview() {
    MobistoryTheme {
        EventTimelineDisplayer()
    }
}
