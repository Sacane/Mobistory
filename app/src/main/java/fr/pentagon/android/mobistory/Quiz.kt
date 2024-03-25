package fr.pentagon.android.mobistory

import android.os.SystemClock
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.pentagon.android.mobistory.backend.Database
import fr.pentagon.android.mobistory.backend.Event
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.TimeZone
import kotlin.math.absoluteValue

@Composable
fun Quiz(modifier: Modifier = Modifier) {
    var running by remember { mutableStateOf(false) }
    var over by remember { mutableStateOf(false) }
    var nbRemainingQuestions by remember { mutableIntStateOf(0) }
    var question by remember { mutableStateOf<Question?>(null) }
    var score by remember { mutableStateOf(0) }
    var questions by remember { mutableStateOf<List<Question>>(listOf()) }
    val context = LocalContext.current

    if (!running && !over) {
        Column(modifier = modifier
            .padding(20.dp)
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Quiz", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1.4f))
            Text(text = "Le quiz est composé de 3 questions avec 4 possibilités pour chaque question mais une seule réponse possible, vous avez 10 secondes pour répondre à chaque question.")
            Spacer(modifier = Modifier.weight(1f))
            if (questions.isNotEmpty()) {
                Button(onClick = {
                    running = true
                    nbRemainingQuestions = 3
                    question = questions.random()
                }) {
                    Text(text = "Démarrer")
                }
            }
            else {
                Text(text = "Questions en cours de chargement ...")
            }
            Spacer(modifier = Modifier.weight(2f))
        }
    }
    else if (running && nbRemainingQuestions > 0) {
        Column(modifier = modifier
            .padding(20.dp)
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Spacer(modifier = Modifier.weight(1f))
            QuestionManager(modifier = Modifier.weight(12f), question = question!!, onGoodAnswer = { score++ }, onCountdownEnd = {
                nbRemainingQuestions--
                if (nbRemainingQuestions == 0) {
                    running = false
                    over = true
                }
                else {
                    question = questions.random()
                }
            })
            Spacer(modifier = Modifier.weight(1f))
        }
    }
    else {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Nombre de réponses correctes : $score")
                Row {
                    Button(onClick = { over = false; score = 0 }) {
                        Text(text = "Retour")
                    }
                }
            }
        }
    }

    LaunchedEffect(Dispatchers.IO) {
        val events = Database.eventDao().getAll()

        questions = generateQuestion(events)
        Log.i(null, "QUESTIONS CREATED " + questions.size)
    }
}

@Preview(showBackground = true)
@Composable
fun QuizPreview() {
    MobistoryTheme {
        Quiz()
    }
}

@Composable
fun QuestionManager(modifier: Modifier = Modifier, question: Question, onGoodAnswer: () -> Unit, onCountdownEnd: () -> Unit) {
    var selectedAnswer by remember { mutableStateOf<Answer?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .weight(1f)
            .fillMaxSize()) {
            Countdown(duration = 10000, running = true, onEnd = {
                onCountdownEnd()

                if (selectedAnswer != null && selectedAnswer!!.goodAnswer) {
                    onGoodAnswer()
                }
                selectedAnswer = null
            })
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier
            .weight(10f)
            .fillMaxSize()) {
            QuizQuestion(question = question, selectedAnswer = selectedAnswer, selectAnswer = { answer ->
                selectedAnswer = answer
            })
        }
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
        Text(modifier = Modifier.align(Alignment.Center), text = formatted, fontSize = 30.sp)
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
            var startTime = SystemClock.elapsedRealtime()

            while (true) {
                elapsedTime = SystemClock.elapsedRealtime() - startTime
                if (elapsedTime >= duration) {
                    onEnd()
                    startTime = SystemClock.elapsedRealtime()
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
fun QuizQuestion(modifier: Modifier = Modifier, question: Question, selectedAnswer: Answer?, selectAnswer: (Answer) -> Unit) {
    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .weight(1f)
            .fillMaxSize(), contentAlignment = Alignment.Center) {
            val scrollState = rememberScrollState()

            Text(text = question.label, modifier = Modifier.verticalScroll(scrollState))
        }
        Column(modifier = Modifier
            .weight(3f)
            .fillMaxSize()) {
            Row(modifier = Modifier
                .weight(1f)
                .fillMaxSize()) {
                Answer(modifier = Modifier.weight(1f), answer = question.answers[0], selected = selectedAnswer?.label == question.answers[0].label, selectAnswer = { answer -> selectAnswer(answer)})
                Answer(modifier = Modifier.weight(1f), answer = question.answers[1], selected = selectedAnswer?.label == question.answers[1].label, selectAnswer = { answer -> selectAnswer(answer)})
            }
            Row(modifier = Modifier
                .weight(1f)
                .fillMaxSize()) {
                Answer(modifier = Modifier.weight(1f), answer = question.answers[2], selected = selectedAnswer?.label == question.answers[2].label, selectAnswer = { answer -> selectAnswer(answer)})
                Answer(modifier = Modifier.weight(1f), answer = question.answers[3], selected = selectedAnswer?.label == question.answers[3].label, selectAnswer = { answer -> selectAnswer(answer)})
            }
        }
    }
}

data class Answer(val label: String, val goodAnswer: Boolean)

@Composable
fun Answer(modifier: Modifier = Modifier, answer: Answer, selected: Boolean, selectAnswer: (Answer) -> Unit) {
    Box(modifier = modifier
        .fillMaxSize()
        .padding(20.dp)) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(
                color = if (selected) Color.Red else Color(
                    red = 255,
                    green = 213,
                    blue = 141,
                    alpha = 255
                )
            )
            .clickable { selectAnswer(answer) }, contentAlignment = Alignment.Center) {
            val scrollState = rememberScrollState()

            Text(text = answer.label, modifier = Modifier.verticalScroll(scrollState))
        }
    }
}

