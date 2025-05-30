package com.mikroc.dndviewer.screens

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mikroc.dndviewer.screens.homebrew.HomeBrewViewer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.mikroc.dndviewer.R
import com.mikroc.dndviewer.components.CustomHpManaBar
import com.mikroc.dndviewer.models.CharacterModel
import com.mikroc.dndviewer.screens.character.CharacterScreen
import com.mikroc.dndviewer.screens.inventory.InventoryScreen
import com.mikroc.dndviewer.theme.backgroundColor
import com.mikroc.dndviewer.theme.discordBlue
import com.mikroc.dndviewer.theme.textColor
import com.mikroc.dndviewer.theme.textColorAccent
import com.mikroc.dndviewer.theme.topBarColor
import com.mikroc.dndviewer.viewmodels.MainViewModel
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(characterSelected: CharacterModel, viewModel: MainViewModel) {
    val tabSelected by viewModel.selectedTabIndex.collectAsStateWithLifecycle()
    val context = LocalContext.current

    if (characterSelected.name.isEmpty()) {
        EmptySelection()
    } else {
        Column(
            modifier = Modifier
                .background(topBarColor())
                .fillMaxSize()
        ) {
            if (characterSelected.vidaMax > 0 ||
                characterSelected.manaMax > 0 ||
                characterSelected.metaMagicMax > 0
            ) {
                CustomHpManaBar(viewModel = viewModel)
            }
            val characterStatus = isCharacterEmpty(character = characterSelected)
            val tabsInfo = remember(characterStatus, context) {
                when (characterStatus) {
                    0 -> listOf(
                        context.getString(R.string.tab_personatge),
                        context.getString(R.string.tab_homebrew),
                        context.getString(R.string.tab_inventari)
                    )

                    1 -> listOf(
                        context.getString(R.string.tab_personatge),
                        context.getString(R.string.tab_inventari)
                    )

                    2 -> listOf(
                        context.getString(R.string.tab_homebrew),
                        context.getString(R.string.tab_inventari)
                    )

                    else -> emptyList<String>()
                }
            }
            LaunchedEffect(tabsInfo.size) {
                if ((tabSelected >= tabsInfo.size && tabsInfo.isNotEmpty()) ||
                    (tabsInfo.isEmpty() && tabSelected != 0)
                ) {
                    viewModel.setSelectedTabIndex(0)
                }
            }
            if (tabsInfo.isNotEmpty() && characterStatus != 3) {
                TabRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(0.dp)),
                    selectedTabIndex = tabSelected.coerceIn(
                        0,
                        (tabsInfo.size - 1).coerceAtLeast(0)
                    ),
                    containerColor = topBarColor(),
                    indicator = { tabPositions ->
                        if (tabSelected < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[tabSelected]),
                                color = discordBlue
                            )
                        }
                    }
                ) {
                    tabsInfo.forEachIndexed { index, title ->
                        Tab(
                            modifier = Modifier.padding(8.dp),
                            selected = tabSelected == index,
                            onClick = { viewModel.setSelectedTabIndex(index) }
                        ) {
                            val color = if (tabSelected == index) {
                                textColorAccent()
                            } else {
                                textColor()
                            }
                            Text(text = title, color = color)
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(backgroundColor())
            ) {
                val maxTabIndex = tabsInfo.size - 1
                val safeTabIndex =
                    tabSelected.coerceIn(0, maxTabIndex.coerceAtLeast(0))

                when (characterStatus) {
                    0 -> {
                        // Personatge + Homebrew + Inventari
                        when (safeTabIndex) {
                            0 -> CharacterScreen(characterModel = characterSelected)
                            1 -> HomeBrewViewer(File(characterSelected.homebrewRoute))
                            2 -> InventoryScreen(viewModel = viewModel)
                        }
                    }

                    1 -> {
                        // Personatge + Inventari
                        when (safeTabIndex) {
                            0 -> CharacterScreen(characterModel = characterSelected)
                            1 -> InventoryScreen(viewModel = viewModel)
                        }
                    }

                    2 -> {
                        // Homebrew + Inventari
                        when (safeTabIndex) {
                            0 -> HomeBrewViewer(File(characterSelected.homebrewRoute))
                            1 -> InventoryScreen(viewModel = viewModel)
                        }
                    }

                    3 -> {
                        //Inventari
                        InventoryScreen(viewModel = viewModel)
                    }
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
fun EmptySelection() {
    Image(
        painter = painterResource(R.drawable.empty),
        contentDescription = "",
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 8.dp)
    )
}
