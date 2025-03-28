package com.example.dndviewer.Screens.Inventory

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.dndviewer.Components.CustomTextField
import com.example.dndviewer.Components.ExpandableBox
import com.example.dndviewer.Dialogs.DialogNewItem
import com.example.dndviewer.Models.CharacterModel
import com.example.dndviewer.Screens.Inventory.Items.RowConsumible
import com.example.dndviewer.Screens.Inventory.Items.RowItem
import com.example.dndviewer.Screens.Inventory.Spells.RowSpell
import com.example.dndviewer.Screens.viewModel
import com.example.dndviewer.R

@Composable
fun InventoryScreen(context: Context, characterModel: CharacterModel) {
    val dialogNewItem = remember {
        //0 = dialogClosed
        //1 = newItemNormal
        //2 = newConsumibleItem
        mutableIntStateOf(0)
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
                        viewModel.deleteObjecte(
                            itemName = item.name,
                            characterName = characterModel.name
                        )
                        listItems.value = viewModel.getObjectes(characterModel.name)
                            .filter { !it.isConsumible }
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
                            viewModel.deleteObjecte(
                                itemName = item.name,
                                characterName = characterModel.name
                            )
                            listConsumables.value = viewModel.getObjectes(characterModel.name)
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
}