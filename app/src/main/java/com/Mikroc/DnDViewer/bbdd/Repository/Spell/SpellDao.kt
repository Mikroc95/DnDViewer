package com.Mikroc.DnDViewer.bbdd.Repository.Spell

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.Mikroc.DnDViewer.bbdd.MyBBDD
import com.Mikroc.DnDViewer.Models.SpellModel
import kotlinx.coroutines.flow.Flow

@Dao
interface SpellDao {
    @Query("SELECT * FROM ${MyBBDD.Spells.TABLE_NAME}")
    fun getSpells(): Flow<MutableList<SpellModel>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun insertSpell(spell: SpellModel)

    @Update(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun UpdateSpell(spell: SpellModel)

    @Delete
    fun deleteSpell(spell: SpellModel)
}