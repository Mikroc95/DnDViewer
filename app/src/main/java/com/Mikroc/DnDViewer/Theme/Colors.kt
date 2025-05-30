package com.mikroc.dndviewer.theme


import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val white = Color(0xfff9f9f9)
val transparent = Color(0XFFFFFFFF)

val discordBlue = Color(0XFF7289da)
val discordLightGray = Color(0XFF424549)
val discordDarkGray = Color(0XFF36393e)
val discordLightBlack = Color(0XFF282b30)
val discordDarkBlack = Color(0XFF1e2124)
val discordOrangeAccent = Color(0XFFf28f0c)
val discordRed = Color(0XFFed5555)
val blueMana = Color(0XFF5eaeeb)
val yellowMetaMagic = Color(0XFFEEDD82)


@Composable
fun topBarColor(): Color = discordDarkGray

@Composable
fun topBarTextColor(): Color = transparent

@Composable
fun textColor(): Color = white

@Composable
fun textColorAccent(): Color = discordBlue

@Composable
fun backgroundColor(): Color = discordLightGray
