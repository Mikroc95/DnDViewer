package com.example.dndviewer.Screens.Inventory.Items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dndviewer.Components.InputCounter
import com.example.dndviewer.Models.ItemsModel
import com.example.dndviewer.Screens.context
import com.example.dndviewer.Theme.blueMana
import com.example.dndviewer.Theme.discordBlue
import com.example.dndviewer.Theme.discordLigthBlack
import com.example.dndviewer.Theme.discordOrangeAccent
import com.example.dndviewer.Theme.textColor
import com.example.dndviewer.ViewModels.MainViewModel
import com.example.dndviewer.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RowItem(item: ItemsModel, viewModel: MainViewModel, onEquipItem: (ItemsModel) -> Boolean,onDeleteClicked:()->Unit) {
    val isEquiped = remember {
        mutableStateOf(item.isEquiped)
    }
    val rowModifier = if (isEquiped.value) {
        Modifier
            .background(discordBlue)
            .clip(RoundedCornerShape(3.dp))
            .border(2.dp, blueMana)

    } else {
        Modifier
            .background(discordLigthBlack)
            .clip(RoundedCornerShape(3.dp))

    }
    val charges = remember {
        mutableStateOf(item.actualCharges.toInt().or(0))
    }
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top,
        modifier = rowModifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = { },
                onLongClick = {
                    isEquiped.value = onEquipItem(item)
                }
            )

    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()) {
                Row(horizontalArrangement = Arrangement.Start){
                    Text(
                        text = item.name,
                        color = textColor(),
                        fontWeight = FontWeight.W600,
                        fontSize = 16.sp
                    )
                }
               Row(horizontalArrangement = Arrangement.End) {
                   Image(
                       imageVector = Icons.Outlined.Delete,
                       colorFilter = ColorFilter.tint(textColor()),
                       contentDescription = "",
                       modifier = Modifier.clickable {onDeleteClicked()
                       }
                   )
               }
            }

            val counterText =
                "${charges.value} / ${item.charges} ${context.getString(R.string.item_charges)}"
            val valueTextField = remember {
                mutableStateOf("")
            }
            InputCounter(
                totalResult = counterText,
                borderColor = discordOrangeAccent,
                valueTextField = valueTextField,
                onKeyBoardDone = {
                    if (valueTextField.value.toInt().or(-10000) != -10000) {
                        val value = valueTextField.value.toInt()
                        charges.value =  if (item.actualCharges.toInt() + value > 0) {
                             if(item.actualCharges.toInt() + value <= item.charges.toInt()){
                               item.actualCharges.toInt() + value
                            }else{
                               item.charges.toInt()
                            }
                        }else{
                           0
                        }
                        item.actualCharges =  charges.value.toString()
                        viewModel.setObjectes(item)
                    }
                },
                onLessClicked = {
                    if (charges.value - 1 >= 0) {
                        charges.value--
                        item.actualCharges = charges.value.toString()
                        viewModel.updateObjectes(item)
                    }
                },
                onPlusClicked = {
                    if (charges.value + 1 <= item.charges.toInt()) {
                        charges.value++
                        item.actualCharges = charges.value.toString()
                        viewModel.updateObjectes(item)
                    }
                })
            Text(
                text = item.description,
                color = textColor()
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                color = textColor()
            )
        }
    }
}

@Composable
fun RowConsumible(item: ItemsModel,onDeleteClicked:()->Unit, viewModel: MainViewModel) {
    val totalConsumibles = remember {
        mutableStateOf(item.charges.toInt())
    }
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .background(discordLigthBlack)
            .clip(RoundedCornerShape(3.dp))
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val titleText = "${item.name} X ${totalConsumibles.value}"
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()) {
                Row(horizontalArrangement = Arrangement.Start){
                    Text(
                        text = item.name,
                        color = textColor(),
                        fontWeight = FontWeight.W600,
                        fontSize = 16.sp
                    )
                }
                Row(horizontalArrangement = Arrangement.End) {
                    Image(
                        imageVector = Icons.Outlined.Delete,
                        colorFilter = ColorFilter.tint(textColor()),
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            onDeleteClicked()
                        }
                    )
                }
            }
            val valueTextField = remember {
                mutableStateOf("")
            }
            InputCounter(
                totalResult = totalConsumibles.value.toString(),
                valueTextField = valueTextField,
                borderColor = discordOrangeAccent,
                onKeyBoardDone = {
                    if (valueTextField.value.toInt().or(-10000) != -10000) {
                        val value = valueTextField.value.toInt()
                        if (totalConsumibles.value + value > 0) {
                            totalConsumibles.value += value
                        }else{
                            totalConsumibles.value = 0
                        }
                        item.charges = totalConsumibles.value.toString()
                        item.actualCharges = item.charges
                        viewModel.setObjectes(item)
                    }
                },
                onPlusClicked = {
                    totalConsumibles.value++
                    item.charges = totalConsumibles.value.toString()
                    item.actualCharges = item.charges
                    viewModel.setObjectes(item)
                },
                onLessClicked = {
                    if (totalConsumibles.value - 1 >= 0) {
                        totalConsumibles.value--
                        item.charges = totalConsumibles.value.toString()
                        item.actualCharges = item.charges
                        viewModel.setObjectes(item)
                    }else{
                        totalConsumibles.value = 0
                        item.charges = totalConsumibles.value.toString()
                        item.actualCharges = item.charges
                        viewModel.setObjectes(item)
                    }
                })
            Text(
                text = item.description,
                color = textColor()
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                color = textColor()
            )
        }
    }
}