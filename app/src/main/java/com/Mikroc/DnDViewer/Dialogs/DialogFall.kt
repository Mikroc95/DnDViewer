package com.Mikroc.DnDViewer.Dialogs

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.Mikroc.DnDViewer.bbdd.Repository.Database.FakeCharacterRepository
import com.Mikroc.DnDViewer.bbdd.Repository.Database.FakeItemsRepository
import com.Mikroc.DnDViewer.bbdd.Repository.Database.FakeSpellRepository
import com.Mikroc.DnDViewer.R
import com.Mikroc.DnDViewer.Theme.backgroundColor
import com.Mikroc.DnDViewer.Theme.textColor
import com.Mikroc.DnDViewer.ViewModels.MainViewModel

@Composable
fun DialogFall(fallen: MutableState<Boolean>, viewModel: MainViewModel) {
    //0 = nothing
    //1 = fail
    //2 = good
    val listMatches = remember {
        mutableStateListOf<Int>()
    }
    val context = LocalContext.current
    Dialog(onDismissRequest = { }) {
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

                Text(
                    text = context.getString(R.string.Dialog_fall_title),
                    fontSize = TextUnit(15f, TextUnitType.Sp),
                    color = textColor()
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = context.getString(R.string.Dialog_fall_description),
                    fontSize = TextUnit(12f, TextUnitType.Sp),
                    color = textColor()
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.Start
            ) {

                if (listMatches.isNotEmpty()) {
                    listMatches.forEach {
                        val source = if (it == 1) R.drawable.skull else R.drawable.health
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Image(
                                painter = painterResource(source),
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 8.dp)
                            )
                        }
                    }

                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        handleThrow(
                            result = 2,
                            listMatches = listMatches,
                            fallen = fallen,
                            viewModel = viewModel,
                            context = context
                        )

                    },
                ) {
                    Image(
                        painter = painterResource(R.drawable.health),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                IconButton(
                    onClick = {
                        handleThrow(
                            result = 1,
                            listMatches = listMatches,
                            fallen = fallen,
                            viewModel = viewModel,
                            context = context
                        )

                    },
                ) {
                    Image(
                        painter = painterResource(R.drawable.skull),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

        }
    }
}

private fun handleThrow(
    result: Int,
    listMatches: SnapshotStateList<Int>,
    fallen: MutableState<Boolean>,
    viewModel: MainViewModel,
    context: Context
) {
    listMatches.add(element = result)
    if (isDead(listMatches)) {
        fallen.value = false
        Toast.makeText(
            context,
            "ha mort ${viewModel.selectedCharacter.value.name}",
            Toast.LENGTH_SHORT
        ).show()
        viewModel.characterHasDied()
    }
    if (isAlive(list = listMatches)) {
        fallen.value = false
        Toast.makeText(
            context,
            "s'ha aixecat ${viewModel.selectedCharacter.value.name}",
            Toast.LENGTH_SHORT
        ).show()
        viewModel.characterStabilized()
    }
}

private fun isDead(list: MutableList<Int>): Boolean {
    val dead = list.filter { it == 1 }.toList()
    return dead.size >= 3
}

private fun isAlive(list: MutableList<Int>): Boolean {
    val alive = list.filter { it == 2 }.toList()
    return alive.size >= 3
}

@Preview
@Composable
fun DialogFallPreview() {
    val remember = remember { mutableStateOf(true) }
    DialogFall(
        remember, viewModel = MainViewModel(
            itemRepository = FakeItemsRepository(),
            characterRepository = FakeCharacterRepository(),
            spellRepository = FakeSpellRepository()
        )
    )
}