package com.mikroc.dndviewer.dialogs

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mikroc.dndviewer.components.CustomTextField
import com.mikroc.dndviewer.models.ItemsModel
import com.mikroc.dndviewer.theme.backgroundColor
import com.mikroc.dndviewer.theme.discordBlue
import com.mikroc.dndviewer.theme.discordDarkBlack
import com.mikroc.dndviewer.theme.textColor
import com.mikroc.dndviewer.R


@Composable
fun DialogNewItem(
    characterCode: Int,
    onDismissRequest: (ItemsModel) -> Unit,
    onClose: () -> Unit,
    isConsumable: Boolean = false,
    editing: ItemsModel = ItemsModel()
) {
    val context = LocalContext.current
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
                            text = if (isConsumable) context.getString(R.string.new_item_quantity)
                            else context.getString(R.string.new_item_charges),
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
                    .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = discordBlue),
                    onClick = {
                        try {
                            if (name.value.isNotEmpty() && description.value.isNotEmpty()) {
                                val charge = checkInteger(charges.value, isConsumable)
                                if (charge.toInt() <= 0 && isConsumable) {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.new_item_warning_add_quantity),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    if (editing.name.isEmpty()) {
                                        onDismissRequest(
                                            ItemsModel(
                                                name = name.value,
                                                description = description.value,
                                                charges = charge,
                                                actualCharges = charge,
                                                isEquipped = false,
                                                isConsumable = isConsumable,
                                                character = characterCode
                                            )
                                        )
                                    } else {
                                        val actualCharges = if (editing.charges != charge) charge
                                        else editing.actualCharges
                                        onDismissRequest(
                                            ItemsModel(
                                                name = name.value,
                                                description = description.value,
                                                charges = charge,
                                                id = editing.id,
                                                isEquipped = editing.isEquipped,
                                                actualCharges = actualCharges,
                                                isConsumable = editing.isConsumable,
                                                character = characterCode
                                            )
                                        )
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.dialog_error_empty_fields),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
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

private fun checkInteger(value: String, isConsumable: Boolean): String {
    try {
        val integer: Int = value.toInt()
        return integer.toString()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return if (isConsumable) "-1" else "0"
}

@Composable
@Preview
fun DialogNewItemPreview() {
    DialogNewItem(
        characterCode = 0,
        onDismissRequest = {},
        onClose = { },
    )
}