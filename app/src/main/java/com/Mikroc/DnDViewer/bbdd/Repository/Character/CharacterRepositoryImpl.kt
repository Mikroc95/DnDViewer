package com.mikroc.dndviewer.bbdd.repository.character

import com.mikroc.dndviewer.models.CharacterModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepositoryImpl @Inject constructor(
    private val characterDao: CharacterDao
) : CharacterRepository {
    override fun getAllCharactersFlow(): Flow<List<CharacterModel>> {
        return characterDao.getCharactersFlow()
    }

    override fun getCharacterByIdFlow(id: Int): Flow<CharacterModel?> {
        return characterDao.getCharactersFlow().map { characters ->
            characters.find { it.code == id }
        }
    }

    override suspend fun getCharacterById(id: Int): CharacterModel? {

        val charactersList = characterDao.getCharactersFlow().first()
        return charactersList.find { it.code == id }
    }

    override suspend fun insertCharacter(character: CharacterModel): Long {
        return characterDao.insertCharacter(character = character)
    }

    override suspend fun updateCharacter(character: CharacterModel): Int {
        return characterDao.updateCharacter(character = character)
    }

    override suspend fun deleteCharacter(character: CharacterModel): Int {
        return characterDao.deleteCharacter(character = character)
    }
}


