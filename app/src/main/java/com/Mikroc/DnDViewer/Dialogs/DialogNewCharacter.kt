package com.Mikroc.DnDViewer.Dialogs

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.Mikroc.DnDViewer.Components.CustomTextField
import com.Mikroc.DnDViewer.Models.CharacterModel
import com.Mikroc.DnDViewer.Theme.backgroundColor
import com.Mikroc.DnDViewer.Theme.discordBlue
import com.Mikroc.DnDViewer.Theme.textColor
import com.Mikroc.DnDViewer.Theme.textColorAccent
import com.Mikroc.DnDViewer.Utils.GetCustomContents
import com.Mikroc.DnDViewer.R
import com.Mikroc.DnDViewer.Screens.viewModel
import com.Mikroc.DnDViewer.Theme.blueMana
import com.Mikroc.DnDViewer.Theme.discordRed
import java.io.File


@Composable
fun DialogNewCharacter(
    characterModel: CharacterModel = CharacterModel(),
    onDismissRequest: (CharacterModel) -> Unit,
    onClose: () -> Unit,
    context: Context
) {
    val character = if (characterModel.name.isEmpty()) {
        CharacterModel()
    } else {
        characterModel
    }

    val newCharacter = remember {
        mutableStateOf(character)
    }
    val name = remember {
        mutableStateOf(character.name)
    }
    val hp = remember {
        mutableStateOf(
            if (character.vidaMax > 0) {
                character.vidaMax.toString()
            } else {
                ""
            }
        )
    }
    val mana = remember {
        mutableStateOf(
            if (character.manaMax > 0) {
                character.manaMax.toString()
            } else {
                ""
            }
        )
    }
    val maxSpell = remember {
        mutableStateOf(
            if (character.maxSpell > 0) {
                character.maxSpell.toString()
            } else {
                ""
            }
        )
    }
    val characterPicker = rememberLauncherForActivityResult(
        contract = GetCustomContents(),
        onResult = { uris ->
            val item = context.contentResolver.openInputStream(uris.first())
            val imgByte = item?.readBytes()
            item?.close()
            newCharacter.value.imageCharacter = imgByte!!
        }
    )
    val homebrewPicker = rememberLauncherForActivityResult(
        contract = GetCustomContents(),
        onResult = { uris ->
            newCharacter.value.homebrewRoute = saveHomeBrew(
                context = context,
                name = newCharacter.value.name,
                uri = uris.first()
            )
        }
    )


    Dialog(onDismissRequest = {
        if (characterModel.name.isEmpty()) {
            File(newCharacter.value.homebrewRoute).apply {
                if (exists()) {
                    viewModel.deleteHomeBrew(
                        character = newCharacter.value,
                        context = context
                    )
                }
            }
        }
        onClose()
    }) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .background(backgroundColor())
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                CustomTextField(
                    value = name.value,
                    onValueChange = {
                        name.value = it
                        newCharacter.value.name = it
                    },
                    enabled = characterModel.name.isEmpty(),
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(3.dp))
                        .border(2.dp, discordBlue),
                    placeHolder = {
                        Text(
                            text = context.getString(R.string.new_character_name),
                            color = textColor()
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = backgroundColor(),
                        unfocusedContainerColor = backgroundColor(),
                        focusedTextColor = textColor(),
                        unfocusedTextColor = textColorAccent()
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalArrangement = Arrangement.Center
            ) {
                val borderColor = if(characterModel.name.isEmpty()) discordBlue else discordRed
                CustomTextField(
                    value = hp.value,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(3.dp))
                        .border(2.dp, borderColor),
                    onValueChange = {
                        hp.value = it
                        try {
                            newCharacter.value.vida = hp.value.toInt()
                            newCharacter.value.vidaMax = hp.value.toInt()
                        } catch (e: Exception) {
                            newCharacter.value.vida = 0
                            newCharacter.value.vidaMax = 0
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    placeHolder = {
                        Text(
                            text = context.getString(R.string.new_character_health_points),
                            color = textColor()
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = backgroundColor(),
                        unfocusedContainerColor = backgroundColor(),
                        focusedTextColor = textColor(),
                        unfocusedTextColor = textColorAccent()
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                val borderColor = if(characterModel.name.isEmpty()) discordBlue else blueMana
                CustomTextField(
                    value = mana.value,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(3.dp))
                        .border(2.dp, borderColor),
                    onValueChange = {
                        mana.value = it
                        try {
                            newCharacter.value.mana = mana.value.toInt()
                            newCharacter.value.manaMax = mana.value.toInt()

                        } catch (e: Exception) {
                            newCharacter.value.mana = 0
                            newCharacter.value.manaMax = 0
                        }

                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    placeHolder = {
                        Text(
                            text = context.getString(R.string.new_character_mana),
                            color = textColor()
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = backgroundColor(),
                        unfocusedContainerColor = backgroundColor(),
                        focusedTextColor = textColor(),
                        unfocusedTextColor = textColorAccent()
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CustomTextField(
                    value = maxSpell.value,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(3.dp))
                        .border(2.dp, discordBlue),
                    onValueChange = {
                        maxSpell.value = it
                        try {
                            newCharacter.value.maxSpell = maxSpell.value.toInt()
                        } catch (e: Exception) {
                            newCharacter.value.maxSpell = 0
                        }

                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    placeHolder = {
                        Text(
                            text = context.getString(R.string.new_character_maxSpell),
                            color = textColor()
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = backgroundColor(),
                        unfocusedContainerColor = backgroundColor(),
                        focusedTextColor = textColor(),
                        unfocusedTextColor = textColorAccent()
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                //.padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val buttonsEnabled = name.value.isNotEmpty()
                Button(
                    onClick = { characterPicker.launch("image/*") },
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(horizontal = 8.dp),
                    enabled = buttonsEnabled,
                    colors = ButtonDefaults.buttonColors(containerColor = discordBlue)
                ) {
                    Text(
                        text = context.getString(R.string.new_character_file).uppercase(),
                        color = textColor(),
                        fontSize = TextUnit(11f, TextUnitType.Sp)
                    )
                }

                Button(
                    onClick = { homebrewPicker.launch("application/pdf") },
                    modifier = Modifier.weight(0.5f),
                    enabled = buttonsEnabled,
                    colors = ButtonDefaults.buttonColors(containerColor = discordBlue)
                ) {
                    Text(
                        text = context.getString(R.string.new_character_homebrew).uppercase(),
                        color = textColor(),
                        fontSize = TextUnit(11f, TextUnitType.Sp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = discordBlue),
                    onClick = {
                        if (name.value.isNotEmpty()) {
                            newCharacter.value.apply {
                                this.name = name.value
                                onDismissRequest(this)
                            }
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.new_character_alert_name_empty),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                ) {
                    Text(
                        text = context.getString(R.string.dialog_save).uppercase(),
                        color = textColor()
                    )
                }
            }
        }
    }
}


private fun saveHomeBrew(context: Context, name: String, uri: Uri): String {
    val folder = File(context.filesDir.absolutePath + "/$name")
    folder.mkdirs()
    val file = File(folder.absolutePath + "/homebrew.pdf")
    folder.createNewFile()
    val cr: ContentResolver = context.contentResolver
    val input = cr.openInputStream(uri)
    file.outputStream().use { stream ->
        input?.copyTo(stream)
    }
    input?.close()
    return file.absolutePath.toString()
}
