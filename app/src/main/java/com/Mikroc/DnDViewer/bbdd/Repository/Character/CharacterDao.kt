package com.Mikroc.DnDViewer.bbdd.Repository.Character

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.Mikroc.DnDViewer.Models.CharacterModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM personatge")
    fun getCharactersFlow(): Flow<List<CharacterModel>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    fun insertCharacter(character: CharacterModel): Long

    @Update
    fun updateCharacter(character: CharacterModel)

    @Delete
    fun deleteCharacter(character: CharacterModel)


}