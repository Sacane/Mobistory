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
fun EventTimelineDisplayer(modifier: Modifier = Modifier, event: FakeEvent, position: EventTimeLinePosition) {
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
        Column(
            modifier = Modifier
                .background(color = Color.Green)
                .weight(4f)
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Box(modifier = Modifier.weight(2f).fillMaxSize()) {
                AutoTextV2(text = event.name)
            }
            Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                AutoText(text = event.date)
            }
            Box(modifier = Modifier.weight(7f).fillMaxSize()) {
                AutoTextV2(text = event.description)
                // Text(fontSize = 5.em, text = event.description)
            }
        }
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
        EventTimelineDisplayer(event = FakeEvent("TITLE", "DESCRIPTION DESCRIPTIOND ESCRIPTIONDESCRIPTI", "12/02/2024"), position = EventTimeLinePosition.TOP)
    }
}

@Composable
fun TimeLine(modifier: Modifier = Modifier, events: List<FakeEvent>) {
    val state = rememberScrollState()
    var arrowSize by remember { mutableStateOf(IntSize.Zero) }
    val (top, bottom) = events.withIndex().partition { it.index % 2 == 0 }
    val topEvents = top.map { it.value }
    val bottomEvents = bottom.map { it.value }

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
                for (event in topEvents) {
                    EventTimelineDisplayer(
                        modifier = Modifier.width((constraints.maxWidth / 2).pxToDp()),
                        event = event,
                        position = EventTimeLinePosition.TOP
                    )
                }
                if (topEvents.size == bottomEvents.size) {
                    Box {}
                }
            }
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            )
            Row(modifier = Modifier
                .weight(3f)
                .onSizeChanged {
                    arrowSize = it
                }
                .fillMaxSize()) {
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
                if (bottomEvents.size > 0) {
                    Box {}
                }
                for (event in bottomEvents) {
                    EventTimelineDisplayer(
                        modifier = Modifier.width((constraints.maxWidth / 2).pxToDp()),
                        event = event,
                        position = EventTimeLinePosition.BOTTOM
                    )
                }
                if (topEvents.size > bottomEvents.size) {
                    Box {}
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimeLinePreview() {
    MobistoryTheme {
        TimeLine(events = listOf(
            FakeEvent("EVENT 1", "DESCRIPTION 1", "03/02/2024"),
            FakeEvent("EVENT 2", "DESCRIPTION 2", "03/02/2024"),
            FakeEvent("EVENT 3", "DESCRIPTION 3", "03/02/2024"),
            FakeEvent("EVENT 4", "DESCRIPTION 4", "03/02/2024")
        ))

        /*TimeLine(
            events = listOf(
                Event("EVENT 1", "DESCRIPTION 1", "03/02/2024"),
                Event("EVENT 2", "DESCRIPTION 2", "03/02/2024"),
                Event("EVENT 3", "DESCRIPTION 3", "03/02/2024"),
                Event("EVENT 4", "DESCRIPTION 4", "03/02/2024")
            )
        )*/
    }
}
