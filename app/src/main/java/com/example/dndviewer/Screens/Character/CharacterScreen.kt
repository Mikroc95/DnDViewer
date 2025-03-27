package com.example.dndviewer.Screens.Character

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.dndviewer.Components.InputCounter
import com.example.dndviewer.Models.CharacterModel
import com.example.dndviewer.Screens.viewModel
import com.example.dndviewer.Theme.blueMana
import com.example.dndviewer.Theme.discordRed
import com.example.dndviewer.R

@Composable
fun CharacterScreen(context: Context, characterModel: CharacterModel) {
    var scaleCharacters by remember { mutableFloatStateOf(1f) }
    var offsetCharacters by remember { mutableStateOf(Offset(0f, 0f)) }
    if (characterModel.vidaMax > 0 || characterModel.manaMax > 0) {
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
                            context.getString(
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
                            context.getString(
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

    if (!characterModel.imageCharacter.contentEquals(byteArrayOf())) {
        // Create an Image composable with zooming and panning.
        val bitmap = characterModel.imageCharacter.toBitmap()
        val scrollImage = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxSize()
                .scrollable(scrollImage, orientation = Orientation.Vertical)
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(), // Replace 'imagePainter' with your image
                contentDescription = null,
                modifier = Modifier
                    .clipToBounds()
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        this.detectTransformGestures { _, pan, zoom, _ ->
                            // Update the scale based on zoom gestures.
                            scaleCharacters *= zoom

                            // Limit the zoom levels within a certain range (optional).
                            scaleCharacters = scaleCharacters.coerceIn(0.5f, 10f)

                            // Update the offset to implement panning when zoomed.
                            offsetCharacters = if (scaleCharacters == 1f) Offset(
                                0f,
                                0f
                            ) else offsetCharacters + pan
                        }
                    }
                    .graphicsLayer(
                        scaleX = scaleCharacters, scaleY = scaleCharacters,
                        translationX = offsetCharacters.x, translationY = offsetCharacters.y
                    )
            )
        }
    }
}

fun ByteArray.toBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}
