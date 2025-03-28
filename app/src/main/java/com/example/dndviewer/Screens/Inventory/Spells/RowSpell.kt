package com.example.dndviewer.Screens.Inventory.Spells

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.dndviewer.Components.CustomTextField
import com.example.dndviewer.Models.SpellModel
import com.example.dndviewer.Screens.viewModel
import com.example.dndviewer.Theme.discordBlue
import com.example.dndviewer.Theme.textColor
import com.example.dndviewer.R
import com.example.dndviewer.Screens.characterModel

@Composable
fun RowSpell(spell: SpellModel, count: Int, context: Context) {
    val valueName = remember {
        mutableStateOf(spell.name)
    }
    val valueLevel = remember {
        mutableStateOf(spell.level)
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
            Text(text = "$count. ",
                color = textColor(),
            )
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
                .padding(end = 8.dp)
        ) {
            CustomTextField(
                value = valueName.value,
                onValueChange = {
                    valueName.value = it
                    viewModel.updateSpells(
                        SpellModel(
                            id = spell.id,
                            name = valueName.value,
                            level = valueLevel.value,
                            character = characterModel.name
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(3.dp))
                    .border(2.dp, discordBlue),
                placeHolder = {
                    Text(
                        text = context.getString(R.string.row_spell_name),
                        color = textColor()
                    )
                }
            )
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .padding(start = 8.dp)
        ) {
            CustomTextField(
                value = valueLevel.value,
                onValueChange = {
                    valueLevel.value = it
                    viewModel.updateSpells(
                        SpellModel(
                            id = spell.id,
                            name = valueName.value,
                            level = valueLevel.value,
                            character = characterModel.name
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(3.dp))
                    .border(2.dp, discordBlue),
                placeHolder = {
                    Text(
                        text = context.getString(R.string.row_spell_level),
                        color = textColor()
                    )
                }
            )
        }
    }
}