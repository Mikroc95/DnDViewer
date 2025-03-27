package com.example.dndviewer.Components

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun FiveEView() {
    var webView: WebView? by remember { mutableStateOf(null) }
    val url = "https://5e.tools/"
    BackHandler(enabled = true) {
        webView?.goBack()
    }
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                loadUrl(url)
                webView = this
                settings.javaScriptEnabled = true

            }
        }
    )
    LaunchedEffect(Unit) {
        snapshotFlow { webView?.canGoBack() }
            .collect { _ ->
                webView?.loadUrl(url)
            }
    }
}