data class Question(val label: String, val answers: List<Answer>)

fun generateQuestion(events: List<Event>): List<Question>  {
    val questions = ArrayList<Question>()

    questions.addAll(generateQuestionsWithDate(events))
    questions.addAll(generateQuestionsWithDescription(events))

    return questions
}

private fun filterQuestionsWithDate(event: Event): Boolean {
    if (event.startDate != null && event.endDate == null) {
        return true
    }
    else if (event.startDate != null) {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"))
        cal.setTime(event.startDate)
        val startYear = cal.get(Calendar.YEAR)
        cal.setTime(event.endDate!!)
        val endYear = cal.get(Calendar.YEAR)

        return endYear == startYear
    }

    return false
}

private fun generateQuestionsWithDate(events: List<Event>): List<Question> {
    val questions = ArrayList<Question>()

    val filteredEvents = events.filter { event -> filterQuestionsWithDate(event) }

    for (event in filteredEvents) {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"))
        cal.setTime(event.startDate!!)
        val year = cal.get(Calendar.YEAR)
        val years = ArrayList<Int>(3)

        while (years.size < 3) {
            val randomYear = (((year - 20)..(year - 1)) + ((year + 1)..(year + 20))).random()
            if (!years.contains(randomYear)) years.add(randomYear)
        }

        questions.add(Question(label = "En quelle année cet évènement a eu lieu: " + event.getQuestionLabel() + " ?", answers = listOf(
            Answer(label = year.toString(), goodAnswer = true),
            Answer(label = years[0].toString(), goodAnswer = false),
            Answer(label = years[1].toString(), goodAnswer = false),
            Answer(label = years[2].toString(), goodAnswer = false)
        ).shuffled()))
    }

    return questions
}

private fun generateQuestionsWithDescription(events: List<Event>): List<Question> {
    val questions = ArrayList<Question>()

    val filteredEvents = events.filter { event -> event.description != null }

    for (event in filteredEvents) {
        val selected = ArrayList<Event>(4)

        selected.add(event)
        while (selected.size < 4) {
            val selectedEvent = filteredEvents.random()

            if (!selected.contains(selectedEvent)) {
                selected.add(selectedEvent)
            }
        }

        questions.add(Question(label = "A quel évènement correspond cette description: " + event.getQuestionDescription() + " ?", answers = listOf(
            Answer(label = event.getQuestionLabel(), goodAnswer = true),
            Answer(label = selected[1].getQuestionLabel(), goodAnswer = false),
            Answer(label = selected[2].getQuestionLabel(), goodAnswer = false),
            Answer(label = selected[3].getQuestionLabel(), goodAnswer = false)
        ).shuffled()))
    }

    return questions
}
