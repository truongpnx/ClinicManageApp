package com.example.herb.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.herb.R
import com.example.herb.event.StoredHerbEvent
import com.example.herb.helper.StringHelper
import com.example.herb.helper.TimeHelper
import com.example.herb.state.HerbDetailState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportHerbDetailDialog(
    state: HerbDetailState,
    onEvent: (StoredHerbEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val color = Color.Green.copy(alpha = 0.8f)

    val buyCost = (state.buyPriceL.toFloatOrNull() ?: 0f) * (state.buyWeightF.toFloatOrNull() ?: 0f)
    val processCost =
        (state.processTimeF.toFloatOrNull() ?: 0f) * (state.laborCostL.toFloatOrNull() ?: 0f)

    val totalMoney = buyCost + processCost + (state.additionalCostL.toFloatOrNull() ?: 0f)

    val calendarState = rememberUseCaseState()
    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { date ->
            onEvent(StoredHerbEvent.SetBuyLocalDate(date))
        },
        config = CalendarConfig(
            yearSelection = true,
            monthSelection = true,
            cameraDate = state.buyLocalDate
        )
    )

    val clockState = rememberUseCaseState()
    ClockDialog(
        state = clockState,
        selection = ClockSelection.HoursMinutes { hours, minutes ->
            onEvent(StoredHerbEvent.SetBuyTime(LocalTime.of(hours, minutes)))
        },
        config = ClockConfig(
            defaultTime = state.buyTime,
            is24HourFormat = true
        )
    )


    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onEvent(StoredHerbEvent.HideDialog) },
        confirmButton = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            )
            {
                Button(onClick = {
                    onEvent(StoredHerbEvent.SaveStoredHerb)
                }) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        },
        text = {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    text = stringResource(id = R.string.date),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold
                )


                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                        .clickable { clockState.show() },
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    textAlign = TextAlign.Center,
                    color = LocalContentColor.current.copy(alpha = 0.8f),
                    text = TimeHelper.timeToString(state.buyTime, "HH:mm"),
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                        .clickable { calendarState.show() },
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    textAlign = TextAlign.Center,
                    color = LocalContentColor.current.copy(alpha = 0.8f),
                    text = TimeHelper.localDateToString(state.buyLocalDate)
                )


                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    text = stringResource(id = R.string.buy_price) + " (VND/g)",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                    onValueChange = {
                        val str = it.filter { char: Char -> char.isDigit() }
                        onEvent(StoredHerbEvent.SetBuyPrice(str))
                    },
                    value = state.buyPriceL,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )


                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),

                    text = stringResource(id = R.string.buy_weight) + " (g)",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold
                )

                FloatInputField(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    value = state.buyWeightF,
                    onValueChange = { first, second ->
                        onEvent(
                            StoredHerbEvent.SetBuyWeight(
                                "$first.${second}",
                            )
                        )
                    }
                )


                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    text = stringResource(id = R.string.process_time),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold
                )

                FloatInputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    value = state.processTimeF,
                    onValueChange = { first, second ->
                        onEvent(
                            StoredHerbEvent.SetProcessTime(
                                "$first.$second",
                            )
                        )
                    }
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    text = stringResource(id = R.string.labor_cost) + " (VND/h)",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                    onValueChange = {
                        val str = it.filter { c -> c.isDigit() }
                        onEvent(StoredHerbEvent.SetLaborCost(str))
                    },
                    value = state.laborCostL,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    text = stringResource(id = R.string.additional_cost) + " (VND)",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                    onValueChange = {
                        val str = it.filter { c -> c.isDigit() }
                        onEvent(StoredHerbEvent.SetAdditionalCost(str))
                    },
                    value = state.additionalCostL,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )


                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    text = stringResource(id = R.string.store_weight) + " (g)",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold
                )

                FloatInputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    value = state.storeWeightF,
                    onValueChange = { first, second ->
                        onEvent(
                            StoredHerbEvent.SetStoreWeight(
                                "$first.$second",
                            )
                        )
                    }
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = stringResource(id = R.string.total_money),
                    color = color,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    textAlign = TextAlign.Center
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                    text = StringHelper.numberToFormattedString(totalMoney) + " VND",
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    textAlign = TextAlign.Center,
                    color = color
                )
            }
        }

    )
}

@Composable
fun FloatInputField(
    value: String,
    onValueChange: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var paths by remember {
        mutableStateOf(StringHelper.splitStringFloatPattern(value))
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ) {

        TextField(
            modifier = Modifier
                .fillMaxWidth(0.7f),
            value = paths.first,
            onValueChange = {
                val newStr = it.filter { char -> char.isDigit() }
                paths = Pair(newStr, paths.second)
                onValueChange(newStr, paths.second) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )
        Text(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            text = ".",
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize
        )
        TextField(
            value = paths.second,
            onValueChange = {
                val newStr = it.filter { char -> char.isDigit() }
                paths = Pair(paths.first, newStr)
                onValueChange(paths.first, newStr) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true

        )
    }
}