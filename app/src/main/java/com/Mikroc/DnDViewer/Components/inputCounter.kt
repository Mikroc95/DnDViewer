package com.Mikroc.DnDViewer.Components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Mikroc.DnDViewer.Theme.backgroundColor
import com.Mikroc.DnDViewer.Theme.textColor
import com.Mikroc.DnDViewer.R

@Composable
fun InputCounter(
    totalResult: String,
    borderColor: Color,
    labelColor: Color = textColor(),
    inputPadding: PaddingValues = PaddingValues(6.dp),
    valueTextField: String,
    onTextFieldValueChange: (String) -> Unit,
    onKeyBoardDone: () -> Unit,
    onLessClicked: () -> Unit,
    onPlusClicked: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text(
            text = totalResult,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W600,
            fontSize = 12.sp,
            color = labelColor
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.remove),
                    tint = Color.White,
                    contentDescription = "",
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(borderColor)
                        .clickable {
                            onLessClicked()
                        }
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                CustomTextField(
                    value = valueTextField,
                    onValueChange = {
                        onTextFieldValueChange(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(3.dp))
                        .border(2.dp, borderColor),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                    inputPadding = inputPadding,
                    singleLine = true,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onKeyBoardDone()
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        cursorColor = textColor(),
                        focusedContainerColor = backgroundColor(),
                        unfocusedContainerColor = backgroundColor(),
                        focusedIndicatorColor = textColor()

                    ),
                    textStyle = LocalTextStyle.current.copy(color = textColor()),
                )
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    tint = Color.White,
                    contentDescription = "",
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(borderColor)
                        .clickable {
                            onPlusClicked()
                        })
            }
        }
    }


}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun InputCounterPreview() {
    Row(modifier = Modifier.background(Color.Black)) {
        InputCounter(
            totalResult = "10",
            borderColor = Color.Red,
            valueTextField ="",
            onTextFieldValueChange = {},
            onKeyBoardDone = { },
            onPlusClicked = { },
            onLessClicked = { }
        )
    }

}