package com.example.dndviewer.Models

data class ItemsModel(
    var name:String="",
    var description:String="",
    var charges:String="",
    var actualCharges:String = "",
    var isEquiped:Boolean=false,
    var isConsumible:Boolean=false,
    var character:String = ""
)
