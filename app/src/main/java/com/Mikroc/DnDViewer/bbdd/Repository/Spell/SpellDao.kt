package com.mikroc.dndviewer.bbdd.repository.spell

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mikroc.dndviewer.bbdd.MyBbDd
import com.mikroc.dndviewer.models.SpellModel
import kotlinx.coroutines.flow.Flow

@Dao
interface SpellDao {
    @Query("SELECT * FROM ${MyBbDd.Spells.TABLE_NAME} WHERE character = :characterCode")
    fun getSpells(characterCode: Int): Flow<List<SpellModel>>

    @Query("SELECT * FROM ${MyBbDd.Spells.TABLE_NAME} WHERE character = :id")
    fun getSpellsDirectly(id: Int): List<SpellModel>


    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun insertSpell(spell: SpellModel)

    @Update(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun updateSpell(spell: SpellModel)

    @Delete
    fun deleteSpell(spell: SpellModel)
}