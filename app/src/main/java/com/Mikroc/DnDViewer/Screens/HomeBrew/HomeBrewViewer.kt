package com.Mikroc.DnDViewer.Screens.HomeBrew


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.Mikroc.DnDViewer.Screens.EmptySelection
import io.github.veselyjan92.pdfviewer.PDFViewer
import io.github.veselyjan92.pdfviewer.rememberPDFViewerPdfiumState
import java.io.File

@Composable
fun HomeBrewViewer(
    homebrew: File,
    modifier: Modifier = Modifier
) {
    if (homebrew.exists()) {
        val pdf = rememberPDFViewerPdfiumState(homebrew)
        PDFViewer(
            modifier = modifier.fillMaxSize(),
            state = pdf,
        )
    } else {
        EmptySelection()
    }
}