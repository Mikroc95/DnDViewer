package com.mikroc.dndviewer.bbdd.repository.character

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mikroc.dndviewer.models.CharacterModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM personatge")
    fun getCharactersFlow(): Flow<List<CharacterModel>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCharacter(character: CharacterModel): Long

    @Update
    suspend fun updateCharacter(character: CharacterModel): Int

    @Delete
    suspend fun deleteCharacter(character: CharacterModel): Int
}