package com.Mikroc.DnDViewer.Models

data class ItemsModel(
    var id:Int =0,
    var name:String="",
    var description:String="",
    var charges:String="",
    var actualCharges:String = "",
    var isEquiped:Boolean=false,
    var isConsumible:Boolean=false,
    var character:String = ""
)
