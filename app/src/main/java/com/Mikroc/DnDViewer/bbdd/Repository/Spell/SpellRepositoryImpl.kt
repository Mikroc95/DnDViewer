package com.mikroc.dndviewer.bbdd.repository.spell

import com.mikroc.dndviewer.models.SpellModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpellRepositoryImpl @Inject constructor(
    private val spellDao: SpellDao
) : SpellRepository {
    override fun getSpells(characterCode: Int): Flow<List<SpellModel>> {
        return spellDao.getSpells(characterCode = characterCode)
    }

    override suspend fun getSpellsDirectly(id: Int): List<SpellModel> {
        return spellDao.getSpellsDirectly(id = id)
    }

    override suspend fun insertSpell(spell: SpellModel) {
        spellDao.insertSpell(spell = spell)
    }

    override suspend fun updateSpell(spell: SpellModel) {
        spellDao.updateSpell(spell = spell)
    }

    override suspend fun deleteSpell(spell: SpellModel) {
        spellDao.deleteSpell(spell = spell)
    }
}