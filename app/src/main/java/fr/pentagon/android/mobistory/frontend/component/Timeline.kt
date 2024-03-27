package fr.pentagon.android.mobistory.frontend.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import fr.pentagon.android.mobistory.backend.entity.Event
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme

enum class EventTimeLinePosition {
    TOP, BOTTOM
}

@Composable
fun EventTimelineDisplayer(modifier: Modifier = Modifier, event: Event, position: EventTimeLinePosition) {
    Column(modifier = modifier) {
        if (position == EventTimeLinePosition.BOTTOM) {
            Box(
                modifier = Modifier
                    .background(
                        color = Color(red = 255, green = 213, blue = 141, alpha = 255),
                        shape = GenericShape { size, layoutDirection ->
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
                .background(color = Color(red = 255, green = 213, blue = 141, alpha = 255))
                .weight(4f)
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Box(modifier = Modifier
                .weight(2f)
                .fillMaxSize()) {
                AutoTextV2(text = event.title)
            }
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxSize()) {
                AutoText(text = event.getCleanDate())
            }
            Box(modifier = Modifier
                .weight(7f)
                .fillMaxSize()) {
                AutoTextV2(text = event.brief)
                // Text(fontSize = 5.em, text = event.description)
            }
        }
        if (position == EventTimeLinePosition.TOP) {
            Box(
                modifier = Modifier
                    .background(
                        color = Color(red = 255, green = 213, blue = 141, alpha = 255),
                        shape = GenericShape { size, layoutDirection ->
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

@Composable
fun TimeLine(modifier: Modifier = Modifier, events: List<Event>) {
    val stateRowTop = rememberLazyListState()
    val stateRowBottom = rememberLazyListState()
    var arrowSize by remember { mutableStateOf(IntSize.Zero) }
    val (top, bottom) = events.withIndex().partition { it.index % 2 == 0 }
    val topEvents = top.map { it.value }
    val bottomEvents = bottom.map { it.value }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val constraints = this.constraints

        Column {
            LazyRow(
                modifier = Modifier
                    .weight(4f)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy((constraints.maxWidth / 2).pxToDp()),
                state = stateRowTop
            ) {
                items(topEvents.size) { index ->
                    val event = topEvents[index]

                    Row(horizontalArrangement = Arrangement.spacedBy((constraints.maxWidth / 2).pxToDp())) {
                        EventTimelineDisplayer(
                            modifier = Modifier.width((constraints.maxWidth / 2).pxToDp()),
                            event = event,
                            position = EventTimeLinePosition.TOP
                        )
                        if (index == (topEvents.size - 1) && topEvents.size == bottomEvents.size) {
                            Box {}
                        }
                    }
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
                        .background(color = Color(red = 255, green = 213, blue = 141, alpha = 255))
                        .fillMaxSize()
                ) {}
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(red = 255, green = 213, blue = 141, alpha = 255),
                            shape = GenericShape { size, layoutDirection ->
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
            LazyRow(
                modifier = Modifier
                    .weight(4f)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy((constraints.maxWidth / 2).pxToDp()),
                state = stateRowBottom
            ) {
                items(bottomEvents.size) { index ->
                    val event = bottomEvents[index]

                    Row(horizontalArrangement = Arrangement.spacedBy((constraints.maxWidth / 2).pxToDp())) {
                        Box {}
                        EventTimelineDisplayer(
                            modifier = Modifier.width((constraints.maxWidth / 2).pxToDp()),
                            event = event,
                            position = EventTimeLinePosition.BOTTOM
                        )
                        if (index == (bottomEvents.size - 1) && topEvents.size > bottomEvents.size) {
                            Box {}
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(stateRowTop.firstVisibleItemScrollOffset) {
        stateRowBottom.scrollToItem(
            stateRowTop.firstVisibleItemIndex,
            stateRowTop.firstVisibleItemScrollOffset
        )
    }
    LaunchedEffect(stateRowBottom.firstVisibleItemScrollOffset) {
        stateRowTop.scrollToItem(
            stateRowBottom.firstVisibleItemIndex,
            stateRowBottom.firstVisibleItemScrollOffset
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimeLinePreview() {
    MobistoryTheme {
        TimeLine(events = listOf(
            Event(label = "LABEL", description = "DESCRIPTION"),
            Event(label = "LABEL2", description = "DESCRIPTION2"),
            Event(label = "LABEL2", description = "DESCRIPTION2"),
            Event(label = "LABEL2", description = "DESCRIPTION2"),
            Event(label = "LABEL2", description = "DESCRIPTION2")
        ))
    }
}
