package com.Mikroc.DnDViewer.bbdd.Repository.Spell

import com.Mikroc.DnDViewer.Models.SpellModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpellRepositoryImpl @Inject constructor(
    private val spellDao: SpellDao
) : SpellRepository {
    override fun getSpells(characterCode: Int): Flow<MutableList<SpellModel>> {
        return spellDao.getSpells()
    }

    override fun insertSpell(spell: SpellModel) {
        spellDao.insertSpell(spell = spell)
    }

    override fun updateSpell(spell: SpellModel) {
        spellDao.UpdateSpell(spell = spell)
    }

    override fun deleteSpell(spell: SpellModel) {
        spellDao.deleteSpell(spell = spell)
    }
}