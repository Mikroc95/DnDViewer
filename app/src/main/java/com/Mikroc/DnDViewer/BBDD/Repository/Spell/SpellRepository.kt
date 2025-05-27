package com.Mikroc.DnDViewer.BBDD.Repository.Spell

import com.Mikroc.DnDViewer.Models.SpellModel
import kotlinx.coroutines.flow.Flow

interface SpellRepository {
    fun getSpells(characterCode: Int): Flow<MutableList<SpellModel>>
    fun insertSpell(spell: SpellModel)
    fun updateSpell(spell: SpellModel)
    fun deleteSpell(spell: SpellModel)
}
