package com.Mikroc.DnDViewer.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.asIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.Mikroc.DnDViewer.Components.CustomHpManaBar
import com.Mikroc.DnDViewer.Models.CharacterModel
import com.Mikroc.DnDViewer.Screens.Character.CharacterScreen
import com.Mikroc.DnDViewer.Screens.Inventory.InventoryScreen
import com.Mikroc.DnDViewer.Theme.backgroundColor
import com.Mikroc.DnDViewer.Theme.discordBlue
import com.Mikroc.DnDViewer.Theme.textColor
import com.Mikroc.DnDViewer.Theme.textColorAccent
import com.Mikroc.DnDViewer.Theme.topBarColor
import com.Mikroc.DnDViewer.ViewModels.MainViewModel
import com.Mikroc.DnDViewer.R
import com.Mikroc.DnDViewer.Screens.HomeBrew.HomeBrewViewer

import java.io.File


@Composable
fun MainScreen(characterSelected: CharacterModel, viewModel: MainViewModel) {
    val tabSelected = viewModel.selectedTabIndex.collectAsState()
    if (characterSelected.name.isEmpty()) {
        EmptySelection()
    } else {
        Column(modifier = Modifier.background(topBarColor())) {
            if (characterSelected.vidaMax > 0 || characterSelected.manaMax > 0 || characterSelected.metaMagiaMax > 0) {
                CustomHpManaBar(viewModel = viewModel)
            }
            when (isCharacterEmpty(character = characterSelected)) {
                0 -> {
                    TabRowFull(
                        characterSelected = characterSelected,
                        viewModel = viewModel,
                        tabSelected = tabSelected.asIntState()
                    )
                }

                1 -> {
                    TabRowCharacter(
                        characterSelected = characterSelected,
                        viewModel = viewModel,
                        tabSelected = tabSelected.asIntState()
                    )
                }

                2 -> {
                    TabRowHomeBrew(
                        characterSelected = characterSelected,
                        viewModel = viewModel,
                        tabSelected = tabSelected.asIntState()
                    )
                }

                3 -> {
                    InventoryScreen(viewModel = viewModel)
                }
            }
        }

    }
}

//0-->>show Personatge + homebrew + inventari
//1-->show Personatge + inventari
//2-->show homebrew + inventari
//3 --> show inventari
fun isCharacterEmpty(character: CharacterModel): Int {
    return if (
        character.homebrewRoute.isNotEmpty() &&
        !character.imageCharacter.contentEquals(byteArrayOf())
    ) {
        0
    } else {
        if (character.homebrewRoute.isEmpty() && !character.imageCharacter.contentEquals(byteArrayOf())) {
            1
        } else if (character.imageCharacter.contentEquals(byteArrayOf()) && character.homebrewRoute.isNotEmpty()) {
            2
        } else {
            3
        }
    }
}

