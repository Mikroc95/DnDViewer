package com.mikroc.dndviewer.bbdd.repository.character

import com.mikroc.dndviewer.models.CharacterModel
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getAllCharactersFlow(): Flow<List<CharacterModel>>
    fun getCharacterByIdFlow(id: Int): Flow<CharacterModel?>
    suspend fun getCharacterById(id: Int): CharacterModel?
    suspend fun insertCharacter(character: CharacterModel): Long
    suspend fun updateCharacter(character: CharacterModel): Int
    suspend fun deleteCharacter(character: CharacterModel): Int
}