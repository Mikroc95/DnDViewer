package com.Mikroc.DnDViewer

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.Mikroc.DnDViewer.Components.CustomNavigationDrawer
import com.Mikroc.DnDViewer.Dialogs.DialogNewCharacter
import com.Mikroc.DnDViewer.Models.CharacterModel
import com.Mikroc.DnDViewer.Screens.MainScreen
import com.Mikroc.DnDViewer.Screens.tabSelected
import com.Mikroc.DnDViewer.Theme.discordBlue
import com.Mikroc.DnDViewer.Theme.discordLigthBlack
import com.Mikroc.DnDViewer.Theme.textColor
import com.Mikroc.DnDViewer.ViewModels.MainViewModel

class MainActivity : FragmentActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Main(
    context: Context,
    listCharacters: MutableList<CharacterModel>,
    viewModel: MainViewModel,
) {
    var updateCharacter = CharacterModel()
    val topBarTitle = remember { mutableStateOf(context.getString(R.string.app_name)) }
    val dialogNewCharacter = remember {
        //0 tancat
        //1 crear
        //2 editar
        mutableIntStateOf(0)
    }
    val dialogDeleteCharacter = remember {
        mutableStateOf(false)
    }

    if (viewModel.characterSelected.value.name.isNotEmpty()) {
        topBarTitle.value = viewModel.characterSelected.value.name
    }
    CustomNavigationDrawer(
        topBarTitle = topBarTitle,
        topBarIcon = painterResource(id = R.drawable.hamburger),
        characters = listCharacters,
        onCharacterSelected = {
            viewModel.characterSelected.value = viewModel.getCharacter(it.name).first()
            /*CharacterModel(
                name = it.name,
                imageCharacter = it.imageCharacter,
                homebrewRoute = it.homebrewRoute,
                vida = it.vida,
                vidaMax = it.vidaMax,
                mana = it.mana,
                manaMax = it.manaMax,
                maxSpell = it.maxSpell,
                observations = it.observations
            )*/
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
            dialogDeleteCharacter.value = true
        }
    ) {
        MainScreen(
            characterSelected = viewModel.characterSelected.value,
            viewModel = viewModel
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
                    viewModel.characterSelected.value = CharacterModel(
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
                viewModel = viewModel,
            )
        }
        if(dialogDeleteCharacter.value){
            BasicAlertDialog(onDismissRequest = { dialogDeleteCharacter.value = false}) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(discordLigthBlack),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = context.getString(R.string.delete_character),
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
                                listCharacters.remove(viewModel.characterSelected.value)
                                viewModel.deleteCharacter(character = viewModel.characterSelected.value, context = context)
                                viewModel.characterSelected.value = CharacterModel()
                                topBarTitle.value = context.getString(R.string.app_name)
                                dialogDeleteCharacter.value = false
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
                            onClick = { dialogDeleteCharacter.value = false },
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


