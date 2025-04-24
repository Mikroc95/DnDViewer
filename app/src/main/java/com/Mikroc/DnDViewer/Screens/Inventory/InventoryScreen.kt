package com.Mikroc.DnDViewer.Screens.Inventory

import android.content.Context
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.Mikroc.DnDViewer.Screens.viewModel
import com.Mikroc.DnDViewer.R
import com.Mikroc.DnDViewer.Theme.backgroundColor
import com.Mikroc.DnDViewer.Theme.discordBlue
import com.Mikroc.DnDViewer.Theme.discordLigthBlack
import com.Mikroc.DnDViewer.Theme.textColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(context: Context, characterModel: CharacterModel) {
    val dialogNewItem = remember {
        //0 = dialogClosed
        //1 = newItemNormal
        //2 = newConsumibleItem
        mutableIntStateOf(0)
    }
    val dialogDeleteItem = remember {
        mutableStateOf(ItemsModel())
    }
    val list = viewModel.getObjectes(characterModel.name)
    val listItems = remember {
        mutableStateOf(list.filter { !it.isConsumible })
    }
    val listConsumables = remember { mutableStateOf(list.filter { it.isConsumible }) }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val expandableHeight = if (characterModel.maxSpell > 0) {
        screenHeight / 3
    } else {
        screenHeight / 2
    }
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
                        saveObjectes = { viewModel.setObjectes(it) },
                        context = context,
                        onDeleteClicked = {
                          dialogDeleteItem.value = item
                        },
                        onEquipItem = { it ->
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
                            saveObjectes = { viewModel.setObjectes(it) },
                            onDeleteClicked = {
                                dialogDeleteItem.value = item
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
                    val listSpells = viewModel.getSpells(characterModel.name)
                    listSpells.forEach {
                        RowSpell(spell = it, count = listSpells.indexOf(it) + 1, context = context)
                    }
                }
            }
        }
        //OBSERVATIONS
        val observations = remember {
            mutableStateOf(characterModel.observations)
        }
        ExpandableBox(
            title = context.getString(R.string.iventory_observations),
            icon = painterResource(R.drawable.save),
            onIconClicked = {
                characterModel.observations = observations.value
                viewModel.updateCharacters(character = characterModel)
            }
        ) {
            CustomTextField(
                value = observations.value,
                onValueChange = { observations.value = it },
                modifier = Modifier
                    .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                    .fillMaxWidth(),
                singleLine = false
            )
        }
        //DIALOG NEW ITEM
        if (dialogNewItem.intValue == 1) {
            DialogNewItem(
                characterName = characterModel.name,
                onDismissRequest = { item ->
                    viewModel.setObjectes(item)
                    dialogNewItem.intValue = 0
                    listItems.value = viewModel.getObjectes(characterModel.name)
                        .filter { !it.isConsumible }
                },
                onClose = { dialogNewItem.intValue = 0 },
                isConsumable = false,
                context = context
            )
        } else if (dialogNewItem.intValue == 2) {
            DialogNewItem(
                characterName = characterModel.name,
                onDismissRequest = { item ->
                    viewModel.setObjectes(item)
                    dialogNewItem.intValue = 0
                    listConsumables.value = viewModel.getObjectes(characterModel.name)
                        .filter { it.isConsumible }
                },
                onClose = { dialogNewItem.intValue = 0 },
                isConsumable = true,
                context = context
            )
        }
        if(dialogDeleteItem.value.name.isNotEmpty()){
            BasicAlertDialog(onDismissRequest = { dialogDeleteItem.value = ItemsModel() }) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(discordLigthBlack),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = context.getString(R.string.inventory_delete_item_title),
                            modifier = Modifier.padding(start = 16.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(14f,TextUnitType.Sp),
                            color = textColor()
                        )
                        Text(
                            text = context.getString(R.string.common_delete_text),
                            modifier = Modifier.padding(top = 16.dp),
                            fontSize = TextUnit(12f,TextUnitType.Sp),
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
                                    itemName = dialogDeleteItem.value.name,
                                    characterName = characterModel.name
                                )
                                if(dialogDeleteItem.value.isConsumible){
                                    listConsumables.value = viewModel.getObjectes(characterModel.name)
                                        .filter { it.isConsumible}
                                }else{
                                    listItems.value = viewModel.getObjectes(characterModel.name)
                                        .filter { !it.isConsumible}
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