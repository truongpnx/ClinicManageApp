package com.example.herb.screens

import android.app.Activity
import android.util.Log
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.herb.R
import com.example.herb.database.entity.Herb
import com.example.herb.database.entity.StoredHerb
import com.example.herb.dialog.ExportHerbDetailDialog
import com.example.herb.dialog.ImportHerbDetailDialog
import com.example.herb.dialog.ReadExportHerbDetailHistoryDialog
import com.example.herb.dialog.ReadImportHerbDetailHistoryDialog
import com.example.herb.event.StoredHerbEvent
import com.example.herb.helper.StringHelper
import com.example.herb.helper.TimeHelper
import com.example.herb.state.HerbDetailState
import com.example.herb.util.StoredHerbSortType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HerbDetailScreen(
    state: HerbDetailState, onEvent: (StoredHerbEvent) -> Unit, modifier: Modifier = Modifier
) {
    if (state.herb == null) (LocalContext.current as Activity).finish()

    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetContent = { HerbDetailFooter(state, onEvent) },
    ) {

        if (state.isDialogOn) {
            if (state.isImport) ImportHerbDetailDialog(state = state, onEvent = onEvent)
            else ExportHerbDetailDialog(state = state, onEvent = onEvent)
        }

        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
        ) {
            HerbDetailHeader(herb = state.herb!!)
            HistoryStoreHerb(state, onEvent)
        }
    }
}

@Composable
fun HerbDetailHeader(herb: Herb, modifier: Modifier = Modifier) {
    val activity = LocalContext.current as Activity

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = LocalContentColor.current.copy(
                    alpha = 0.4f, blue = 0.2f, green = 0.8f, red = 0f
                ), shape = RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp)
            ), horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 10.dp),
            text = herb.herbName,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
        )

        Button(
            onClick = { activity.finish() },
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = Color.Transparent, disabledContainerColor = Color.Transparent
            ),

            ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Arrow Back"
            )
        }
    }


}


@Composable
fun HerbDetailFooter(
    state: HerbDetailState, onEvent: (StoredHerbEvent) -> Unit
) {
    val hasValue = state.herb!!.totalWeight != null && state.herb.avgPrice != null

    Log.d("HerbDetailScreen", "HerbDetailFooter: ${state.herb}")

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
                onClick = { onEvent(StoredHerbEvent.ExportHerb) },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = Color.Red,
                    contentColor = Color.White,
                ),
                shape = RoundedCornerShape(10.dp),
                enabled = hasValue
            ) {
                Text(text = stringResource(id = R.string.export_herb))
            }

            Button(
                modifier = Modifier.fillMaxWidth(.8f),
                onClick = { onEvent(StoredHerbEvent.ImportHerb) },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = Color.Green,
                    contentColor = Color.White,
                ),
                shape = RoundedCornerShape(10.dp),

                ) {
                Text(text = stringResource(id = R.string.import_herb))
            }

        }

        if (hasValue) {

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.avg_price),
                    fontSize = 16.sp,
                )
                Text(
                    text = StringHelper.numberToFormattedString(
                        state.herb.avgPrice ?: 0,
                    ) + " VND/g",
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
                    text = StringHelper.floatToString(state.herb.totalWeight!!, 1) + " (g)",
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
                    text = StringHelper.numberToFormattedString(
                        (state.herb.avgPrice?.toFloat() ?: 0f) * state.herb.totalWeight!!
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
}

@Composable
fun MyDropDownBar(
    valueList: List<Pair<String, () -> Unit>>, modifier: Modifier = Modifier
) {
    if (valueList.isEmpty()) return

    var isExpand by remember { mutableStateOf(false) }

    var selectedText by remember { mutableStateOf(valueList[0].first) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    // Up Icon when expanded and down icon when collapsed
    val icon = if (isExpand) Icons.Filled.KeyboardArrowUp
    else Icons.Filled.KeyboardArrowDown

    Column(modifier = modifier) {
        OutlinedTextField(value = selectedText,
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
                Icon(icon, "contentDescription", Modifier.clickable { isExpand = !isExpand })
            })

        DropdownMenu(expanded = isExpand,
            onDismissRequest = { isExpand = false },
            modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            valueList.forEach { pair ->
                DropdownMenuItem(onClick = {
                    selectedText = pair.first
                    isExpand = false
                    pair.second.invoke()
                }, text = { Text(text = pair.first) })
            }
        }
    }
}


@Composable
fun HistoryStoreHerb(
    state: HerbDetailState, onEvent: (StoredHerbEvent) -> Unit
) {
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

            val valueList = listOf(Pair(
                stringResource(id = R.string.date)
            ) { onEvent(StoredHerbEvent.SetSortType(StoredHerbSortType.DATE)) },
//                Pair<String, () -> Unit>(
//                    stringResource(id = R.string.buy_price)
//                ) { TODO() },
//                Pair<String, () -> Unit>(
//                    stringResource(id = R.string.buy_weight)
//                ) { TODO() },
                Pair(
                    stringResource(id = R.string.store_weight)
                ) { onEvent(StoredHerbEvent.SetSortType(StoredHerbSortType.STORE_WEIGHT)) })

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
                        width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp)
                    )
            ) {
                LazyColumn(
                    Modifier.fillMaxWidth()
                ) {
                    items(state.storedHerbs) {
                        HerbDetailHistoryRow(it)
                    }
                }

            }
        }

    }

}

@Composable
fun HerbDetailHistoryRow(storedHerb: StoredHerb) {

    var showDialog by remember { mutableStateOf(false) }

    val color = if (storedHerb.isImport) Color.Green.copy(alpha = 0.8f)
    else Color.Red.copy(alpha = 0.8f)
    val prefix = if (storedHerb.isImport) "" else "-"

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { showDialog = true }
        .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(5.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {

        Text(
            modifier = Modifier.padding(8.dp),
            text = TimeHelper.localDateToString(
                TimeHelper.longToUtilDate(storedHerb.buyDate)
            ),
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            textAlign = TextAlign.Right,
            color = LocalContentColor.current.copy(alpha = 0.8f)
        )

        Text(
            modifier = Modifier.padding(8.dp),
            text = prefix + StringHelper.floatToString(storedHerb.storeWeight, 1) + " (g)",
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            textAlign = TextAlign.Right,
            color = color
        )
    }

    if (showDialog) {
        if (storedHerb.isImport) {
            ReadImportHerbDetailHistoryDialog(storedHerb = storedHerb, onDismiss = {
                showDialog = false
            })
        } else {
            ReadExportHerbDetailHistoryDialog(storedHerb = storedHerb, onDismiss = {
                showDialog = false
            })
        }
    }
}
