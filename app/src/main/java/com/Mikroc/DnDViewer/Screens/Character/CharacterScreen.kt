package com.Mikroc.DnDViewer.Screens.Character

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.drawable.toBitmap
import com.Mikroc.DnDViewer.Components.CustomHpManaBar
import com.Mikroc.DnDViewer.Models.CharacterModel
import com.Mikroc.DnDViewer.R
import com.Mikroc.DnDViewer.ViewModels.MainViewModel

@Composable
fun CharacterScreen(characterModel: CharacterModel,viewModel:MainViewModel) {
    var scaleCharacters by remember { mutableFloatStateOf(1f) }
    var offsetCharacters by remember { mutableStateOf(Offset(0f, 0f)) }
    if (characterModel.vidaMax > 0 || characterModel.manaMax > 0) {
        CustomHpManaBar(characterModel = characterModel, viewModel = viewModel)
    }

    if (!characterModel.imageCharacter.contentEquals(byteArrayOf())) {
        val bitmap = try{
            characterModel.imageCharacter.toBitmap()
        }catch (e:Exception){
            e.printStackTrace()
            LocalContext.current.getDrawable(R.drawable.empty)?.toBitmap()
        }
        val scrollImage = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxSize()
                .scrollable(scrollImage, orientation = Orientation.Vertical)
        ) {
            Image(
                bitmap = bitmap?.asImageBitmap()!!,
                contentDescription = null,
                modifier = Modifier
                    .clipToBounds()
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        this.detectTransformGestures { _, pan, zoom, _ ->
                            // Update the scale based on zoom gestures.
                            scaleCharacters *= zoom

                            // Limit the zoom levels within a certain range (optional).
                            scaleCharacters = scaleCharacters.coerceIn(0.5f, 10f)

                            // Update the offset to implement panning when zoomed.
                            offsetCharacters = if (scaleCharacters == 1f) Offset(
                                0f,
                                0f
                            ) else offsetCharacters + pan
                        }
                    }
                    .graphicsLayer(
                        scaleX = scaleCharacters, scaleY = scaleCharacters,
                        translationX = offsetCharacters.x, translationY = offsetCharacters.y
                    )
            )
        }
    }
}

fun ByteArray.toBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}

@Preview
@Composable
private fun CharacterScreenPreview(){

    val img = byteArrayOf(-1, -40, -1, -31, 1, 122, 69, 120, 105, 102, 0, 0, 77, 77, 0, 42, 0, 0, 0,
        8, 0, 6, 1, 0, 0, 3, 0, 0, 0, 1, 4, 56, 0, 0, 1, 1, 0, 3, 0, 0, 0, 1, 9, 36, 0, 0, 1, 49, 0,
        2, 0, 0, 0, 39, 0, 0, 0, 86, -121, 105, 0, 4, 0, 0, 0, 1, 0, 0, 0, -111, 1, 18, 0, 3, 0, 0,
        0, 1, 0, 1, 0, 0, 1, 50, 0, 2, 0, 0, 0, 20, 0, 0, 0, 125, 0, 0, 0, 0, 65, 110)
    CharacterScreen(
        characterModel = CharacterModel(vidaMax = 10, manaMax = 10, imageCharacter = img),
        viewModel = MainViewModel()
    )
}