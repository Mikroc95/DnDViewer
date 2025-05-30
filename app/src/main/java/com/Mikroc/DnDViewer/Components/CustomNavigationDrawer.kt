package com.mikroc.dndviewer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mikroc.dndviewer.R
import com.mikroc.dndviewer.models.CharacterModel
import com.mikroc.dndviewer.theme.backgroundColor
import com.mikroc.dndviewer.theme.discordBlue
import com.mikroc.dndviewer.theme.textColor
import com.mikroc.dndviewer.theme.topBarColor
import com.mikroc.dndviewer.theme.topBarTextColor
import com.mikroc.dndviewer.theme.transparent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


private lateinit var drawerState: DrawerState
private lateinit var coroutineScope: CoroutineScope
private lateinit var title: MutableState<String>
private lateinit var listCharacters: List<CharacterModel>


@Composable
fun CustomNavigationDrawer(
    topBarTitle: MutableState<String>,
    topBarIcon: Painter? = null,
    characters: List<CharacterModel>,
    onCharacterSelected: (CharacterModel) -> Unit,
    onNewCharacterClicked: () -> Unit,
    onUpdateCharacter: (CharacterModel) -> Unit,
    onDeleteCharacter: () -> Unit,
    content: @Composable () -> Unit,
) {
    title = topBarTitle
    listCharacters = characters
    drawerState = rememberDrawerState(initialValue = androidx.compose.material3.DrawerValue.Closed)
    coroutineScope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerContent = {
            GetNavigationDrawer(
                onCharacterSelected = onCharacterSelected,
                onNewCharacterClicked = onNewCharacterClicked,
                onUpdateCharacter = onUpdateCharacter,
            )
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                GetTopBar(
                    icon = topBarIcon,
                    onDeleteCharacter = onDeleteCharacter
                )
            },
            contentColor = transparent,
            containerColor = backgroundColor()
        ) {
            Column(Modifier.padding(it)) {
                content()
            }
        }
    }
}


@Composable
fun GetNavigationDrawer(
    onCharacterSelected: (CharacterModel) -> Unit,
    onNewCharacterClicked: () -> Unit,
    onUpdateCharacter: (CharacterModel) -> Unit,
) {
    val configuration = LocalConfiguration.current
    var screenWidth = configuration.screenWidthDp.dp / 2
    screenWidth += 30.dp
    ModalDrawerSheet(
        modifier = Modifier.width(screenWidth),
        drawerContainerColor = topBarColor()
    ) {
        Column(
            modifier = Modifier
                .width(screenWidth)
                .fillMaxHeight()
                .padding(horizontal = 16.dp, vertical = 26.dp)
                .verticalScroll(rememberScrollState())
        ) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            listCharacters.forEach {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.8f)
                            .clickable {
                                coroutineScope.launch {
                                    onDrawerClicked()
                                    onCharacterSelected(it)
                                }
                            }) {
                        Text(
                            it.name,
                            modifier = Modifier.fillMaxWidth(),
                            color = topBarTextColor()
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.2f)
                            .clickable {
                                onUpdateCharacter(it)
                            }) {
                        Image(
                            imageVector = Icons.Outlined.Edit,
                            colorFilter = ColorFilter.tint(textColor()),
                            contentDescription = "",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {

                Button(
                    onClick = {
                        coroutineScope.launch {
                            onDrawerClicked()
                            onNewCharacterClicked()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = discordBlue)
                ) {
                    Text(
                        text = LocalContext.current.getString(R.string.create_character),
                        color = textColor(),
                        textAlign = TextAlign.Center
                    )
                }
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetTopBar(icon: Painter?, onDeleteCharacter: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = topBarColor()
        ),
        title = {
            Text(
                text = title.value,
                textAlign = TextAlign.Center,
                color = topBarTextColor(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = @Composable {
            if (title.value != LocalContext.current.getString(R.string.app_name)) {
                IconButton(onClick = {
                    onDeleteCharacter()
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = null, tint = textColor())
                }
            }
        },

        navigationIcon = {
            if (icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = "",
                    tint = topBarTextColor(),
                    modifier = Modifier.clickable {
                        onDrawerClicked()
                    }
                )
            }
        }
    )
}


fun onDrawerClicked() {
    coroutineScope.launch {
        if (drawerState.isClosed) drawerState.open() else drawerState.close()
    }
}