package com.example.herb.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
fun ExportHerbDetailDialog(
    state: HerbDetailState,
    onEvent: (StoredHerbEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val color = Color.Red.copy(alpha = 0.8f)

    val totalMoney =
        (state.buyPriceL.toFloatOrNull() ?: 0f) * (state.buyWeightF.toFloatOrNull() ?: 0f)

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
    val scrollableState = rememberScrollableState { .1f }


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
                    .scrollable(
                        state = scrollableState,
                        orientation = Orientation.Vertical
                    )
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
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
                    text = stringResource(id = R.string.sell_price) + " (VND/g)",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                    readOnly = true,
                    onValueChange = {
                        onEvent(StoredHerbEvent.SetBuyPrice(state.herb!!.avgPrice.toString()))
                    },
                    value = state.herb!!.avgPrice.toString(),
                )


                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),

                    text = stringResource(id = R.string.sell_weight) + " (g)",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold
                )

                FloatInputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    value = state.buyWeightF,
                    onValueChange = { first, second ->
                        onEvent(
                            StoredHerbEvent.SetSellValue(
                                "$first.${second}",
                                state.herb.avgPrice.toString()
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