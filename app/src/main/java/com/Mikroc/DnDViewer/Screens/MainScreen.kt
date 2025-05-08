package com.Mikroc.DnDViewer.Screens

import android.net.Uri
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
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
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.VerticalPdfReaderState
import java.io.File

var tabSelected: MutableState<Int> = mutableIntStateOf(0)

var characterModel = CharacterModel(imageCharacter = byteArrayOf())

private var pdfVerticalReaderState = VerticalPdfReaderState(
    resource = ResourceType.Local(Uri.fromFile(File(characterModel.homebrewRoute))),
    isZoomEnable = true
)
@Composable
fun MainScreen(characterSelected: CharacterModel, viewModel: MainViewModel) {
    characterModel = characterSelected
    pdfVerticalReaderState = VerticalPdfReaderState(
        resource = ResourceType.Local(Uri.fromFile(File(characterModel.homebrewRoute))),
        isZoomEnable = true,
    )
    if (characterModel.name.isEmpty()) {
        EmptySelection()
    } else {
        Column(modifier = Modifier.background(topBarColor())) {
            if (characterModel.vidaMax > 0 || characterModel.manaMax > 0) {
                CustomHpManaBar(characterModel = characterModel, viewModel = viewModel)
            }
            when (isCharacterEmpty(character = characterModel)) {
                0 -> {
                    TabRowFull(characterSelected = characterModel, viewModel = viewModel)
                }

                1 -> {
                    TabRowCharacter(characterSelected = characterModel, viewModel = viewModel)
                }

                2 -> {
                    TabRowHomeBrew(characterSelected = characterModel, viewModel = viewModel)
                }

                3 -> {
                    InventoryScreen(characterModel = characterModel, viewModel = viewModel)
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
private fun TabRowFull(characterSelected: CharacterModel, viewModel: MainViewModel) {
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

        Tab(modifier = Modifier.padding(8.dp),
            selected = tabSelected.value == 0,
            onClick = { tabSelected.value = 0 }
        ) {
            val color = if (tabSelected.value == 0) {
                textColorAccent()
            } else {
                textColor()
            }
            Text(text = context.getString(R.string.tab_personatge), color = color)
        }

        Tab(modifier = Modifier.padding(8.dp),
            selected = tabSelected.value == 1,
            onClick = { tabSelected.value = 1 }
        ) {
            val color = if (tabSelected.value == 1) {
                textColorAccent()
            } else {
                textColor()
            }
            Text(text = context.getString(R.string.tab_homebrew), color = color)
        }

        Tab(modifier = Modifier.padding(8.dp),
            selected = tabSelected.value == 2,
            onClick = { tabSelected.value = 2 }
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
                if (characterSelected.homebrewRoute.isNotEmpty()) {
                    VerticalPDFReader(
                        state = pdfVerticalReaderState,
                        modifier = Modifier
                            .background(color = Color.White)
                            .clipToBounds(),
                    )
                }
            }

            2 -> {
                InventoryScreen(
                    characterModel = characterSelected,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
private fun TabRowCharacter(characterSelected: CharacterModel, viewModel: MainViewModel) {
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

        Tab(modifier = Modifier.padding(8.dp),
            selected = tabSelected.value == 0,
            onClick = { tabSelected.value = 0 }
        ) {
            val color = if (tabSelected.value == 0) {
                textColorAccent()
            } else {
                textColor()
            }
            Text(text = context.getString(R.string.tab_personatge), color = color)
        }

        Tab(modifier = Modifier.padding(8.dp),
            selected = tabSelected.value == 1,
            onClick = { tabSelected.value = 1 }
        ) {
            val color = if (tabSelected.value == 1) {
                textColorAccent()
            } else {
                textColor()
            }
            Text(text = context.getString(R.string.tab_inventari), color = color)
        }
    }

    Column(modifier = Modifier
            .background(backgroundColor())
            .fillMaxSize()
    ) {
        when (tabSelected.value) {
            0 -> {
                CharacterScreen(characterModel = characterSelected)
            }

            1 -> {
                InventoryScreen(
                    characterModel = characterSelected,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
private fun TabRowHomeBrew(characterSelected: CharacterModel, viewModel: MainViewModel) {
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
        Tab(modifier = Modifier.padding(8.dp),
            selected = tabSelected.value == 0,
            onClick = { tabSelected.value = 0 }
        ) {
            val color = if (tabSelected.value == 0) {
                textColorAccent()
            } else {
                textColor()
            }
            Text(text = context.getString(R.string.tab_homebrew), color = color)
        }

        Tab(modifier = Modifier.padding(8.dp),
            selected = tabSelected.value == 1,
            onClick = { tabSelected.value = 1 }
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
                VerticalPDFReader(
                    state = pdfVerticalReaderState,
                    modifier = Modifier
                        .background(color = Color.White)
                        .clipToBounds(),
                )
            }

            1 -> {
                InventoryScreen(
                    characterModel = characterSelected,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
private fun EmptySelection() {
    Image(
        painter = painterResource(R.drawable.empty),
        contentDescription = "",
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 8.dp)
    )
}
