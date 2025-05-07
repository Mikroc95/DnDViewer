package com.Mikroc.DnDViewer.Utils


fun String.stringToInt(): Int {
    return if (this.isEmpty()) {
        0
    } else {
        this.toIntOrNull() ?: 0
    }
}
