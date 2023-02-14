package com.example.compose_calendar

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose_calendar.ui.theme.ComposecalendarTheme
import java.text.DateFormatSymbols
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.util.*
import kotlin.time.Duration.Companion.days

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposecalendarTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Greeting(name: String) {
    SimpleCalendar()

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SimpleCalendar() {
    val context = LocalContext.current
    var selectedYear by remember { mutableStateOf(2023) }
    var chosenYear by remember { mutableStateOf(2023) }
    var selectedMonth by remember { mutableStateOf(Calendar.FEBRUARY) }
    var chosenMonth by remember { mutableStateOf("FEBRUARY") }
    var expanded by remember { mutableStateOf(false) }
    var expandedMonth by remember { mutableStateOf(false) }

    Column {
        Row {
            Text("Select a year:      ", Modifier.clickable { expanded = true })
            Text(chosenYear.toString(), Modifier.clickable { expanded = true })
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                for (year in 2020..2030) {
                    DropdownMenuItem(onClick = {
                        selectedYear = year
                        expanded = false
                        chosenYear = year
                    }) {
                        Text(year.toString())
                    }
                }
            }
        }
        Row {
            Text("Select a month:            ", Modifier.clickable { expandedMonth = true })
            Text(chosenMonth, Modifier.clickable { expandedMonth = true })
            DropdownMenu(expanded = expandedMonth, onDismissRequest = { expandedMonth = false }) {
                for (month in Month.values()) {
                    DropdownMenuItem(onClick = {
                        selectedMonth = month.ordinal
                        expandedMonth = false
                        chosenMonth = month.name
                    }) {
                        Text(month.name.lowercase().capitalize(Locale.ROOT))
                    }
                }
            }
        }
        Row(Modifier.fillMaxWidth()) {
            val weekdays = listOf("Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri")
            weekdays.forEach {
                Text(
                    text = it,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        val firstDayOfMonth = LocalDate.of(selectedYear, selectedMonth + 1, 1)
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value

        LazyVerticalGrid(columns = GridCells.Fixed(7)) {
            val emptyCells = List(firstDayOfWeek - 1) { null }
            val daysInMonth = firstDayOfMonth.lengthOfMonth()

            items(emptyCells.size + daysInMonth + 2) { dayOfMonth ->
                val day = dayOfMonth - emptyCells.size - 1
                val textColor =
                    if ((day + firstDayOfWeek - 3) % 7 == 5 || (day + firstDayOfWeek - 3) % 7 == 6) Color.Red else Color.Black
                Text(
                    text = if (day <= 0 || day > daysInMonth) " " else day.toString(),
                    textAlign = TextAlign.Center,
                    color = textColor,
                    modifier = Modifier
                        .padding(4.dp)
                        .height(40.dp)
                        .clickable {
                            Toast
                                .makeText(context, day.toString(), Toast.LENGTH_LONG)
                                .show()
                        }
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposecalendarTheme {
        Greeting("Android")
    }
}