package com.Mikroc.DnDViewer.Components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.Mikroc.DnDViewer.Theme.discordDarkBlack
import com.Mikroc.DnDViewer.Theme.discordLigthBlack
import com.Mikroc.DnDViewer.Theme.discordLigthGray
import com.Mikroc.DnDViewer.Theme.textColor

@Composable
fun ExpandableBox(
    title: String,
    icon: Painter? = null,
    onIconClicked: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val isRowExpanded = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(3.dp))
            .border(6.dp, discordLigthGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(discordDarkBlack)
                .padding(horizontal = 8.dp)
                .clickable {
                    isRowExpanded.value = !isRowExpanded.value
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(0.6f)
            ) {
                Text(text = title, modifier = Modifier.padding(16.dp), color = textColor())
                if (icon != null) {
                    Icon(painter = icon, contentDescription = "", modifier = Modifier.clickable {
                        isRowExpanded.value = false
                        onIconClicked()
                    })
                }
            }

            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.weight(0.2f)) {
                val iconExpanded = if (isRowExpanded.value) {
                    Icons.Filled.KeyboardArrowUp
                } else {
                    Icons.Filled.KeyboardArrowDown
                }
                Image(
                    imageVector = iconExpanded,
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(textColor()),
                    modifier = Modifier.clickable {
                        isRowExpanded.value = !isRowExpanded.value
                    }
                )
            }
        }
        val contentModifier = if (isRowExpanded.value) {
            Modifier
                .background(discordLigthBlack)
                .fillMaxWidth()
        } else {
            Modifier
                .background(discordLigthBlack)
                .height(0.dp)
                .fillMaxWidth()
        }
        Box(
            modifier = Modifier
                .background(discordLigthBlack)
                .animateContentSize()
                .then(contentModifier)
        ) {
            if (isRowExpanded.value) {
                content()
            }
        }
    }
}

@Preview
@Composable
private fun ExpandableBoxPreview() {
    ExpandableBox(title = "Mocked", onIconClicked = {}) {

    }
}