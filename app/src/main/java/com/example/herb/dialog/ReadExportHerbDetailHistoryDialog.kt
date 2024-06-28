package com.example.herb.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.herb.R
import com.example.herb.database.entity.StoredHerb
import com.example.herb.helper.StringHelper
import com.example.herb.helper.TimeHelper
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadExportHerbDetailHistoryDialog(
    storedHerb: StoredHerb,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {

    val color = Color.Red.copy(alpha = 0.8f)
    val defaultColor = Color.White.copy(alpha = 0.8f)

    val totalMoney = storedHerb.buyPrice * storedHerb.buyWeight

    val dateString = TimeHelper.localDateToString(
        TimeHelper.utcToZone(
            TimeHelper.longToUtilDate(storedHerb.buyDate), ZoneId.systemDefault()
        )
    )

    BasicAlertDialog(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .background(
                color = LocalContentColor.current.copy(alpha = 1f, 0.5f, 0.5f, 0.5f),
                shape = RoundedCornerShape(10.dp)
            ),
        onDismissRequest = { onDismiss.invoke() }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = stringResource(id = R.string.export_herb),
                color = color,
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                text = stringResource(id = R.string.date),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold,
                color = defaultColor
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = dateString,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Right,
                color = defaultColor

            )


            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                text = stringResource(id = R.string.sell_price),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold,
                color = defaultColor
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = StringHelper.numberToFormattedString(storedHerb.buyPrice, "") + " VND/g",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Right,
                color = defaultColor
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                text = stringResource(id = R.string.store_weight),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold,
                color = defaultColor
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = StringHelper.floatToString(storedHerb.buyWeight, 1) + " (g)",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Right,
                color = color
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = stringResource(id = R.string.total_money),
                color = color,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                textAlign = TextAlign.Center,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = "- " + StringHelper.numberToFormattedString(totalMoney, "") + " VND",
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                textAlign = TextAlign.Center,
                color = color
            )
        }
    }
}