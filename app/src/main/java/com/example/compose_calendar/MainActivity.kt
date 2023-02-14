package com.example.compose_calendar

import android.os.Build
import android.os.Bundle
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
    var selectedYear by remember { mutableStateOf(2023) }
    var chosenYear by remember { mutableStateOf(2023) }
    var selectedMonth by remember { mutableStateOf(Calendar.FEBRUARY) }
    var chosenMonth by remember { mutableStateOf("FEBRUARY") }
    var expanded by remember { mutableStateOf(false) }
    var expandedMonth by remember { mutableStateOf(false) }

    val daysInMonth by remember( selectedYear, selectedMonth) {
        val year = Year.of(selectedYear)
        val month = Month.values()[selectedMonth]
        month.length(year.isLeap)
        mutableStateOf(month.length(year.isLeap))
    }

    Column() {
        Row {
            Text(
                "Select a year:      ",
                Modifier.clickable {
                    expanded = true
                },
            )
            Text(
                text = chosenYear.toString(),
                Modifier.clickable {
                    expanded = true
                },
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                for (year in 2020..2030) {
                    DropdownMenuItem(onClick = { selectedYear = year }) {
                        Text(year.toString(), color = Color.Black, modifier = Modifier.clickable {
                            expanded = false
                            chosenYear = year
                        })
                    }
                }
            }
        }
        Row {
            Text("Select a month:            ", modifier = Modifier.clickable {
                expandedMonth = true
            })
            Text(chosenMonth.toString())
            DropdownMenu(
                expanded = expandedMonth,
                onDismissRequest = { },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                for (month in Month.values()) {
                    DropdownMenuItem(onClick = { selectedMonth = month.ordinal }) {
                        Text(month.name.lowercase().capitalize(Locale.ROOT), modifier = Modifier.clickable {
                            expandedMonth = false
                            chosenMonth = month.name
                        })
                    }
                }
            }
        }
        // Add a row for displaying weekdays
        Row(Modifier.fillMaxWidth()) {
            val weekdays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            weekdays.forEach {
                Text(
                    text = it,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Get the first day of the selected month
        val firstDayOfMonth = LocalDate.of(selectedYear, selectedMonth + 1, 1)
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value



        LazyVerticalGrid(columns = GridCells.Fixed(7)) {

            val emptyCells = List(firstDayOfWeek - 1) { "" }

            items(emptyCells.size + daysInMonth) { dayOfMonth ->
                if (dayOfMonth < emptyCells.size) {
                    Text(text = "", modifier = Modifier.padding(4.dp))
                } else {
                    val date = LocalDate.of(selectedYear, selectedMonth + 1, dayOfMonth - emptyCells.size + 1)
                    val textColor = if (date.dayOfWeek == DayOfWeek.SUNDAY) Color.Red else Color.Black
                    Text(
                        text = (dayOfMonth - emptyCells.size + 1).toString(),
                        textAlign = TextAlign.Center,
                        color = textColor,
                        modifier = Modifier.padding(4.dp)
                    )
                }
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