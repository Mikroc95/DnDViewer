package com.Mikroc.DnDViewer.bbdd.Repository.Character

import com.Mikroc.DnDViewer.Models.CharacterModel
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getAllCharactersFlow(): Flow<List<CharacterModel>>
    fun getCharacterByIdFlow(id: Int): Flow<CharacterModel?>
    suspend fun getCharacterById(id: Int): CharacterModel?
    suspend fun insertCharacter(character: CharacterModel): Long
    suspend fun updateCharacter(character: CharacterModel)
    suspend fun deleteCharacter(character: CharacterModel)
}