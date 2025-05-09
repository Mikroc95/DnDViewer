package com.Mikroc.DnDViewer.Screens.HomeBrew


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.veselyjan92.pdfviewer.PDFViewer
import io.github.veselyjan92.pdfviewer.rememberPDFViewerPdfiumState
import java.io.File

@Composable
fun HomeBrewViewer(
    homebrew: File,
    modifier: Modifier = Modifier
) {
    val pdf = rememberPDFViewerPdfiumState(homebrew)
    PDFViewer(
        modifier = modifier.fillMaxSize(),
        state = pdf,
    )

}