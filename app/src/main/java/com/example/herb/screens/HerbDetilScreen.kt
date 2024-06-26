package com.example.herb.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.herb.R
import com.example.herb.database.entity.Herb
import com.example.herb.database.entity.StoredHerb
import com.example.herb.dialog.ReadExportHerbDetailHistoryDialog
import com.example.herb.dialog.ReadImportHerbDetailHistoryDialog
import com.example.herb.event.StoredHerbEvent
import com.example.herb.helper.StringHelper
import com.example.herb.state.HerbDetailState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HerbDetailScreen(
    state: HerbDetailState,
    onEvent: (StoredHerbEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = { HerbDetailHeader() },
        sheetContent = { HerbDetailFooter() },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
        ) {
            HistoryStoreHerb()
        }
    }
}

@Composable
fun HerbDetailHeader() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Herb Detail",
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center,
        fontSize = MaterialTheme.typography.headlineLarge.fontSize
    )
}


@Preview
@Composable
fun HerbDetailFooter() {
    val herb = Herb(
        herbName = "Name",
        totalWeight = 500.5f,
        avgPrice = 100000L
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {


            Button(
                modifier = Modifier.fillMaxWidth(.45f),
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = Color.Red,
                    contentColor = Color.White,
                ),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(text = stringResource(id = R.string.export_herb))
            }

            Button(
                modifier = Modifier.fillMaxWidth(.8f),
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = Color.Green,
                    contentColor = Color.White,
                ),
                shape = RoundedCornerShape(10.dp),

                ) {
                Text(text = stringResource(id = R.string.import_herb))
            }

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.avg_price),
                fontSize = 16.sp,
            )
            Text(
                text = StringHelper.numberToCurrency(herb.avgPrice, "") + " VND/g",
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                textAlign = TextAlign.Right,
                color = LocalContentColor.current.copy(alpha = 0.8f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.weight_remain),
                fontSize = 16.sp,
            )
            Text(
                text = StringHelper.floatToString(herb.totalWeight, 1) + " (g)",
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                textAlign = TextAlign.Right,
                color = LocalContentColor.current.copy(alpha = 0.8f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.total_money),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = StringHelper.numberToCurrency(
                    herb.avgPrice.toFloat() * herb.totalWeight,
                    ""
                ) + " VND",
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                textAlign = TextAlign.Right,
                fontWeight = FontWeight.Bold,
                color = LocalContentColor.current.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun MyDropDownBar(
    valueList: List<Pair<String, () -> Unit>>,
    modifier: Modifier = Modifier
) {
    if (valueList.isEmpty()) return

    var isExpand by remember { mutableStateOf(false) }

    var selectedText by remember { mutableStateOf(valueList[0].first) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    // Up Icon when expanded and down icon when collapsed
    val icon = if (isExpand)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedText,
            readOnly = true,
            onValueChange = { selectedText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to
                    // the DropDown the same width
                    textFieldSize = coordinates.size.toSize()
                },
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { isExpand = !isExpand })
            }
        )

        DropdownMenu(
            expanded = isExpand,
            onDismissRequest = { isExpand = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            valueList.forEach { pair ->
                DropdownMenuItem(onClick = {
                    selectedText = pair.first
                    isExpand = false
                    pair.second.invoke()
                },
                    text = { Text(text = pair.first) }
                )
            }
        }
    }
}


@Preview
@Composable
fun HistoryStoreHerb() {
    // TODO change back to false
    var isExpand by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .heightIn(max = 500.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { isExpand = !isExpand }
                .background(
                    shape = RoundedCornerShape(10.dp),
                    color = LocalContentColor.current.copy(alpha = 0.1f)
                ),
            contentAlignment = Alignment.Center,
        ) {

            Text(
                modifier = Modifier.padding(8.dp),
                text = stringResource(id = R.string.herb_history),
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            )
        }


        if (isExpand) {

            val valueList = listOf(
                Pair<String, () -> Unit>(
                    stringResource(id = R.string.date)
                ) { TODO() },
//                Pair<String, () -> Unit>(
//                    stringResource(id = R.string.buy_price)
//                ) { TODO() },
//                Pair<String, () -> Unit>(
//                    stringResource(id = R.string.buy_weight)
//                ) { TODO() },
                Pair<String, () -> Unit>(
                    stringResource(id = R.string.store_weight)
                ) { TODO() }
            )

            MyDropDownBar(valueList = valueList, modifier = Modifier.fillMaxWidth())

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .border(
                        width = 1.dp, color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                val storedHerb = StoredHerb(
                    herbID = 0,
                    buyDate = "22/06/2024",
                    buyPrice = 5000L,
                    buyWeight = 1000f,
                    processTime = 1f,
                    laborCost = 50000,
                    storeWeight = 500f,
                    additionalCost = 4000,
                    isImport = true
                )
                LazyColumn(
                    Modifier.fillMaxWidth()
                ) {

                }

                HerbDetailHistoryRow(storedHerb = storedHerb)
            }
        }

    }

}


@Composable
fun HerbDetailHistoryRow(storedHerb: StoredHerb) {
    var showDialog by remember { mutableStateOf(false) }

    val color = if (storedHerb.isImport)
        Color.Green.copy(alpha = 0.8f)
    else Color.Red.copy(alpha = 0.8f)
    val prefix = if (storedHerb.isImport) "" else "-"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showDialog = true }
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(5.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            modifier = Modifier
                .padding(8.dp),
            text = storedHerb.buyDate,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            textAlign = TextAlign.Right,
            color = LocalContentColor.current.copy(alpha = 0.8f)
        )

        Text(
            modifier = Modifier
                .padding(8.dp),
            text = prefix + StringHelper.floatToString(storedHerb.storeWeight, 1) + " (g)",
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            textAlign = TextAlign.Right,
            color = color
        )
    }

    if (showDialog) {
        if (storedHerb.isImport) {
            ReadImportHerbDetailHistoryDialog(storedHerb = storedHerb) {
                showDialog = false
            }
        }
        else {
            ReadExportHerbDetailHistoryDialog(storedHerb = storedHerb) {
                showDialog = false
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewHerbDetailScreen() {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = { HerbDetailHeader() },
        sheetContent = { HerbDetailFooter() },

        ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
        ) {
            HistoryStoreHerb()
        }
    }
}