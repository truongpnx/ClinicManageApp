package com.example.herb.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.herb.R
import com.example.herb.event.HerbEvent
import com.example.herb.state.HerbState

@Composable
fun AddHerbDialog(
    state: HerbState,
    onEvent: (HerbEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(HerbEvent.HideDialog)
        },
        title = { Text(text = stringResource(id = R.string.add_herb_title)) },
        text = {
            TextField(
                value = state.herbName,
                onValueChange = {
                    onEvent(HerbEvent.SetHerbName(it))
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.herb_name))
                }
            )
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            )
            {
                Button(onClick = {
                    onEvent(HerbEvent.SaveHerb)
                }) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        },
    )
}