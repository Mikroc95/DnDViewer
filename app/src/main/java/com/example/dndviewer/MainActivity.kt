package com.example.dndviewer

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle


import androidx.activity.compose.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dndviewer.Components.CustomNavigationDrawer
import com.example.dndviewer.Dialogs.DialogNewCharacter
import com.example.dndviewer.Models.CharacterModel
import com.example.dndviewer.Screens.MainScreen
import com.example.dndviewer.Screens.characterModel
import com.example.dndviewer.ViewModels.MainViewModel

class MainActivity : FragmentActivity() {
    private lateinit var viewModel: MainViewModel

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                viewModel = ViewModelProvider(this)[MainViewModel::class.java]
                viewModel.getBBDD(this)
                setContent {
                    Main(
                        context = this,
                        listCharacters = viewModel.getCharacters(),
                        viewModel = viewModel
                    )
                }
            }
        requestPermissionLauncher.launch(
            Manifest.permission.READ_EXTERNAL_STORAGE)
        requestPermissionLauncher.launch(
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        requestPermissionLauncher.launch(
            Manifest.permission.MANAGE_EXTERNAL_STORAGE)
    }
}
@Composable
private fun Main(
    context: Context,
    listCharacters: MutableList<CharacterModel>,
    viewModel: MainViewModel,
) {
    var updateCharacter = CharacterModel()
    val characterSelected: MutableState<CharacterModel> = remember { mutableStateOf(CharacterModel(image_character = byteArrayOf())) }
    val topBarTitle = remember { mutableStateOf(context.getString(R.string.app_name)) }
    val dialogNewCharacter = remember {
        //0 tancat
        //1 crear
        //2 editar
        mutableStateOf(0)
    }
    val listCharacter = remember { mutableStateOf(listCharacters) }
    if (characterSelected.value.name.isNotEmpty()) {
        topBarTitle.value = characterSelected.value.name
    }
    CustomNavigationDrawer(
        topBarTitle = topBarTitle,
        topBarIcon = painterResource(id = R.drawable.hamburger),
        characters = listCharacter.value,
        onCharacterSelected = { characterSelected.value = it },
        onNewCharacterClicked = {
            dialogNewCharacter.value = 1
        },
        onUpdateCharacter = {
            updateCharacter = it
            dialogNewCharacter.value = 2
        },
        onDeleteCharacter = {
            listCharacter.value.remove(characterSelected.value)
            viewModel.deleteCharacter(character = characterSelected.value)
            characterSelected.value = CharacterModel()
            topBarTitle.value = context.getString(R.string.app_name)
        },
        cntxt = context
    ) {
        MainScreen(
            cntxt = context,
            characterSelected = characterSelected.value,
            mainViewModel = viewModel
        )
        if (dialogNewCharacter.value>0) {
            DialogNewCharacter(
                characterModel = updateCharacter,
                onDismissRequest = {
                    if(dialogNewCharacter.value == 1){
                        viewModel.setCharacter(it)
                        listCharacter.value.add(it)
                    }else{
                        viewModel.updateCharacters(character = it)
                        updateCharacter = CharacterModel()
                    }
                    characterSelected.value = it
                    viewModel.insertSpells(it.name,it.maxSpell)
                    dialogNewCharacter.value = 0


                },
                onClose = {
                    updateCharacter = CharacterModel()
                    dialogNewCharacter.value = 0
                },
                context = context
            )
        }
    }
}