@Composable
private fun TabRowFull(
    characterSelected: CharacterModel,
    viewModel: MainViewModel,
    tabSelected: State<Int>
) {
    val context = LocalContext.current
    TabRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(0.dp)),
        selectedTabIndex = tabSelected.value,
        containerColor = topBarColor(),

        indicator = {

            TabRowDefaults.SecondaryIndicator(
                Modifier.tabIndicatorOffset(it[tabSelected.value]),
                color = discordBlue
            )
        }
    ) {

        Tab(
            modifier = Modifier.padding(8.dp),
            selected = tabSelected.value == 0,
            onClick = { viewModel.setSelectedTabIndex(0) }
        ) {
            val color = if (tabSelected.value == 0) {
                textColorAccent()
            } else {
                textColor()
            }
            Text(text = context.getString(R.string.tab_personatge), color = color)
        }

        Tab(
            modifier = Modifier.padding(8.dp),
            selected = tabSelected.value == 1,
            onClick = { viewModel.setSelectedTabIndex(1) }
        ) {
            val color = if (tabSelected.value == 1) {
                textColorAccent()
            } else {
                textColor()
            }
            Text(text = context.getString(R.string.tab_homebrew), color = color)
        }

        Tab(
            modifier = Modifier.padding(8.dp),
            selected = tabSelected.value == 2,
            onClick = { viewModel.setSelectedTabIndex(2) }
        ) {
            val color = if (tabSelected.value == 2) {
                textColorAccent()
            } else {
                textColor()
            }
            Text(text = context.getString(R.string.tab_inventari), color = color)
        }
    }

    Column(
        modifier = Modifier
            .background(backgroundColor())
            .fillMaxSize(),

        ) {
        when (tabSelected.value) {
            0 -> {
                CharacterScreen(characterModel = characterSelected)
            }

            1 -> {
                HomeBrewViewer(File(characterSelected.homebrewRoute))
            }

            2 -> {
                InventoryScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun TabRowCharacter(
    characterSelected: CharacterModel,
    viewModel: MainViewModel,
    tabSelected: State<Int>
) {
    val context = LocalContext.current
    TabRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(0.dp)),
        selectedTabIndex = tabSelected.value,
        containerColor = topBarColor(),
        indicator = {

            TabRowDefaults.SecondaryIndicator(
                Modifier.tabIndicatorOffset(it[tabSelected.value]),
                color = discordBlue
            )
        }
    ) {

        Tab(
            modifier = Modifier.padding(8.dp),
            selected = tabSelected.value == 0,
            onClick = { viewModel.setSelectedTabIndex(0) }
        ) {
            val color = if (tabSelected.value == 0) {
                textColorAccent()
            } else {
                textColor()
            }
            Text(text = context.getString(R.string.tab_personatge), color = color)
        }

        Tab(
            modifier = Modifier.padding(8.dp),
            selected = tabSelected.value == 1,
            onClick = { viewModel.setSelectedTabIndex(1) }
        ) {
            val color = if (tabSelected.value == 1) {
                textColorAccent()
            } else {
                textColor()
            }
            Text(text = context.getString(R.string.tab_inventari), color = color)
        }
    }

    Column(
        modifier = Modifier
            .background(backgroundColor())
            .fillMaxSize()
    ) {
        when (tabSelected.value) {
            0 -> {
                CharacterScreen(characterModel = characterSelected)
            }

            1 -> {
                InventoryScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun TabRowHomeBrew(
    characterSelected: CharacterModel,
    viewModel: MainViewModel,
    tabSelected: State<Int>
) {
    val context = LocalContext.current
    TabRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(0.dp)),
        selectedTabIndex = tabSelected.value,
        containerColor = topBarColor(),
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                Modifier.tabIndicatorOffset(it[tabSelected.value]),
                color = discordBlue
            )
        }
    ) {
        Tab(
            modifier = Modifier.padding(8.dp),
            selected = tabSelected.value == 0,
            onClick = { viewModel.setSelectedTabIndex(0) }
        ) {
            val color = if (tabSelected.value == 0) {
                textColorAccent()
            } else {
                textColor()
            }
            Text(text = context.getString(R.string.tab_homebrew), color = color)
        }

        Tab(
            modifier = Modifier.padding(8.dp),
            selected = tabSelected.value == 1,
            onClick = { viewModel.setSelectedTabIndex(1) }
        ) {
            val color = if (tabSelected.value == 1) {
                textColorAccent()
            } else {
                textColor()
            }
            Text(text = context.getString(R.string.tab_inventari), color = color)
        }
    }

    Column(
        modifier = Modifier
            .background(backgroundColor())
            .fillMaxSize(),

        ) {
        when (tabSelected.value) {
            0 -> {
                HomeBrewViewer(File(characterSelected.homebrewRoute))
            }

            1 -> {
                InventoryScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun EmptySelection() {
    Image(
        painter = painterResource(R.drawable.empty),
        contentDescription = "",
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 8.dp)
    )
}
