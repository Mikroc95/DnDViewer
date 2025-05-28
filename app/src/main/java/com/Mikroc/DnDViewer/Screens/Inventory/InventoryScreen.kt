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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.Mikroc.DnDViewer.bbdd.Repository.Database.FakeCharacterRepository
import com.Mikroc.DnDViewer.bbdd.Repository.Database.FakeItemsRepository
import com.Mikroc.DnDViewer.bbdd.Repository.Database.FakeSpellRepository
import com.Mikroc.DnDViewer.Components.CustomTextField
import com.Mikroc.DnDViewer.Components.ExpandableBox
import com.Mikroc.DnDViewer.Dialogs.DialogNewItem
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val currentUpdateCharacterJob = remember { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()
    val characterModel by viewModel.selectedCharacter.collectAsState()
    var localObservationsText by remember { mutableStateOf("") }
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
    val listItems by viewModel.itemsFlowList.collectAsState()
    val listConsumables by viewModel.consumableFlowList.collectAsState()
    val listSpells by viewModel.spellsFlowList.collectAsState()

    LaunchedEffect(characterModel) {
        if (characterModel.observations != localObservationsText) {
            localObservationsText = characterModel.observations
        }
    }

    LaunchedEffect(localObservationsText, characterModel) {
        currentUpdateCharacterJob.value?.cancel()
        if (characterModel.observations != localObservationsText) {
            currentUpdateCharacterJob.value = scope.launch {
                delay(1000L)
                val character = viewModel.selectedCharacter.value
                character.let { update ->
                    if (update.observations != localObservationsText) { // Doble check
                        val updatedCharacter = update.copy(observations = localObservationsText)
                        viewModel.updateCharacters(updatedCharacter)
                    }
                }
            }
        }
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(configuration.screenHeightDp.dp)
            ) {
                itemsIndexed(
                    items = listItems,
                    key = { _, item -> item.id!! }
                ) { _, item ->
                    RowItem(
                        item = item,
                        saveObjectes = { viewModel.updateItems(it) },

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
                                    viewModel.updateItems(it)
                                } else {
                                    if (listItems.filter { it.isEquiped }.size < 3) {
                                        it.isEquiped = true
                                        viewModel.updateItems(it)
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(configuration.screenHeightDp.dp)
            ) {
                itemsIndexed(
                    items = listConsumables,
                    key = { _, item -> item.id!! }
                ) { _, item ->
                    RowConsumible(
                        item = item,
                        saveObjectes = { viewModel.updateItems(it) },
                        onDeleteClicked = {
                            dialogDeleteItem.value = item
                        },
                        onEditClicked = {
                            dialogEditItem.value = it
                        },
                        onConsumeItem = {
                            viewModel.deleteItem(it)
                        }
                    )
                }
            }
        }

        //ENCANTERIS
        if (characterModel.maxSpell > 0) {
            ExpandableBox(title = context.getString(R.string.inventory_spells)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(configuration.screenHeightDp.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    itemsIndexed(
                        items = listSpells,
                        key = { _, spell -> spell.id!! }
                    ) { index, spell ->
                        RowSpell(
                            spell = spell,
                            count = index + 1,
                            onUpdateSpell = viewModel::updateSpell
                        )
                    }
                }
            }
        }
        ExpandableBox(title = context.getString(R.string.iventory_observations)) {
            CustomTextField(
                value = localObservationsText,
                onValueChange = {
                    localObservationsText = it
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
                    viewModel.insertItems(item)
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
                    viewModel.updateItems(item)
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
                                viewModel.deleteItem(
                                    itemsModel = dialogDeleteItem.value,
                                )
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
        viewModel = MainViewModel(
            itemRepository = FakeItemsRepository(),
            characterRepository = FakeCharacterRepository(),
            spellRepository = FakeSpellRepository()
        )
    )
}