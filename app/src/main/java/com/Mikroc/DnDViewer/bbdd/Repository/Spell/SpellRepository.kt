package com.Mikroc.DnDViewer.bbdd.Repository.Spell

import com.Mikroc.DnDViewer.Models.SpellModel
import kotlinx.coroutines.flow.Flow

interface SpellRepository {
    fun getSpells(characterCode: Int): Flow<List<SpellModel>>
    suspend fun getSpellsDirectly(id: Int): List<SpellModel>
    suspend fun insertSpell(spell: SpellModel)
    suspend fun updateSpell(spell: SpellModel)
    suspend fun deleteSpell(spell: SpellModel)
}
