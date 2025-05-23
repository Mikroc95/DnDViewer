package com.Mikroc.DnDViewer.Screens.Inventory

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.Mikroc.DnDViewer.Components.CustomTextField
import com.Mikroc.DnDViewer.Components.ExpandableBox
import com.Mikroc.DnDViewer.Dialogs.DialogNewItem
import com.Mikroc.DnDViewer.Models.CharacterModel
import com.Mikroc.DnDViewer.Models.ItemsModel
import com.Mikroc.DnDViewer.Screens.Inventory.Items.RowConsumible
import com.Mikroc.DnDViewer.Screens.Inventory.Items.RowItem
import com.Mikroc.DnDViewer.Screens.Inventory.Spells.RowSpell
import com.Mikroc.DnDViewer.R
import com.Mikroc.DnDViewer.Theme.backgroundColor
import com.Mikroc.DnDViewer.Theme.discordBlue
import com.Mikroc.DnDViewer.Theme.discordLigthBlack
import com.Mikroc.DnDViewer.Theme.textColor
import com.Mikroc.DnDViewer.ViewModels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(characterModel: CharacterModel, viewModel: MainViewModel) {
    val context = LocalContext.current
    val dialogNewItem = remember {
        //0 = dialogClosed
        //1 = newItemNormal
        //2 = newConsumibleItem
        mutableIntStateOf(0)
    }
    val dialogDeleteItem = remember {
        mutableStateOf(ItemsModel())
    }
    val dialogEditItem = remember {
        mutableStateOf(ItemsModel())
    }
    val list = viewModel.getObjectes(characterModel.code)
    val listItems = remember {
        mutableStateOf(list.filter { !it.isConsumible })
    }
    val listConsumables = remember { mutableStateOf(list.filter { it.isConsumible }) }
    val configuration = LocalConfiguration.current
    val expandableHeight = configuration.screenHeightDp.dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(backgroundColor())
    ) {

        //ITEMS
        ExpandableBox(
            title = context.getString(R.string.inventory_bag),
            icon = painterResource(R.drawable.add),
            onIconClicked = {
                dialogNewItem.intValue = 1
            }
        ) {
            Column(
                modifier = Modifier
                    .height(expandableHeight)
                    .verticalScroll(rememberScrollState())
            ) {
                listItems.value.forEach { item ->
                    RowItem(
                        item = item,
                        saveObjectes = { viewModel.updateObjectes(it) },

                        onDeleteClicked = {
                            dialogDeleteItem.value = item
                        },
                        onEditClicked = {
                            dialogEditItem.value = it
                        },
                        onEquipItem = { it ->
                            try {
                                if (it.isEquiped) {
                                    it.isEquiped = false
                                    viewModel.updateObjectes(it)
                                } else {
                                    if (listItems.value.filter { it.isEquiped }.size < 3) {
                                        it.isEquiped = true
                                        viewModel.updateObjectes(it)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.item_max_items_equiped),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                return@RowItem it.isEquiped
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            return@RowItem false
                        }
                    )
                }
            }
        }

        ExpandableBox(
            title = context.getString(R.string.inventory_consumables),
            icon = painterResource(R.drawable.add),
            onIconClicked = {
                dialogNewItem.intValue = 2
            }
        ) {
            Column(
                modifier = Modifier
                    .height(expandableHeight)
                    .verticalScroll(rememberScrollState())
            ) {
                Column {
                    listConsumables.value.forEach { item ->
                        RowConsumible(
                            item = item,
                            saveObjectes = { viewModel.updateObjectes(it) },
                            onDeleteClicked = {
                                dialogDeleteItem.value = item
                            },
                            onEditClicked = {
                                dialogEditItem.value = it
                            },
                            onConsumeItem = {
                                viewModel.deleteObjecte(it.id)
                                listConsumables.value = viewModel.getObjectes(characterModel.code)
                                    .filter { it.isConsumible }
                            }
                        )
                    }
                }
            }
        }

        //ENCANTERIS
        if (characterModel.maxSpell > 0) {
            ExpandableBox(title = context.getString(R.string.inventory_spells)) {
                Column(
                    modifier = Modifier
                        .height(expandableHeight)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    val listSpells = viewModel.getSpells(characterModel.code)
                    listSpells.forEach {
                        RowSpell(
                            spell = it,
                            count = listSpells.indexOf(it) + 1,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
        //OBSERVATIONS
        val observations = remember {
            mutableStateOf(characterModel.observations)
        }
        ExpandableBox(title = context.getString(R.string.iventory_observations)) {
            CustomTextField(
                value = observations.value,
                onValueChange = {
                    observations.value = it
                    characterModel.observations = observations.value
                    viewModel.updateCharacters(character = characterModel)
                },
                modifier = Modifier
                    .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                    .fillMaxWidth(),
                singleLine = false
            )
        }
        //DIALOG NEW ITEM
        if (dialogNewItem.intValue == 1 || dialogNewItem.intValue == 2) {
            DialogNewItem(
                characterCode = characterModel.code,
                onDismissRequest = { item ->
                    viewModel.insertObjectes(item)
                    if (dialogNewItem.intValue == 1) {
                        listItems.value = viewModel.getObjectes(characterModel.code)
                            .filter { !it.isConsumible }
                    } else {
                        listConsumables.value = viewModel.getObjectes(characterModel.code)
                            .filter { it.isConsumible }
                    }
                    dialogNewItem.intValue = 0
                    dialogEditItem.value = ItemsModel()
                },
                onClose = { dialogNewItem.intValue = 0 },
                isConsumable = dialogNewItem.intValue == 2,
            )
        }
        if (dialogEditItem.value.name.isNotEmpty()) {
            DialogNewItem(
                characterCode = characterModel.code,
                onDismissRequest = { item ->
                    viewModel.updateObjectes(item)
                    if (item.isConsumible) {
                        listConsumables.value = viewModel.getObjectes(characterModel.code)
                            .filter { it.isConsumible }
                    } else {
                        listItems.value = viewModel.getObjectes(characterModel.code)
                            .filter { !it.isConsumible }
                    }
                    dialogEditItem.value = ItemsModel()
                    dialogNewItem.intValue = 0
                },
                onClose = {
                    dialogEditItem.value = ItemsModel()
                    dialogNewItem.intValue = 0
                },
                isConsumable = dialogEditItem.value.isConsumible,
                editing = dialogEditItem.value,
            )
        }
        if (dialogDeleteItem.value.name.isNotEmpty()) {
            BasicAlertDialog(onDismissRequest = { dialogDeleteItem.value = ItemsModel() }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(discordLigthBlack),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = context.getString(R.string.inventory_delete_item_title),
                            modifier = Modifier.padding(start = 16.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(14f, TextUnitType.Sp),
                            color = textColor()
                        )
                        Text(
                            text = context.getString(R.string.common_delete_text),
                            modifier = Modifier.padding(top = 16.dp),
                            fontSize = TextUnit(12f, TextUnitType.Sp),
                            color = textColor()
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                viewModel.deleteObjecte(
                                    id = dialogDeleteItem.value.id,
                                )
                                if (dialogDeleteItem.value.isConsumible) {
                                    listConsumables.value =
                                        viewModel.getObjectes(characterModel.code)
                                            .filter { it.isConsumible }
                                } else {
                                    listItems.value = viewModel.getObjectes(characterModel.code)
                                        .filter { !it.isConsumible }
                                }
                                dialogDeleteItem.value = ItemsModel()
                            },
                            modifier = Modifier
                                .weight(0.3f)
                                .padding(horizontal = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = discordBlue)
                        ) {
                            Text(
                                text = context.getString(R.string.common_yes),
                                color = textColor(),
                                fontSize = TextUnit(16f, TextUnitType.Sp)
                            )
                        }

                        Button(
                            onClick = { dialogDeleteItem.value = ItemsModel() },
                            modifier = Modifier.weight(0.3f),
                            colors = ButtonDefaults.buttonColors(containerColor = discordBlue)
                        ) {
                            Text(
                                text = context.getString(R.string.common_no),
                                color = textColor(),
                                fontSize = TextUnit(16f, TextUnitType.Sp)
                            )
                        }
                    }
                }

            }
        }
    }
}

@Preview
@Composable
private fun InventoryScreenPreview() {
    InventoryScreen(
        characterModel = CharacterModel(),
        viewModel = MainViewModel()
    )
}