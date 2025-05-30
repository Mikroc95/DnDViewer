package com.mikroc.dndviewer.bbdd.repository.spell

import com.mikroc.dndviewer.models.SpellModel
import kotlinx.coroutines.flow.Flow

interface SpellRepository {
    fun getSpells(characterCode: Int): Flow<List<SpellModel>>
    suspend fun getSpellsDirectly(id: Int): List<SpellModel>
    suspend fun insertSpell(spell: SpellModel)
    suspend fun updateSpell(spell: SpellModel)
    suspend fun deleteSpell(spell: SpellModel)
}
