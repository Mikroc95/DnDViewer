package com.Mikroc.DnDViewer.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.Mikroc.DnDViewer.bbdd.MyBBDD

@Entity(tableName = MyBBDD.Spells.TABLE_NAME)
data class SpellModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String = "",
    var level: String = "",
    var character: Int = -1
)