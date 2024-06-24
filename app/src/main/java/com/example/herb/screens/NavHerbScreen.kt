package com.example.herb.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.herb.R
import com.example.herb.database.entity.Herb
import com.example.herb.dialog.AddHerbDialog
import com.example.herb.event.HerbEvent
import com.example.herb.helper.StringHelper
import com.example.herb.state.HerbState

@Composable
fun HerbScreen(
    state: HerbState,
    onEvent: (HerbEvent) -> Unit,
    paddingValues: PaddingValues = PaddingValues()
) {
//    Log.d("HerbScreen", "Create")

    Scaffold(
        modifier = Modifier.padding(paddingValues),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(HerbEvent.ShowDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add herb"
                )
            }
        }
    ) { padding ->

        if (state.isAddingHerb) {
            AddHerbDialog(state = state, onEvent = onEvent)
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            NavHerbScreenHeader()

            Box(
                modifier = Modifier
                    .weight(1f, fill = true)
                    .fillMaxWidth()
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(state.herbs) { herb -> AddHerbRow(herb = herb, onEvent = onEvent) }
                }
            }

            NavHerbScreenFooter()
        }
    }

}

@Composable
fun AddHerbRow(
    herb: Herb,
    onEvent: (HerbEvent) -> Unit
) {
    var isExpand by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpand = !isExpand },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            colorResource(id = R.color.theme),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = herb.herbID.toString(),
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                )
                {
                    Text(
                        text = StringHelper.toProperCase(herb.herbName),
                        fontSize = 20.sp
                    )
                    if (!isExpand) {
                        Text(
                            text = StringHelper.numberToString(herb.avgPrice, "", "VND/g"),
                            fontSize = 20.sp,
                            color = Color.Yellow
                        )
                    }
                }
            }

            if (isExpand) {
                Row {
                    Text(
                        text = stringResource(id = R.string.avg_price) + " " +
                                StringHelper.numberToString(herb.avgPrice, "", "VND/g")
                    )
                }
                Row {
                    Text(
                        text = stringResource(id = R.string.weight_remain) + " " +
                                StringHelper.numberToFloorWeight(herb.totalWeight, "g")
                    )
                }
                Row {
                    Text(
                        text = stringResource(id = R.string.total_money) + " " +
                                StringHelper.numberToVND(herb.avgPrice * herb.totalWeight)
                    )
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
fun NavHerbScreenHeader() {
    Text(
        text = "Header",
        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun NavHerbScreenFooter() {
    // footer
    Row {
        Text(
            text = stringResource(id = R.string.title_herb),
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = stringResource(id = R.string.title_herb),
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}


@Composable
@Preview
fun PreviewHerbScreen() {
//    HerbScreen()
}