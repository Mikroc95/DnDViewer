package com.mikroc.dndviewer.screens.inventory.spells

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mikroc.dndviewer.components.CustomTextField
import com.mikroc.dndviewer.models.SpellModel
import com.mikroc.dndviewer.theme.discordBlue
import com.mikroc.dndviewer.theme.textColor
import com.mikroc.dndviewer.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RowSpell(
    spell: SpellModel,
    count: Int,
    onUpdateSpell: (SpellModel) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var localName by remember(spell.id) { mutableStateOf(spell.name) }
    var localLevel by remember(spell.id) { mutableStateOf(spell.level) }
    LaunchedEffect(spell.name) {
        if (localName != spell.name) {
            localName = spell.name
        }
    }
    LaunchedEffect(spell.level) {
        if (localLevel != spell.level) {
            localLevel = spell.level
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentWidth()
                .weight(0.07f)
        ) {
            Text(
                text = "$count. ",
                color = textColor(),
            )
        }
        val currentNameJob = remember { mutableStateOf<Job?>(null) }
        val currentLevelJob = remember { mutableStateOf<Job?>(null) }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
                .padding(end = 8.dp)
        ) {
            CustomTextField(
                value = localName,
                onValueChange = {
                    localName = it
                    currentNameJob.value?.cancel()
                    currentNameJob.value = scope.launch {
                        delay(1000L)
                        onUpdateSpell(spell.copy(name = localName, level = localLevel))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(3.dp))
                    .border(2.dp, discordBlue),
                placeHolder = {
                    Text(
                        text = context.getString(R.string.row_spell_name), color = textColor()
                    )
                })
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .padding(start = 8.dp)
        ) {
            CustomTextField(
                value = localLevel,
                onValueChange = {
                    localLevel = it
                    currentLevelJob.value?.cancel()
                    currentLevelJob.value = scope.launch {
                        delay(1000L)
                        onUpdateSpell(spell.copy(name = localName, level = localLevel))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(3.dp))
                    .border(2.dp, discordBlue),
                placeHolder = {
                    Text(
                        text = context.getString(R.string.row_spell_level), color = textColor()
                    )
                })
        }
    }
}