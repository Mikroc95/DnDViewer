package com.example.dndviewer.Dialogs

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
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
import androidx.compose.material3.TextField
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
import com.example.dndviewer.Components.CustomTextField
import com.example.dndviewer.Models.CharacterModel
import com.example.dndviewer.Theme.backgroundColor
import com.example.dndviewer.Theme.discordBlue
import com.example.dndviewer.Theme.textColor
import com.example.dndviewer.Theme.textColorAccent
import com.example.dndviewer.utils.GetCustomContents
import com.example.dndviewer.R
import java.io.File


@Composable
fun DialogNewCharacter(
    characterModel: CharacterModel = CharacterModel(),
    onDismissRequest: (CharacterModel) -> Unit,
    onClose:()->Unit,
    context: Context){

    val newCharacter = remember {
        mutableStateOf(characterModel)
    }
    val name = remember {
        mutableStateOf(characterModel.name)
    }
    val hp = remember {
        mutableStateOf(
            if(characterModel.vidaMax>0){
                characterModel.vidaMax.toString()
            }else {
                ""
            }
        )
    }
    val mana = remember {
        mutableStateOf(
            if(characterModel.manaMax>0){
                characterModel.manaMax.toString()
            }else {
                ""
            }
        )
    }
    val maxSpell = remember {
        mutableStateOf(
            if(characterModel.maxSpell>0){
                characterModel.maxSpell.toString()
            }else {
                ""
            }
        )
    }
    val characterPicker = rememberLauncherForActivityResult(
        contract = GetCustomContents(isMultiple = false),
        onResult = { uris ->
            val item = context.contentResolver.openInputStream(uris.first())
            val imgbyte = item?.readBytes()
            item?.close()
            newCharacter.value.image_character = imgbyte!!
        }
    )
    val homebrewPicker = rememberLauncherForActivityResult(
        contract = GetCustomContents(isMultiple = false),
        onResult = { uris ->
            newCharacter.value.homebrewRoute = saveHomeBrew(
                context = context,
                name = newCharacter.value.name,
                uri = uris.first()
            )
        }
    )


    Dialog(onDismissRequest = onClose) {
        Column(modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .background(backgroundColor())
            .padding(16.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                CustomTextField(
                    value = name.value,
                    onValueChange = {
                        name.value = it

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
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), horizontalArrangement = Arrangement.Center) {
                CustomTextField(
                    value =  hp.value,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(3.dp))
                        .border(2.dp, discordBlue),
                    onValueChange =  {
                        hp.value = it
                        try{
                            newCharacter.value.vida = hp.value.toInt()
                            newCharacter.value.vidaMax = hp.value.toInt()
                        }catch (e:Exception){
                            newCharacter.value.vida = 0
                            newCharacter.value.vidaMax = 0
                        }


                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    placeHolder = { Text(text = context.getString(R.string.new_character_health_points), color = textColor()) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = backgroundColor(),
                        unfocusedContainerColor = backgroundColor(),
                        focusedTextColor = textColor(),
                        unfocusedTextColor = textColorAccent()
                    )
                )
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CustomTextField(
                    value = mana.value,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(3.dp))
                        .border(2.dp, discordBlue),
                    onValueChange =  {
                        mana.value = it
                        try{
                            newCharacter.value.mana = mana.value.toInt()
                            newCharacter.value.manaMax = mana.value.toInt()

                        }catch (e:Exception){
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

            Row(modifier = Modifier
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
                    onValueChange =  {
                        maxSpell.value = it
                        try{
                            newCharacter.value.maxSpell = maxSpell.value.toInt()
                        }catch (e:Exception){
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

            Row(modifier = Modifier
                .fillMaxWidth(),
                //.padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val buttonsEnabled = name.value.isNotEmpty()
                Button(onClick = { characterPicker.launch("image/*") }, modifier = Modifier.weight(0.5f).padding(horizontal = 8.dp),
                    enabled =buttonsEnabled,
                    colors = ButtonDefaults.buttonColors(containerColor= discordBlue)) {
                    Text(
                        text = context.getString(R.string.new_character_file).uppercase(),
                        color = textColor(),
                        fontSize = TextUnit(11f, TextUnitType.Sp)
                    )
                }

                Button(onClick = { homebrewPicker.launch("application/pdf") },modifier = Modifier.weight(0.5f),
                    enabled = buttonsEnabled,
                    colors = ButtonDefaults.buttonColors(containerColor= discordBlue)) {
                    Text(
                        text = context.getString(R.string.new_character_homebrew).uppercase(),
                        color = textColor(),
                        fontSize = TextUnit(11f, TextUnitType.Sp)
                    )
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ){
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = discordBlue),
                    onClick = {
                        newCharacter.value.apply{
                            this.name = name.value
                            onDismissRequest(this)
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


private fun saveHomeBrew(context: Context, name:String, uri:Uri): String {
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
