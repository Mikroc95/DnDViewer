package com.example.dndviewer.Screens

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import com.example.dndviewer.Components.FiveEView
import com.example.dndviewer.Models.CharacterModel
import com.example.dndviewer.Screens.Character.CharacterScreen
import com.example.dndviewer.Screens.Inventory.InventoryScreen
import com.example.dndviewer.Theme.backgroundColor
import com.example.dndviewer.Theme.discordBlue
import com.example.dndviewer.Theme.textColor
import com.example.dndviewer.Theme.textColorAccent
import com.example.dndviewer.Theme.topBarColor
import com.example.dndviewer.ViewModels.MainViewModel
import com.example.dndviewer.R
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.VerticalPdfReaderState
import java.io.File


var tabSelected: MutableState<Int> = mutableIntStateOf(0)

var characterModel = CharacterModel(imageCharacter = byteArrayOf())

var pdfVerticalReaderState = VerticalPdfReaderState(
    resource = ResourceType.Local(Uri.fromFile(File(characterModel.homebrewRoute))),
    isZoomEnable = true
)
lateinit var viewModel: MainViewModel

@Composable
fun MainScreen(context: Context, characterSelected: CharacterModel, mainViewModel: MainViewModel) {
    characterModel = characterSelected
    //context = cont
    viewModel = mainViewModel
    pdfVerticalReaderState = VerticalPdfReaderState(
        resource = ResourceType.Local(Uri.fromFile(File(characterModel.homebrewRoute))),
        isZoomEnable = true,
    )
    if (characterSelected.name.isEmpty()) {
        EmptySelection()
    } else {
        TabRowCharacter(context = context, characterSelected)
    }
}

@Composable
private fun TabRowCharacter(context: Context, characterSelected: CharacterModel) {
    TabRow(
        modifier = Modifier
            .fillMaxWidth()
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
            Text(text = context.getString(R.string.tab_1), color = color)
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
            Text(text = context.getString(R.string.tab_2), color = color)
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
            Text(text = context.getString(R.string.tab_3), color = color)
        }
    }

    Column(
        modifier = Modifier
            .background(backgroundColor())
            .fillMaxSize(),

        ) {
        when (tabSelected.value) {
            0 -> {
                CharacterScreen(context = context, characterModel = characterSelected)
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(backgroundColor())
                ) {
                    InventoryScreen(context = context, characterModel = characterSelected)
                }
            }
        }

    }
}


@Composable
private fun EmptySelection() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        FiveEView()
    }
}
