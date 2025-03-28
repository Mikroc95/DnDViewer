package com.Mikroc.DnDViewer

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.Mikroc.DnDViewer.Components.CustomNavigationDrawer
import com.Mikroc.DnDViewer.Dialogs.DialogNewCharacter
import com.Mikroc.DnDViewer.Models.CharacterModel
import com.Mikroc.DnDViewer.Screens.MainScreen
import com.Mikroc.DnDViewer.Screens.tabSelected
import com.Mikroc.DnDViewer.ViewModels.MainViewModel

class MainActivity : FragmentActivity() {
    private lateinit var viewModel: MainViewModel

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { _ ->
                viewModel = ViewModelProvider(this)[MainViewModel::class.java]
                viewModel.getBBDD(this)

                setContent {
                    val listCharacters: MutableList<CharacterModel> = rememberSaveable {
                        viewModel.getCharacters()
                    }
                    Main(
                        context = this,
                        listCharacters = listCharacters,
                        viewModel = viewModel
                    )
                }
            }
        requestPermissionLauncher.launch(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        requestPermissionLauncher.launch(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        requestPermissionLauncher.launch(
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        )
    }
}

@Composable
private fun Main(
    context: Context,
    listCharacters: MutableList<CharacterModel>,
    viewModel: MainViewModel,
) {
    var updateCharacter = CharacterModel()
    val characterSelected: MutableState<CharacterModel> =
        remember { mutableStateOf(CharacterModel()) }
    val topBarTitle = remember { mutableStateOf(context.getString(R.string.app_name)) }
    val dialogNewCharacter = remember {
        //0 tancat
        //1 crear
        //2 editar
        mutableIntStateOf(0)
    }

    if (characterSelected.value.name.isNotEmpty()) {
        topBarTitle.value = characterSelected.value.name
    }
    CustomNavigationDrawer(
        topBarTitle = topBarTitle,
        topBarIcon = painterResource(id = R.drawable.hamburger),
        characters = listCharacters,
        onCharacterSelected = {
            characterSelected.value = CharacterModel(
                name = it.name,
                imageCharacter = it.imageCharacter,
                homebrewRoute = it.homebrewRoute,
                vida = it.vida,
                vidaMax = it.vidaMax,
                mana = it.mana,
                manaMax = it.manaMax,
                maxSpell = it.maxSpell,
                observations = it.observations
            )
            tabSelected.value = 0
        },
        onNewCharacterClicked = {
            dialogNewCharacter.intValue = 1
        },
        onUpdateCharacter = {
            updateCharacter = it
            dialogNewCharacter.intValue = 2
        },
        onDeleteCharacter = {
            listCharacters.remove(characterSelected.value)
            viewModel.deleteCharacter(character = characterSelected.value, context = context)
            characterSelected.value = CharacterModel()
            topBarTitle.value = context.getString(R.string.app_name)
        },
        context = context
    ) {
        MainScreen(
            context = context,
            characterSelected = characterSelected.value,
            mainViewModel = viewModel
        )
        if (dialogNewCharacter.intValue > 0) {
            DialogNewCharacter(
                characterModel = updateCharacter,
                onDismissRequest = {
                    if (dialogNewCharacter.intValue == 1) {
                        viewModel.setCharacter(it)
                        listCharacters.add(it)
                    } else {
                        viewModel.updateCharacters(character = it)
                        updateCharacter = CharacterModel()
                    }
                    viewModel.insertSpells(it.name, it.maxSpell)
                    characterSelected.value = CharacterModel(
                        name = it.name,
                        imageCharacter = it.imageCharacter,
                        homebrewRoute = it.homebrewRoute,
                        vida = it.vida,
                        vidaMax = it.vidaMax,
                        mana = it.mana,
                        manaMax = it.manaMax,
                        maxSpell = it.maxSpell,
                        observations = it.observations
                    )
                    dialogNewCharacter.intValue = 0
                    tabSelected.value = 0
                },
                onClose = {
                    updateCharacter = CharacterModel()
                    dialogNewCharacter.intValue = 0
                },
                context = context
            )
        }
    }
}


