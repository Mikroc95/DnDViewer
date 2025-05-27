package com.Mikroc.DnDViewer.bbdd.Repository.Character

import com.Mikroc.DnDViewer.Models.CharacterModel
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
        // Mapea el flujo de listas para encontrar y emitir el personaje específico o null
        return characterDao.getCharactersFlow().map { characters ->
            characters.find { it.code == id } // Asegúrate que 'code' es el campo correcto
        }
    }

    // Implementación suspendida si aún la necesitas
    override suspend fun getCharacterById(id: Int): CharacterModel? {
        // Esta versión toma la primera emisión de la lista y busca en ella.
        // Es menos reactiva que getCharacterByIdFlow.
        val charactersList = characterDao.getCharactersFlow().first()
        return charactersList.find { it.code == id } // Asegúrate que 'code' es el campo correcto
    }

    override suspend fun insertCharacter(character: CharacterModel): Long {
        return characterDao.insertCharacter(character = character)
    }

    override suspend fun updateCharacter(character: CharacterModel) {
        characterDao.updateCharacter(character = character)
    }

    override suspend fun deleteCharacter(character: CharacterModel) {
        characterDao.deleteCharacter(character = character)
    }
}


