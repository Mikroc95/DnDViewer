package com.Mikroc.DnDViewer.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.Mikroc.DnDViewer.Models.CharacterModel
import com.Mikroc.DnDViewer.R
import com.Mikroc.DnDViewer.Theme.blueMana
import com.Mikroc.DnDViewer.Theme.discordRed
import com.Mikroc.DnDViewer.ViewModels.MainViewModel

@Composable
fun CustomHpManaBar(characterModel: CharacterModel,viewModel:MainViewModel){
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
    ) {
        if (characterModel.vidaMax > 0) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val valueHP = remember {
                    mutableStateOf("")
                }
                val currentHP = remember {
                    mutableIntStateOf(characterModel.vida)
                }

                InputCounter(
                    totalResult = "${currentHP.intValue} / ${characterModel.vidaMax} ${
                        LocalContext.current.getString(
                            R.string.counter_hp
                        )
                    }",
                    borderColor = discordRed,
                    labelColor = discordRed,
                    valueTextField = valueHP,
                    onKeyBoardDone = {
                        try{
                            if (valueHP.value.isNotEmpty()) {
                                val value = valueHP.value.toInt()
                                currentHP.intValue = if (currentHP.intValue + value > 0) {
                                    if (currentHP.intValue + value <= characterModel.vidaMax) {
                                        currentHP.intValue + value
                                    } else {
                                        characterModel.vidaMax
                                    }
                                } else {
                                    0
                                }
                                characterModel.vida = currentHP.intValue
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                            valueHP.value = ""
                        }
                    },
                    onLessClicked = {
                        val hp = currentHP.intValue
                        if (hp - 1 >= 0) {
                            currentHP.intValue = hp - 1
                            characterModel.vida = currentHP.intValue
                            viewModel.updateCharacters(character = characterModel)
                        }
                    },
                    onPlusClicked = {
                        val hp = currentHP.intValue
                        if (hp + 1 <= characterModel.vidaMax) {
                            currentHP.intValue = hp + 1
                            characterModel.vida = currentHP.intValue
                            viewModel.updateCharacters(character = characterModel)
                        }
                    }
                )
            }
        }
        if (characterModel.manaMax > 0) {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val valueMana = remember {
                    mutableStateOf("")
                }
                val currentMana = remember {
                    mutableIntStateOf(0)
                }
                currentMana.intValue = characterModel.mana
                InputCounter(
                    totalResult = "${currentMana.intValue} / ${characterModel.manaMax} ${
                        LocalContext.current.getString(
                            R.string.counter_mana
                        )
                    }",
                    borderColor = blueMana,
                    labelColor = blueMana,
                    valueTextField = valueMana,
                    onKeyBoardDone = {
                        try{
                            if (valueMana.value.isNotEmpty()) {
                                val value = valueMana.value.toInt()
                                currentMana.intValue = if (currentMana.intValue + value > 0) {
                                    if (currentMana.intValue + value <= characterModel.manaMax) {
                                        currentMana.intValue + value
                                    } else {
                                        characterModel.manaMax
                                    }
                                } else {
                                    0
                                }
                                characterModel.mana = currentMana.intValue
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                            valueMana.value = ""
                        }
                    },
                    onLessClicked = {
                        val mana = currentMana.intValue
                        if (mana - 1 >= 0) {
                            currentMana.intValue = mana - 1
                            characterModel.mana = currentMana.intValue
                            viewModel.updateCharacters(character = characterModel)
                        }
                    },
                    onPlusClicked = {
                        val mana = currentMana.intValue
                        if (mana + 1 <= characterModel.manaMax) {
                            currentMana.intValue = mana + 1
                            characterModel.mana = currentMana.intValue
                            viewModel.updateCharacters(character = characterModel)
                        }
                    })
            }
        }
    }
}