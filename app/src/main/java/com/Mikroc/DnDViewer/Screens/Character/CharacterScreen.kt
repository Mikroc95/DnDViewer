package com.mikroc.dndviewer.screens.character


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mikroc.dndviewer.models.CharacterModel
import com.mikroc.dndviewer.R
import com.mikroc.dndviewer.theme.backgroundColor


@Composable
fun CharacterScreen(characterModel: CharacterModel, modifier: Modifier = Modifier) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    Box(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .background(backgroundColor())
            .pointerInput(Unit) {
                this.detectTransformGestures { _, pan, zoom, _ ->
                    // Update the scale based on zoom gestures.
                    scale *= zoom

                    // Limit the zoom levels within a certain range (optional).
                    scale = scale.coerceIn(0.5f, 10f)

                    // Update the offset to implement panning when zoomed.
                    offset = if (offset.x == 1f) Offset(
                        0f,
                        0f
                    ) else offset + pan
                }
            },
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(characterModel.imageCharacter.takeIf { it.isNotEmpty() })
                .crossfade(true)
                .placeholder(R.drawable.empty)
                .error(R.drawable.empty)
                .build(),
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale, scaleY = scale,
                    translationX = offset.x, translationY = offset.y
                ),
            contentDescription = LocalContext.current.getString(R.string.tab_personatge),
            contentScale = ContentScale.Fit
        )
    }
}

@Preview
@Composable
private fun CharacterScreenPreview() {
    CharacterScreen(
        characterModel = CharacterModel(),
    )
}