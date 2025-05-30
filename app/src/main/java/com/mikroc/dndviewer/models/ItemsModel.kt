package com.mikroc.dndviewer.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mikroc.dndviewer.bbdd.MyBbDd

@Entity(tableName = MyBbDd.Objectes.TABLE_NAME)
data class ItemsModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String = "",
    var description: String = "",
    var charges: String = "",
    var actualCharges: String = "",
    var isEquipped: Boolean = false,
    var isConsumable: Boolean = false,
    var character: Int = -1
)
