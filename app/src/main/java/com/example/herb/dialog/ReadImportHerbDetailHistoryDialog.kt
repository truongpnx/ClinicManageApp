package com.example.herb.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.window.Dialog
import com.example.herb.R
import com.example.herb.database.entity.StoredHerb
import com.example.herb.helper.StringHelper

@Composable
fun ReadImportHerbDetailHistoryDialog(
    storedHerb: StoredHerb,
    onDismiss: () -> Unit
) {
    val headerText = if (storedHerb.isImport)
        stringResource(id = R.string.import_herb)
    else stringResource(id = R.string.export_herb)

    val color = if (storedHerb.isImport)
        Color.Green.copy(alpha = 0.8f)
    else Color.Red.copy(alpha = 0.8f)


    val totalMoney = storedHerb.buyPrice * storedHerb.buyWeight + storedHerb.processTime * storedHerb.laborCost + storedHerb.additionalCost

    Dialog(onDismissRequest = { onDismiss.invoke() }) {
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
                text = headerText,
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
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = storedHerb.buyDate,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Right,
                color = LocalContentColor.current.copy(alpha = 0.8f)
            )


            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                text = stringResource(id = R.string.buy_price),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = StringHelper.numberToCurrency(storedHerb.buyPrice, "") + " VND/g",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Right,
                color = LocalContentColor.current.copy(alpha = 0.8f)
            )


            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),

                text = stringResource(id = R.string.buy_weight),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = StringHelper.floatToString(storedHerb.buyWeight, 1) + " (g)",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Right,
                color = LocalContentColor.current.copy(alpha = 0.8f)
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

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = StringHelper.floatToString(storedHerb.processTime, 2) + " h",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Right,
                color = LocalContentColor.current.copy(alpha = 0.8f)
            )


            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                text = stringResource(id = R.string.labor_cost),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = StringHelper.numberToCurrency(storedHerb.laborCost, "") + " VND/h",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Right,
                color = LocalContentColor.current.copy(alpha = 0.8f)
            )


            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                text = stringResource(id = R.string.additional_cost),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = StringHelper.numberToCurrency(storedHerb.additionalCost, "") + " VND",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Right,
                color = LocalContentColor.current.copy(alpha = 0.8f)
            )


            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                text = stringResource(id = R.string.store_weight),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = StringHelper.floatToString(storedHerb.storeWeight,1) + " (g)",
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
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = StringHelper.numberToCurrency(totalMoney,"") + " VND",
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                textAlign = TextAlign.Center,
                color = color
            )
        }
    }
}