package com.mikroc.dndviewer.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mikroc.dndviewer.bbdd.MyBbDd

@Entity(tableName = MyBbDd.Spells.TABLE_NAME)
data class SpellModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String = "",
    var level: String = "",
    var character: Int = -1
)