package com.Mikroc.DnDViewer.Dialogs

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.Mikroc.DnDViewer.Components.CustomTextField
import com.Mikroc.DnDViewer.Models.ItemsModel
import com.Mikroc.DnDViewer.Theme.backgroundColor
import com.Mikroc.DnDViewer.Theme.discordBlue
import com.Mikroc.DnDViewer.Theme.discordDarkBlack
import com.Mikroc.DnDViewer.Theme.textColor
import com.Mikroc.DnDViewer.R


@Composable
fun DialogNewItem(
    characterName: String,
    onDismissRequest: (ItemsModel) -> Unit,
    onClose: () -> Unit,
    context: Context,
    isConsumable:Boolean = false,
    editing:ItemsModel = ItemsModel()
) {
    val name = remember {
        mutableStateOf(editing.name)
    }
    val description = remember {
        mutableStateOf(editing.description)
    }
    val charges = remember {
        mutableStateOf(editing.charges)
    }
    Dialog(onDismissRequest = onClose) {
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
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                CustomTextField(
                    value = name.value,
                    onValueChange = {
                        name.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(discordDarkBlack)
                        .clip(RoundedCornerShape(3.dp))
                        .border(2.dp, discordBlue),
                    placeHolder = {
                        Text(
                            text = context.getString(R.string.new_character_name),
                            color = textColor()
                        )
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CustomTextField(
                    value = charges.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(discordDarkBlack)
                        .clip(RoundedCornerShape(3.dp))
                        .border(2.dp, discordBlue),
                    onValueChange = {
                        charges.value = it
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    placeHolder = {
                        Text(
                            text = if(isConsumable) context.getString(R.string.new_item_quantity)
                            else context.getString(R.string.new_item_charges),
                            color = textColor()
                        )
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp), horizontalArrangement = Arrangement.Center
            ) {
                CustomTextField(
                    value = description.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(165.dp)
                        .background(discordDarkBlack)
                        .clip(RoundedCornerShape(3.dp))
                        .border(2.dp, discordBlue)
                        .scrollable(
                            orientation = Orientation.Vertical,
                            state = rememberScrollState()
                        ),
                    onValueChange = {
                        description.value = it

                    },
                    singleLine = false,
                    placeHolder = {
                        Text(
                            text = context.getString(R.string.new_item_description),
                            color = textColor()
                        )
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp,end = 16.dp,top = 4.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = discordBlue),
                    onClick = {
                        if (name.value.isNotEmpty() && description.value.isNotEmpty()) {
                            val charge = checkInteger(charges.value,isConsumable)
                            if(charge.toInt()<=0 && isConsumable){
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.new_item_warning_add_quantity),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else{
                                onDismissRequest(
                                    ItemsModel(
                                        id = editing.id,
                                        name = name.value,
                                        description = description.value,
                                        charges = charge,
                                        actualCharges = charge,
                                        isEquiped = false,
                                        isConsumible = isConsumable,
                                        character = characterName
                                    )
                                )
                            }
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.dialog_error_empty_fields),
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

private fun checkInteger (value:String,isConsumable: Boolean):String{
    try{
        val integer:Int = value.toInt()
        return integer.toString()
    }catch (e:Exception){
        e.printStackTrace()
    }
    return if(isConsumable) "-1" else "0"
}