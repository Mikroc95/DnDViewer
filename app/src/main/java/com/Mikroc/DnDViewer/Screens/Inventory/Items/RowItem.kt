package com.Mikroc.DnDViewer.Screens.Inventory.Items

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
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Mikroc.DnDViewer.Components.InputCounter
import com.Mikroc.DnDViewer.Models.ItemsModel
import com.Mikroc.DnDViewer.Theme.blueMana
import com.Mikroc.DnDViewer.Theme.discordBlue
import com.Mikroc.DnDViewer.Theme.discordLigthBlack
import com.Mikroc.DnDViewer.Theme.discordOrangeAccent
import com.Mikroc.DnDViewer.Theme.textColor
import com.Mikroc.DnDViewer.R
import com.Mikroc.DnDViewer.Utils.stringToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RowItem(
    item: ItemsModel,
    saveObjectes: (ItemsModel) -> Unit,
    onEquipItem: (ItemsModel) -> Boolean,
    onDeleteClicked: () -> Unit,
    onEditClicked: (ItemsModel) -> Unit,
) {
    val context = LocalContext.current
    val isEquiped = remember {
        mutableStateOf(false)
    }
    isEquiped.value = item.isEquiped
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
        mutableIntStateOf(0)
    }
    charges.intValue = item.actualCharges.stringToInt()
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
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(horizontalArrangement = Arrangement.Start) {
                    Text(
                        text = item.name,
                        color = textColor(),
                        fontWeight = FontWeight.W600,
                        fontSize = 16.sp
                    )
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Image(
                        imageVector = Icons.Outlined.Edit,
                        colorFilter = ColorFilter.tint(textColor()),
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            onEditClicked(item)
                        }
                    )
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
            if (item.charges.stringToInt() > 0) {
                val counterText =
                    "${charges.intValue} / ${item.charges} ${context.getString(R.string.item_charges)}"
                val valueTextField = remember {
                    mutableStateOf("")
                }
                InputCounter(
                    totalResult = counterText,
                    borderColor = discordOrangeAccent,
                    valueTextField = valueTextField,
                    onKeyBoardDone = {
                        try {
                            if (valueTextField.value.isNotEmpty()) {
                                val value = valueTextField.value.toInt()
                                charges.intValue = if (item.actualCharges.toInt() + value > 0) {
                                    if (item.actualCharges.toInt() + value <= item.charges.toInt()) {
                                        item.actualCharges.toInt() + value
                                    } else {
                                        item.charges.toInt()
                                    }
                                } else {
                                    0
                                }
                                item.actualCharges = charges.intValue.toString()
                                saveObjectes(item)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            valueTextField.value = ""
                        }
                    },
                    onLessClicked = {
                        val charge = charges.intValue
                        if (charge - 1 >= 0) {
                            charges.intValue = charge - 1
                            item.actualCharges = charges.intValue.toString()
                            saveObjectes(item)
                        }
                    },
                    onPlusClicked = {
                        val charge = charges.intValue
                        if (charge + 1 <= item.charges.toInt()) {
                            charges.intValue = charge + 1
                            item.actualCharges = charges.intValue.toString()
                            saveObjectes(item)
                        }
                    }
                )
            }
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
fun RowConsumible(
    item: ItemsModel,
    onDeleteClicked: () -> Unit,
    onEditClicked: (ItemsModel) -> Unit,
    saveObjectes: (ItemsModel) -> Unit,
    onConsumeItem: (ItemsModel) -> Unit
) {
    val totalConsumibles = remember {
        mutableIntStateOf(0)
    }
    totalConsumibles.intValue = item.charges.stringToInt()
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .background(discordLigthBlack)
            .clip(RoundedCornerShape(3.dp))
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(horizontalArrangement = Arrangement.Start) {
                    Text(
                        text = item.name,
                        color = textColor(),
                        fontWeight = FontWeight.W600,
                        fontSize = 16.sp
                    )
                }
                Row(horizontalArrangement = Arrangement.End) {
                    Image(
                        imageVector = Icons.Outlined.Edit,
                        colorFilter = ColorFilter.tint(textColor()),
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            onEditClicked(item)
                        }
                    )
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
                totalResult = totalConsumibles.intValue.toString(),
                valueTextField = valueTextField,
                borderColor = discordOrangeAccent,
                onKeyBoardDone = {
                    try {
                        if (valueTextField.value.isNotEmpty()) {
                            val value = valueTextField.value.toInt()
                            if (totalConsumibles.intValue + value > 0) {
                                totalConsumibles.intValue += value
                                item.charges = totalConsumibles.intValue.toString()
                                item.actualCharges = item.charges
                                saveObjectes(item)
                            } else {
                                onConsumeItem(item)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        valueTextField.value = ""
                    }
                },
                onPlusClicked = {
                    val total = totalConsumibles.intValue
                    totalConsumibles.intValue = total + 1
                    item.charges = totalConsumibles.intValue.toString()
                    item.actualCharges = item.charges
                    saveObjectes(item)
                },
                onLessClicked = {
                    val total = totalConsumibles.intValue
                    if (total - 1 > 0) {

                        totalConsumibles.intValue = total - 1
                        item.charges = totalConsumibles.intValue.toString()
                        item.actualCharges = item.charges
                        saveObjectes(item)
                    } else {
                        onConsumeItem(item)
                    }
                }
            )

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


@Preview
@Composable
private fun RowItemPreview() {
    RowItem(
        item = ItemsModel(
            name = "MOCK",
            description = "mockmockmock",
            charges = "5",
            actualCharges = "5"
        ),
        saveObjectes = {},
        onEquipItem = { false },
        onDeleteClicked = { },
        onEditClicked = {},
    )
}

@Preview
@Composable
private fun RowConsumablePreview() {
    RowConsumible(
        item = ItemsModel(name = "MOCK", description = "mockmockmock", charges = "5"),
        saveObjectes = {},
        onDeleteClicked = { },
        onEditClicked = {},
        onConsumeItem = {}
    )
}