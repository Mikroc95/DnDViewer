package com.Mikroc.DnDViewer.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.Mikroc.DnDViewer.Dialogs.DialogFall
import com.Mikroc.DnDViewer.R
import com.Mikroc.DnDViewer.Theme.blueMana
import com.Mikroc.DnDViewer.Theme.discordRed
import com.Mikroc.DnDViewer.Theme.yellowMetamagic
import com.Mikroc.DnDViewer.Utils.stringToInt
import com.Mikroc.DnDViewer.ViewModels.MainViewModel


@Composable
fun CustomHpManaBar(viewModel: MainViewModel) {
    val selected by viewModel.selectedCharacter.collectAsState()

    var isCharacterFallen by remember { mutableStateOf(false) }

    var valueHP by remember(selected.code) { mutableStateOf("") }
    var valueMana by remember(selected.code) { mutableStateOf("") }
    var valueMetaMagic by remember(selected.code) { mutableStateOf("") }
    var previousVidaForDialogCheck by remember(selected.code) { mutableIntStateOf(selected.vida) }
    LaunchedEffect(selected.vida) {
        val justDead = (selected.vida <= 0 && previousVidaForDialogCheck > 0)
        if (justDead) {
            isCharacterFallen = true
        } else if (selected.vida > 0) {
            if (isCharacterFallen) {
                isCharacterFallen = false
            }
        }
        previousVidaForDialogCheck = selected.vida
    }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,

        ) {
        if (selected.vidaMax > 0) {
            InputCounterWrapper(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                currentValue = selected.vida,
                maxValue = selected.vidaMax,
                label = LocalContext.current.getString(R.string.counter_hp),
                inputValue = valueHP,
                onInputValueChange = {
                    valueHP = it
                },
                borderColor = discordRed,
                labelColor = discordRed,
                onKeyboardSubmit = { _ ->
                    if (valueHP.isNotEmpty()) {
                        val value = valueHP.stringToInt()
                        val hp = if (selected.vida + value > 0) {
                            if (selected.vida + value <= selected.vidaMax) {
                                selected.vida + value
                            } else {
                                selected.manaMax
                            }
                        } else {
                            0
                        }
                        viewModel.updateCharacterHP(newHP = hp)
                        valueHP = ""
                    }

                },
                onDecrement = {
                    val hp = if (selected.vida - 1 >= 0) {
                        selected.vida - 1
                    } else {
                        selected.vida
                    }
                    viewModel.updateCharacterHP(newHP = hp)
                },
                onIncrement = {
                    val hp = if (selected.vida + 1 <= selected.vidaMax) {
                        selected.vida + 1
                    } else {
                        selected.vida
                    }
                    viewModel.updateCharacterHP(newHP = hp)
                }
            )
        }

        if (selected.manaMax > 0) {
            InputCounterWrapper(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                currentValue = selected.mana,
                maxValue = selected.manaMax,
                label = LocalContext.current.getString(R.string.counter_mana),
                inputValue = valueMana,
                onInputValueChange = { valueMana = it },
                borderColor = blueMana,
                labelColor = blueMana,
                onKeyboardSubmit = { _ ->
                    if (valueMana.isNotEmpty()) {
                        val value = valueMana.stringToInt()
                        val mana = if (selected.mana + value > 0) {
                            if (selected.mana + value <= selected.manaMax) {
                                (selected.mana + value)
                            } else {
                                selected.manaMax
                            }
                        } else {
                            0
                        }
                        viewModel.updateCharacterMana(mana)
                        valueMana = ""
                    }

                },
                onDecrement = {
                    val mana = if (selected.mana - 1 >= 0) {
                        selected.mana - 1
                    } else {
                        selected.mana
                    }
                    viewModel.updateCharacterMana(newMana = mana)
                },
                onIncrement = {
                    val mana = if (selected.mana + 1 <= selected.manaMax) {
                        selected.mana + 1
                    } else {
                        selected.mana
                    }
                    viewModel.updateCharacterHP(newHP = mana)
                }
            )
        }

        if (selected.metaMagiaMax > 0) {
            InputCounterWrapper(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                currentValue = selected.metaMagia,
                maxValue = selected.metaMagiaMax,
                label = LocalContext.current.getString(R.string.counter_metamagic),
                inputValue = valueMetaMagic,
                onInputValueChange = { valueMetaMagic = it },
                borderColor = yellowMetamagic,
                labelColor = yellowMetamagic,
                onKeyboardSubmit = { _ ->
                    if (valueMetaMagic.isNotEmpty()) {
                        val value = valueMetaMagic.stringToInt()
                        val metaMagic = if (selected.metaMagia + value > 0) {
                            if (selected.metaMagia + value <= selected.metaMagiaMax) {
                                selected.metaMagia + value
                            } else {
                                selected.metaMagiaMax
                            }
                        } else {
                            0
                        }
                        viewModel.updateCharacterMetaMagic(newMetaMagic = metaMagic)
                        valueMetaMagic = ""
                    }

                },
                onDecrement = {
                    val metaMagic = if (selected.metaMagia - 1 >= 0) {
                        selected.metaMagia - 1
                    } else {
                        selected.metaMagia
                    }
                    viewModel.updateCharacterMetaMagic(newMetaMagic = metaMagic)
                },
                onIncrement = {
                    val metaMagic = if (selected.metaMagia + 1 <= selected.metaMagiaMax) {
                        selected.metaMagia + 1
                    } else {
                        selected.metaMagia
                    }
                    viewModel.updateCharacterMetaMagic(newMetaMagic = metaMagic)
                }
            )
        }
    }
    if (isCharacterFallen) {
        Dialog(onDismissRequest = {}) {
            DialogFall(viewModel = viewModel, onCharacterResolved = {
                isCharacterFallen = false
            })
        }
    }
}

@Composable
private fun InputCounterWrapper(
    modifier: Modifier = Modifier,
    currentValue: Int,
    maxValue: Int,
    label: String,
    inputValue: String,
    onInputValueChange: (String) -> Unit,
    borderColor: androidx.compose.ui.graphics.Color,
    labelColor: androidx.compose.ui.graphics.Color,
    onKeyboardSubmit: (Int) -> Unit,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start
    ) {
        InputCounter(
            totalResult = "$currentValue / $maxValue $label",
            inputPadding = PaddingValues(horizontal = 6.dp),
            borderColor = borderColor,
            labelColor = labelColor,
            valueTextField = inputValue,
            onKeyBoardDone = {
                try {
                    if (inputValue.isNotEmpty()) {
                        onKeyboardSubmit(inputValue.toInt())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    onInputValueChange("")
                }
            },
            onLessClicked = onDecrement,
            onPlusClicked = onIncrement,
            onTextFieldValueChange = onInputValueChange
        )
    }
}

