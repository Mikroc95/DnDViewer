package com.example.dndviewer.Models

import android.net.Uri

data class CharacterModel(
    var name:String="",
    var image_character:ByteArray = byteArrayOf(),
    var homebrewRoute:String = "",
    var vida:Int=0,
    var vidaMax:Int =0,
    var mana:Int = 0,
    var manaMax:Int =0,
    var observations:String="",
    var maxSpell:Int =0
)
/* MyBBDD.personatge.COLUMN_NAME_VIDA,
            MyBBDD.personatge.COLUMN_NAME_VIDA_MAX,
            MyBBDD.personatge.COLUMN_NAME_MANA,
            MyBBDD.personatge.COLUMN_NAME_MANA_MAX,
            MyBBDD.personatge.COLUMN_NAME_OBS*/