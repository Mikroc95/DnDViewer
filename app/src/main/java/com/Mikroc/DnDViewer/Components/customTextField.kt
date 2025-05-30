package com.mikroc.dndviewer.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikroc.dndviewer.theme.discordDarkBlack
import com.mikroc.dndviewer.theme.discordLightBlack
import com.mikroc.dndviewer.theme.textColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    inputPadding: PaddingValues = PaddingValues(6.dp),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    shape: Shape = TextFieldDefaults.shape,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    colors: TextFieldColors = TextFieldDefaults.colors(
        cursorColor = textColor(),
        focusedContainerColor = discordDarkBlack,
        unfocusedContainerColor = discordLightBlack,
        focusedIndicatorColor = Color.Transparent

    ),
    textStyle: TextStyle = LocalTextStyle.current.copy(color = textColor()),
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    placeHolder: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        enabled = enabled,
        singleLine = singleLine,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        cursorBrush = SolidColor(textColor())
    ) { innerTextField ->

        TextFieldDefaults.DecorationBox(
            value = value,
            colors = colors,
            visualTransformation = visualTransformation,
            innerTextField = innerTextField,
            singleLine = singleLine,
            enabled = enabled,
            interactionSource = interactionSource,
            contentPadding = inputPadding,
            trailingIcon = trailingIcon,
            placeholder = placeHolder,
            leadingIcon = leadingIcon,
            shape = shape,
        )
    }
}

@Preview
@Composable
private fun CustomTextFieldPreview() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        CustomTextField(value = "Mock", onValueChange = {})
    }

}
