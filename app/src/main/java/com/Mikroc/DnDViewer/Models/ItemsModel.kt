package com.Mikroc.DnDViewer.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.Mikroc.DnDViewer.bbdd.MyBBDD

@Entity(tableName = MyBBDD.Objectes.TABLE_NAME)
data class ItemsModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String = "",
    var description: String = "",
    var charges: String = "",
    var actualCharges: String = "",
    var isEquiped: Boolean = false,
    var isConsumible: Boolean = false,
    var character: Int = -1
)
