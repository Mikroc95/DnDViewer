package com.example.dndviewer.Theme


import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val ligthTopBar = Color(0xFF02134d)
val darkTopBar = Color(0xFF010617)

val white = Color(0xfff9f9f9)
val dark = Color(0XFF3F3C3A)
val grey = Color(0XFFC0C0C0)
val accentDark = Color(0XFF99310b)
val accentLigth = Color(0XFFc94210)
val accentDarkDisabled = Color(0XFF99310b)
val accentLigthDisabled = Color(0XFFc94210)
val transparent = Color(0XFFFFFFFFFF)

//Proba haver k tal
val discordBlue = Color(0XFF7289da)
val discordLigthGray = Color(0XFF424549)
val discordDarkGray = Color(0XFF36393e)
val discordLigthBlack = Color(0XFF282b30)
val discordDarkBlack = Color(0XFF1e2124)
val discordOrangeAccent = Color(0XFFf28f0c)
val discordRed = Color(0XFFed5555)
val blueMana = Color(0XFF5eaeeb)
val blueTransparent = Color(0XFF6CB4EE)

@Composable
fun topBarColor(): Color  = discordDarkGray
@Composable
fun topBarTextColor():Color = transparent

@Composable
fun textColor():Color = white

@Composable
fun invertedTextColor():Color = dark

@Composable
fun textColorAccent():Color =  discordBlue

@Composable
fun backgroundColor():Color = discordLigthGray
