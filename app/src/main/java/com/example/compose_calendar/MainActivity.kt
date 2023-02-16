package com.example.compose_calendar

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose_calendar.ui.theme.APP_BG
import com.example.compose_calendar.ui.theme.ComposecalendarTheme
import java.time.DateTimeException
import java.time.LocalDate
import java.time.Month
import java.util.*

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposecalendarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainPage()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainPage() {
    SimpleCalendar()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SimpleCalendar() {
    val context = LocalContext.current
    var selectedYear by remember { mutableStateOf(LocalDate.now().year) }
    var chosenYear by remember { mutableStateOf(LocalDate.now().year) }
    var selectedMonth by remember { mutableStateOf(LocalDate.now().month) }
    var chosenMonth by remember { mutableStateOf(LocalDate.now().month.name) }
    var expanded by remember { mutableStateOf(false) }
    var expandedMonth by remember { mutableStateOf(false) }
    var sendDate by remember { mutableStateOf(LocalDate.now()) }

    val dateList = remember { mutableStateListOf<LocalDate>() }

    Column(Modifier.verticalScroll(rememberScrollState()).background(APP_BG)) {
        Row {
            Row(modifier = Modifier.padding(16.dp)) {
                Text("Select a year:", Modifier.clickable { expanded = true },fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Text(chosenYear.toString(), Modifier.clickable { expanded = true })
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                for (year in 2023 downTo 1960) {
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
            Row(modifier = Modifier.padding(16.dp)) {
                Text("Select a month:", Modifier.clickable { expandedMonth = true },fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Text(chosenMonth.toString(), Modifier.clickable { expandedMonth = true })
            }

            DropdownMenu(expanded = expandedMonth, onDismissRequest = { expandedMonth = false }) {
                for (month in Month.values()) {
                    DropdownMenuItem(onClick = {
                        selectedMonth = month
                        expandedMonth = false
                        chosenMonth = month.name
                    }) {
                        Text(month.name.lowercase().capitalize(Locale.ROOT))
                    }
                }
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(BorderStroke(2.dp, Color.Black))) {
            val weekdays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            weekdays.forEach {
                Text(
                    text = it,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                )
            }
        }
        val firstDayOfMonth = LocalDate.of(selectedYear, selectedMonth, 1)
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value

        Box(Modifier.padding(16.dp).border(BorderStroke(1.dp, Color.Black))) {
            LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier
                .height(195.dp)
                .padding(0.dp,8.dp)
                ) {
                val emptyCells = List(firstDayOfWeek - 1) { null }
                val daysInMonth = firstDayOfMonth.lengthOfMonth()
                items(emptyCells.size + daysInMonth + 2) { dayOfMonth ->
                    val day = dayOfMonth - emptyCells.size + 1
                    val textColor =
                        if ((day + firstDayOfWeek - 3) % 7 == 4 || (day + firstDayOfWeek - 3) % 7 == 5) Color.Red else Color.Black
                    var backgroundColor = Color.Transparent

                    if (day >= 1 || day > daysInMonth) {
                        val currentDate = try {
                            LocalDate.of(selectedYear, selectedMonth, day)
                        } catch (e: DateTimeException) {
                            null
                        }
                        backgroundColor =
                            if (dateList.contains(currentDate)) Color.Gray else Color.Transparent
                    }
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = if (day <= 0 || day > daysInMonth) " " else day.toString(),
                            textAlign = TextAlign.Center,
                            color = textColor,
                            modifier = Modifier
                                .background(backgroundColor)
                                .width(32.dp)
                                .height(32.dp)
                                .clickable(
                                    enabled = !(day <= 0 || day > daysInMonth)
                                ) {

                                    sendDate = LocalDate.of(selectedYear, selectedMonth, day)
                                    onDateClick(sendDate, dateList)
                                },
                        )
                    }


                }
            }
        }
        Text("Chosen Dates:", Modifier.padding(16.dp),fontWeight = FontWeight.Bold)
        LazyColumn(
            Modifier
                .height(350.dp)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            items(dateList.size) { date ->
                Box(modifier = Modifier
                    .padding(8.dp)
                    .border(BorderStroke(1.dp, Color.Black)) ){
                    Text(text = dateList.get(date).toString(), modifier = Modifier.padding(8.dp))
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }

    }
}

fun onDateClick(date: LocalDate, dateList: MutableList<LocalDate>) {

    if (dateList.contains(date)) {
        dateList.remove(date)
    } else {
        dateList.add(date)
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposecalendarTheme {
        MainPage()
    }
}