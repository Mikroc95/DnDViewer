package com.Mikroc.DnDViewer.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import com.Mikroc.DnDViewer.Dialogs.DialogFall
import com.Mikroc.DnDViewer.Models.CharacterModel
import com.Mikroc.DnDViewer.R
import com.Mikroc.DnDViewer.Theme.blueMana
import com.Mikroc.DnDViewer.Theme.discordRed
import com.Mikroc.DnDViewer.Theme.yellowMetamagic
import com.Mikroc.DnDViewer.ViewModels.MainViewModel


@Composable
fun CustomHpManaBar(characterModel: CharacterModel, viewModel: MainViewModel) {
    val isCharacterFalled = remember {
        mutableStateOf(false)
    }
    val valueHP = remember {
        mutableStateOf("")
    }
    val currentHP = remember {
        mutableIntStateOf(characterModel.vida)
    }
    val valueMana = remember {
        mutableStateOf("")
    }
    val currentMana = remember {
        mutableIntStateOf(characterModel.mana)
    }
    val valueMetaMagia = remember { mutableStateOf("") }
    val currentMetaMagia = remember {
        mutableIntStateOf(characterModel.metaMagia)
    }
    //cutre pero eficaç
    currentMana.intValue = characterModel.mana
    currentHP.intValue = characterModel.vida
    currentMetaMagia.intValue = characterModel.metaMagia
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,

        ) {
        if (characterModel.vidaMax > 0) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .weight(1f)
            ) {
                InputCounter(
                    totalResult = "${currentHP.intValue} / ${characterModel.vidaMax} ${
                        LocalContext.current.getString(
                            R.string.counter_hp
                        )
                    }",
                    inputPadding = PaddingValues(horizontal = 6.dp),
                    borderColor = discordRed,
                    labelColor = discordRed,
                    valueTextField = valueHP,
                    onKeyBoardDone = {
                        try {
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
                                if (characterModel.vida <= 0) {
                                    isCharacterFalled.value = true
                                }
                                viewModel.updateCharacters(character = characterModel)
                            }
                        } catch (e: Exception) {
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
                        if (characterModel.vida <= 0) {
                            isCharacterFalled.value = true
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
                horizontalArrangement = if (characterModel.metaMagiaMax > 0) Arrangement.Center else Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .weight(1f)

            ) {
                InputCounter(
                    totalResult = "${currentMana.intValue} / ${characterModel.manaMax} ${
                        LocalContext.current.getString(
                            R.string.counter_mana
                        )
                    }",
                    inputPadding = PaddingValues(horizontal = 6.dp),
                    borderColor = blueMana,
                    labelColor = blueMana,
                    valueTextField = valueMana,
                    onKeyBoardDone = {
                        try {
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
                                viewModel.updateCharacters(character = characterModel)
                            }
                        } catch (e: Exception) {
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
                    }
                )
            }
        }

        if (characterModel.metaMagiaMax > 0) {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .weight(1f)
            ) {
                InputCounter(
                    totalResult = "${currentMetaMagia.intValue} / ${characterModel.metaMagiaMax} ${
                        LocalContext.current.getString(
                            R.string.counter_metamagic
                        )
                    }",
                    inputPadding = PaddingValues(horizontal = 6.dp),
                    borderColor = yellowMetamagic,
                    labelColor = yellowMetamagic,
                    valueTextField = valueMetaMagia,
                    onKeyBoardDone = {
                        try {
                            if (valueMetaMagia.value.isNotEmpty()) {
                                val value = valueMetaMagia.value.toInt()
                                currentMetaMagia.intValue =
                                    if (currentMetaMagia.intValue + value > 0) {
                                        if (currentMetaMagia.intValue + value <= characterModel.metaMagiaMax) {
                                            currentMetaMagia.intValue + value
                                        } else {
                                            characterModel.metaMagiaMax
                                        }
                                    } else {
                                        0
                                    }
                                characterModel.metaMagia = currentMetaMagia.intValue
                                viewModel.updateCharacters(character = characterModel)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            valueHP.value = ""
                        }
                    },
                    onLessClicked = {
                        val metamagia = currentMetaMagia.intValue
                        if (metamagia - 1 >= 0) {
                            currentMetaMagia.intValue = metamagia - 1
                            characterModel.metaMagia = currentMetaMagia.intValue
                            viewModel.updateCharacters(character = characterModel)
                        }
                    },
                    onPlusClicked = {
                        val metamagia = currentMetaMagia.intValue
                        if (metamagia + 1 <= characterModel.metaMagiaMax) {
                            currentMetaMagia.intValue = metamagia + 1
                            characterModel.metaMagia = currentMetaMagia.intValue
                            viewModel.updateCharacters(character = characterModel)
                        }
                    }
                )
            }
        }
    }
    if (isCharacterFalled.value) {
        DialogFall(fallen = isCharacterFalled, viewModel = viewModel)
    }
}

