package com.example.herb.screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.herb.R
import com.example.herb.activity.HerbDetailActivity
import com.example.herb.database.entity.Herb
import com.example.herb.dialog.AddHerbDialog
import com.example.herb.event.HerbEvent
import com.example.herb.helper.StringHelper
import com.example.herb.state.HerbState
import com.example.herb.util.HerbSortType
import com.example.herb.util.IntentExtraName
import java.util.Locale

@Composable
fun HerbScreen(
    state: HerbState,
    onEvent: (HerbEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(HerbEvent.ShowDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add herb"
                )
            }
        },
        topBar = { NavHerbScreenHeader(state = state, onEvent) },
        bottomBar = { NavHerbScreenFooter(state = state) }
    ) { padding ->

        if (state.isAddingHerb) {
            AddHerbDialog(state = state, onEvent = onEvent)
        }

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LocalContentColor.current.copy(alpha = 0.2f)),
            ) {
                items(state.herbs) { herb -> AddHerbRow(herb = herb, onEvent = onEvent) }
            }
        }

    }

}

@Composable
fun AddHerbRow(
    herb: Herb,
    onEvent: (HerbEvent) -> Unit
) {
    var isExpand by remember { mutableStateOf(false) }
    HorizontalDivider(thickness = 1.dp, color = Color.Gray)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpand = !isExpand }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            colorResource(id = R.color.theme_50),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_herb_24),
                        contentDescription = "Herb row icon",
                        modifier = Modifier.size(20.dp),
                        tint = colorResource(id = R.color.herb_color)
                    )
                }
                Text(
                    text = StringHelper.toProperCase(herb.herbName),
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )

                if (!isExpand) {
                    Text(
                        text = StringHelper.numberToString(herb.avgPrice, "", " VND/g"),
                        fontSize = 10.sp,
                        color = Color.Yellow
                    )

                }
            }

            if (isExpand) {

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
                        text = StringHelper.numberToCurrency (herb.avgPrice.toFloat() * herb.totalWeight, "") + " VND",
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Right,
                        fontWeight = FontWeight.Bold,
                        color = LocalContentColor.current.copy(alpha = 0.8f)
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                )
                {
                    val context = LocalContext.current
                    Button(onClick = {
                        val intent = Intent(context, HerbDetailActivity::class.java)
                        intent.putExtra(IntentExtraName.HERB_ID, herb.herbID)
                        intent.putExtra(IntentExtraName.HERB_NAME, herb.herbName)
                        intent.putExtra(IntentExtraName.HERB_WEIGHT, herb.totalWeight)
                        intent.putExtra(IntentExtraName.HERB_PRICE, herb.avgPrice)
                        context.startActivity(intent)
                    }) {
                        Text(text = stringResource(id = R.string.detail))
                    }
                }
            }

        }
        IconButton(onClick = {
            onEvent(HerbEvent.DeleteHerb(herb = herb))
        }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete herb")
        }

    }
}

@Composable
fun NavHerbScreenHeader(
    state: HerbState,
    onEvent: (HerbEvent) -> Unit
) {
    var isSearching by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.title_herb),
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),

                ) {

                Box(
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Button(onClick = { isSearching = !isSearching }) {
                        if (isSearching) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close search"
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Open search"
                            )
                        }
                    }
                }

                if (isSearching) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        value = state.querySort.stringQuery,
                        onValueChange = {
                            onEvent(
                                HerbEvent.SetHerbQuerySort(
                                    querySort = state.querySort.copy(
                                        stringQuery = it
                                    )
                                )
                            )
                        },
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.herb_name),
                                fontSize = 12.sp
                            )
                        },
                        shape = CircleShape,
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 12.sp,
                            lineHeight = 12.sp
                        ),
                    )
                }
            }


            val options = listOf(
                HerbSortType.ID,
                HerbSortType.NAME,
                HerbSortType.AVG_PRICE,
                HerbSortType.WEIGHT
            )
            val selectedText = mapOf(
                Pair(HerbSortType.ID, stringResource(id = R.string.sort_by)),
                Pair(HerbSortType.NAME, stringResource(id = R.string.title_herb)),
                Pair(HerbSortType.AVG_PRICE, stringResource(id = R.string.avg_price)),
                Pair(HerbSortType.WEIGHT, stringResource(id = R.string.weight_remain)),
            )
            var expanded by remember { mutableStateOf(false) }

            Log.d("NavHerbScreen", "NavHerbScreenHeader: ${state.querySort.herbSortType.name}")
            Log.d("NavHerbScreen", "NavHerbScreenHeader: ${selectedText.keys}")

            Box {
                Column {

                    OutlinedButton(onClick = { expanded = true }) {
                        selectedText[state.querySort.herbSortType]?.let { Text(text = it) }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { option ->

                            when (option) {
                                HerbSortType.ID -> {
                                    DropdownMenuItem(onClick = {
                                        expanded = false
                                        onEvent(
                                            HerbEvent.SetHerbQuerySort(
                                                querySort = state.querySort.copy(
                                                    herbSortType = HerbSortType.ID
                                                )
                                            )
                                        )
                                    },
                                        text = { Text(text = stringResource(id = R.string.none_type)) }
                                    )
                                }

                                HerbSortType.NAME -> {
                                    DropdownMenuItem(onClick = {
                                        expanded = false
                                        onEvent(
                                            HerbEvent.SetHerbQuerySort(
                                                querySort = state.querySort.copy(
                                                    herbSortType = HerbSortType.NAME
                                                )
                                            )
                                        )
                                    },
                                        text = { Text(text = stringResource(id = R.string.herb_name)) }
                                    )

                                }

                                HerbSortType.AVG_PRICE -> {
                                    DropdownMenuItem(onClick = {
                                        expanded = false
                                        onEvent(
                                            HerbEvent.SetHerbQuerySort(
                                                querySort = state.querySort.copy(
                                                    herbSortType = HerbSortType.AVG_PRICE
                                                )
                                            )
                                        )
                                    },
                                        text = { Text(stringResource(id = R.string.avg_price)) }
                                    )
                                }

                                HerbSortType.WEIGHT -> {
                                    DropdownMenuItem(onClick = {
                                        expanded = false
                                        onEvent(
                                            HerbEvent.SetHerbQuerySort(
                                                querySort = state.querySort.copy(
                                                    herbSortType = HerbSortType.WEIGHT
                                                )
                                            )
                                        )
                                    },
                                        text = { Text(stringResource(id = R.string.weight_remain)) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }

    }
}

@Composable
fun NavHerbScreenFooter(state: HerbState) {
    // footer
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalContentColor.current.copy(alpha = 0.1f)),
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                text = stringResource(id = R.string.total_money_of)
                        + " ${state.herbs.count()} " + stringResource(id = R.string.title_herb) + "(s)",
                fontSize = 15.sp,
            )
            var totalMoney = 0f
            state.herbs.forEach {
                totalMoney += it.avgPrice * it.totalWeight
            }
            Text(
                text = StringHelper.numberToString(totalMoney, "", " VND"),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = LocalContentColor.current.copy(alpha = 0.8f, blue = 0.8f),
                textAlign = TextAlign.Right
            )
        }
    }

}
