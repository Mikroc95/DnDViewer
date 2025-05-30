package com.mikroc.dndviewer.dialogs

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mikroc.dndviewer.bbdd.repository.database.FakeCharacterRepository
import com.mikroc.dndviewer.bbdd.repository.database.FakeItemsRepository
import com.mikroc.dndviewer.bbdd.repository.database.FakeSpellRepository
import com.mikroc.dndviewer.components.CustomTextField
import com.mikroc.dndviewer.models.CharacterModel
import com.mikroc.dndviewer.theme.backgroundColor
import com.mikroc.dndviewer.theme.discordBlue
import com.mikroc.dndviewer.theme.textColor
import com.mikroc.dndviewer.theme.textColorAccent
import com.mikroc.dndviewer.utils.GetCustomContents
import com.mikroc.dndviewer.R
import com.mikroc.dndviewer.theme.blueMana
import com.mikroc.dndviewer.theme.discordRed
import com.mikroc.dndviewer.theme.yellowMetaMagic
import com.mikroc.dndviewer.viewmodels.MainViewModel
import kotlinx.coroutines.launch
import java.io.File


@Composable
fun DialogNewCharacter(
    characterModel: CharacterModel = CharacterModel(),
    onDismissRequest: (CharacterModel) -> Unit,
    onClose: () -> Unit,
    viewModel: MainViewModel
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val character = if (characterModel.name.isEmpty()) {
        CharacterModel()
    } else {
        characterModel
    }

    val newCharacter = remember {
        mutableStateOf(
            CharacterModel(
                code = character.code,
                name = character.name,
                imageCharacter = character.imageCharacter,
                homebrewRoute = character.homebrewRoute,
                vida = character.vida,
                vidaMax = character.vidaMax,
                mana = character.mana,
                manaMax = character.manaMax,
                metaMagic = character.metaMagic,
                metaMagicMax = character.metaMagicMax,
                observations = character.observations,
                maxSpell = character.maxSpell
            )
        )
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
    val metaMagic = remember {
        mutableStateOf(
            if (character.metaMagicMax > 0) {
                character.metaMagicMax.toString()
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
            try {
                val item = context.contentResolver.openInputStream(uris.first())
                val imgByte = item?.readBytes()
                item?.close()
                newCharacter.value.imageCharacter = imgByte!!
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    context.getString(R.string.new_character_error_character),
                    Toast.LENGTH_SHORT
                ).show()
                newCharacter.value.imageCharacter = byteArrayOf()
                e.printStackTrace()
            }
        }
    )
    val homebrewPicker = rememberLauncherForActivityResult(
        contract = GetCustomContents(),
        onResult = { uris ->
            try {
                newCharacter.value.homebrewRoute = saveHomeBrew(
                    context = context,
                    name = newCharacter.value.name,
                    uri = uris.first()
                )
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    context.getString(R.string.new_character_error_homebrew),
                    Toast.LENGTH_SHORT
                ).show()
                newCharacter.value.homebrewRoute = ""
                e.printStackTrace()
            }
        }
    )


    Dialog(onDismissRequest = {
        if (character.name.isEmpty()) {
            File(newCharacter.value.homebrewRoute).apply {
                if (exists()) {
                    viewModel.deleteHomeBrew(
                        characterName = newCharacter.value.name,
                        homebrewRoute = newCharacter.value.homebrewRoute,
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
                val borderColor = if (characterModel.name.isEmpty()) discordBlue else discordRed
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
                            e.printStackTrace()
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
                val borderColor = if (characterModel.name.isEmpty()) discordBlue else blueMana
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
                            e.printStackTrace()
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
                val borderColor =
                    if (characterModel.name.isEmpty()) discordBlue else yellowMetaMagic
                CustomTextField(
                    value = metaMagic.value,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(3.dp))
                        .border(2.dp, borderColor),
                    onValueChange = {
                        metaMagic.value = it
                        try {
                            newCharacter.value.metaMagic = metaMagic.value.toInt()
                            newCharacter.value.metaMagicMax = metaMagic.value.toInt()

                        } catch (e: Exception) {
                            e.printStackTrace()
                            newCharacter.value.metaMagic = 0
                            newCharacter.value.metaMagicMax = 0
                        }

                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    placeHolder = {
                        Text(
                            text = context.getString(R.string.new_character_meta_magic),
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
                            e.printStackTrace()
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
                        try {
                            if (characterModel.name.isEmpty()) {
                                // CREANT
                                if (name.value.isNotEmpty()) {
                                    if (viewModel.getCharacterByName(name.value).name.isNotEmpty()) {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.new_character_repeated_character),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        newCharacter.value.apply {
                                            this.name = name.value
                                            onDismissRequest(this)
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.new_character_alert_name_empty),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                //EDITANT
                                if (characterModel.name != newCharacter.value.name) {
                                    val oldHomebrewFile = File(characterModel.homebrewRoute)
                                    if (oldHomebrewFile.exists()) {
                                        try {
                                            val newFolder =
                                                File(context.filesDir.absolutePath + "/${newCharacter.value.name}")
                                            newFolder.mkdirs()
                                            val newHomebrewFile =
                                                File(newFolder.absolutePath + "/homebrew.pdf")
                                            newHomebrewFile.createNewFile()

                                            oldHomebrewFile.inputStream()
                                                .use { input ->
                                                    newHomebrewFile.outputStream().use { output ->
                                                        input.copyTo(output)
                                                    }
                                                }

                                            newCharacter.value.homebrewRoute =
                                                newHomebrewFile.absolutePath

                                            viewModel.deleteHomeBrew(
                                                characterName = characterModel.name,
                                                homebrewRoute = characterModel.homebrewRoute,
                                                context = context
                                            )

                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.new_character_error_save),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                }

                                scope.launch {
                                    val oldItemBD = viewModel.getCharacter(characterModel.code)
                                    if (oldItemBD.vidaMax == newCharacter.value.vidaMax) {
                                        newCharacter.value.vida = oldItemBD.vida
                                    } else {
                                        val actualToAdd =
                                            newCharacter.value.vidaMax - oldItemBD.vidaMax
                                        if (actualToAdd > 0) {
                                            newCharacter.value.vida = oldItemBD.vida + actualToAdd

                                        } else {
                                            if (oldItemBD.vida > newCharacter.value.vidaMax) {
                                                newCharacter.value.vida = newCharacter.value.vidaMax
                                            } else {
                                                newCharacter.value.vida = oldItemBD.vida
                                            }
                                        }
                                    }
                                    if (oldItemBD.manaMax == newCharacter.value.manaMax) {
                                        newCharacter.value.mana = oldItemBD.mana
                                    }
                                    if (oldItemBD.metaMagicMax == newCharacter.value.metaMagicMax) {
                                        newCharacter.value.metaMagic = oldItemBD.metaMagic
                                    }

                                    if (newCharacter.value.homebrewRoute.isEmpty() && characterModel.homebrewRoute.isNotEmpty()) {
                                        newCharacter.value.homebrewRoute =
                                            characterModel.homebrewRoute
                                    }
                                    if (newCharacter.value.imageCharacter.isEmpty() && characterModel.imageCharacter.isNotEmpty()) {
                                        newCharacter.value.imageCharacter =
                                            characterModel.imageCharacter
                                    }
                                    newCharacter.value.observations = oldItemBD.observations
                                    onDismissRequest(newCharacter.value)

                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(
                                context,
                                context.getString(R.string.new_character_error_save),
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

@Preview
@Composable
private fun DialogNewCharacterPreview() {
    DialogNewCharacter(
        onDismissRequest = {},
        onClose = { },
        viewModel = MainViewModel(
            itemRepository = FakeItemsRepository(),
            characterRepository = FakeCharacterRepository(),
            spellRepository = FakeSpellRepository()
        )
    )
}


