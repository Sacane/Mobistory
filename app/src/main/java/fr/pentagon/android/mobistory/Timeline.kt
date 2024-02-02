package fr.pentagon.android.mobistory

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme

enum class EventTimeLinePosition {
    TOP, BOTTOM
}

@Composable
fun EventTimelineDisplayer(modifier: Modifier = Modifier, position: EventTimeLinePosition) {
    Column(modifier = modifier.fillMaxSize()) {
        if (position == EventTimeLinePosition.BOTTOM) {
            Box(
                modifier = Modifier
                    .background(color = Color.Green, shape = GenericShape { size, layoutDirection ->
                        moveTo(size.width - (size.width / 3), size.height)
                        lineTo(size.width, 0f)
                        lineTo(size.width, size.height)
                        close()
                    })
                    .weight(1f)
                    .fillMaxSize()
            ) {}
        }
        Box(
            modifier = Modifier
                .background(color = Color.Green)
                .weight(4f)
                .fillMaxSize()
        ) {}
        if (position == EventTimeLinePosition.TOP) {
            Box(
                modifier = Modifier
                    .background(color = Color.Green, shape = GenericShape { size, layoutDirection ->
                        moveTo(size.width - (size.width / 3), 0f)
                        lineTo(size.width, size.height)
                        lineTo(size.width, 0f)
                        close()
                    })
                    .weight(1f)
                    .fillMaxSize()
            ) {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventTimelineDisplayerPreview() {
    MobistoryTheme {
        EventTimelineDisplayer(position = EventTimeLinePosition.BOTTOM)
    }
}

@Composable
fun TimeLine(modifier: Modifier = Modifier) {
    val state = rememberScrollState()
    var arrowSize by remember { mutableStateOf(IntSize.Zero) }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val constraints = this.constraints

        Column {
            Row(
                modifier = Modifier
                    .weight(4f)
                    .horizontalScroll(state)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy((constraints.maxWidth / 2).pxToDp())
            ) {
                EventTimelineDisplayer(
                    modifier = Modifier.width((constraints.maxWidth / 2).pxToDp()),
                    position = EventTimeLinePosition.TOP
                )
                EventTimelineDisplayer(
                    modifier = Modifier.width((constraints.maxWidth / 2).pxToDp()),
                    position = EventTimeLinePosition.TOP
                )
                Box() {}
            }
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            )
            Row(
                modifier = Modifier
                    .weight(3f)
                    .onSizeChanged {
                        arrowSize = it
                    }
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .weight(8f)
                        .padding(0.dp, (arrowSize.height / 5).pxToDp())
                        .background(color = Color.Green)
                        .fillMaxSize()
                ) {}
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.Green,
                            shape = GenericShape { size, layoutDirection ->
                                Log.i(null, size.toString())
                                moveTo(0f, 0f)
                                lineTo(size.width, size.height / 2)
                                lineTo(0f, size.height)
                                close()
                            })
                        .weight(2f)
                        .fillMaxSize()
                ) {}
            }
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            )
            Row(
                modifier = Modifier
                    .weight(4f)
                    .horizontalScroll(state)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy((constraints.maxWidth / 2).pxToDp())
            ) {
                Box() {}
                EventTimelineDisplayer(
                    modifier = Modifier.width((constraints.maxWidth / 2).pxToDp()),
                    position = EventTimeLinePosition.BOTTOM
                )
                EventTimelineDisplayer(
                    modifier = Modifier.width((constraints.maxWidth / 2).pxToDp()),
                    position = EventTimeLinePosition.BOTTOM
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimeLinePreview() {
    MobistoryTheme {
        TimeLine()
    }
}
