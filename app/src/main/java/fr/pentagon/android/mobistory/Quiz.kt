package fr.pentagon.android.mobistory

import android.os.SystemClock
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@Composable
fun Quiz(modifier: Modifier = Modifier) {
    var running by remember { mutableStateOf(false) }

    Column(modifier = modifier
        .padding(20.dp)
        .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "Quiz", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.weight(1.4f))
        Text(text = "Le quiz est composé de 3 questions avec 4 possibilités pour chaque question mais une seule réponse possible, vous avez 10 secondes pour répondre à chaque question.")
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {}) {
            Text(text = "Démarrer")
        }
        Spacer(modifier = Modifier.weight(2f))
    }
}

@Composable
fun HorinzontalFillBar(modifier: Modifier = Modifier, backgroundColor: Color = Color.White, foregroundColor: Color = Color.Red, fillRatio: Float) {
    Row(modifier = modifier.fillMaxSize()) {
        when(fillRatio) {
            0.0f -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(color = backgroundColor))
            }
            1.0f -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(color = foregroundColor))
            }
            else -> {
                Box(modifier = Modifier
                    .weight(fillRatio)
                    .fillMaxSize()
                    .background(color = foregroundColor))
                Box(modifier = Modifier
                    .weight(1f - fillRatio)
                    .fillMaxSize()
                    .background(color = backgroundColor))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VerticalFillBarPreview() {
    MobistoryTheme {
        HorinzontalFillBar(fillRatio = 0.5f)
    }
}

@Composable
fun CountdownBar(modifier: Modifier = Modifier, initialCountdown: Long, elapsedTime: Long) {
    Box(modifier = modifier.fillMaxSize()) {
        val remainsSeconds = (initialCountdown / 1000) - (elapsedTime / 1000)
        val formatted = remainsSeconds.toString().padStart(2, '0')

        HorinzontalFillBar(fillRatio = if(elapsedTime > initialCountdown) {0.0f} else {((elapsedTime.toFloat() - initialCountdown.toFloat()) / initialCountdown.toFloat()).absoluteValue})
        Text(modifier = Modifier.align(Alignment.Center), text = formatted, fontSize = 40.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun CountdownBarPreview() {
    MobistoryTheme {
        CountdownBar(initialCountdown = 10000, elapsedTime = 10000)
    }
}

@Composable
fun Countdown(modifier: Modifier = Modifier, duration: Long, running: Boolean, onEnd: () -> Unit) {
    var elapsedTime by remember { mutableLongStateOf(0) }

    CountdownBar(modifier = modifier.fillMaxSize(), initialCountdown = duration, elapsedTime = elapsedTime)
    LaunchedEffect(running) {
        if (running) {
            val startTime = SystemClock.elapsedRealtime()

            while (true) {
                elapsedTime = SystemClock.elapsedRealtime() - startTime
                if (elapsedTime >= duration) {
                    onEnd()
                    break
                }

                delay(25L)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CountdownPreview() {
    MobistoryTheme {
        Countdown(duration = 10000, running = true, onEnd = {})
    }
}

@Composable
fun QuizQuestion(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f).fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("questionquestionquestionquestionquestionquestionquestionquestionquestionquestion")
        }
        Column(modifier = Modifier.weight(3f).fillMaxSize()) {
            Row(modifier = Modifier.weight(1f).fillMaxSize()) {
                Box(modifier = Modifier.weight(1f).fillMaxSize().padding(20.dp)) {
                    Box(modifier = Modifier.fillMaxSize().background(color = Color(red = 255, green = 213, blue = 141, alpha = 255)), contentAlignment = Alignment.Center) {
                        Text("answer1")
                    }
                }
                Box(modifier = Modifier.weight(1f).fillMaxSize().padding(20.dp)) {
                    Box(modifier = Modifier.fillMaxSize().background(color = Color(red = 255, green = 213, blue = 141, alpha = 255)), contentAlignment = Alignment.Center) {
                        Text("answer2")
                    }
                }
            }
            Row(modifier = Modifier.weight(1f).fillMaxSize()) {
                Box(modifier = Modifier.weight(1f).fillMaxSize().padding(20.dp)) {
                    Box(modifier = Modifier.fillMaxSize().background(color = Color(red = 255, green = 213, blue = 141, alpha = 255)), contentAlignment = Alignment.Center) {
                        Text("answer3")
                    }
                }
                Box(modifier = Modifier.weight(1f).fillMaxSize().padding(20.dp)) {
                    Box(modifier = Modifier.fillMaxSize().background(color = Color(red = 255, green = 213, blue = 141, alpha = 255)), contentAlignment = Alignment.Center) {
                        Text("answer4")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizQuestionPreview() {
    MobistoryTheme {
        QuizQuestion()
    }
}
