package com.mikroc.dndviewer.bbdd.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mikroc.dndviewer.bbdd.repository.character.CharacterDao
import com.mikroc.dndviewer.bbdd.repository.character.CharacterRepository
import com.mikroc.dndviewer.bbdd.repository.items.ItemsDao
import com.mikroc.dndviewer.bbdd.repository.items.ItemsRepository
import com.mikroc.dndviewer.bbdd.repository.spell.SpellDao
import com.mikroc.dndviewer.bbdd.repository.spell.SpellRepository
import com.mikroc.dndviewer.models.CharacterModel
import com.mikroc.dndviewer.models.ItemsModel
import com.mikroc.dndviewer.models.SpellModel
import kotlinx.coroutines.flow.Flow

@Database(
    entities = [
        CharacterModel::class,
        SpellModel::class,
        ItemsModel::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun spellDao(): SpellDao
    abstract fun magicItemDao(): ItemsDao

}

class FakeCharacterRepository : CharacterRepository {

    override fun getAllCharactersFlow(): Flow<List<CharacterModel>> {
        TODO("Not yet implemented")
    }

    override fun getCharacterByIdFlow(id: Int): Flow<CharacterModel?> {
        TODO("Not yet implemented")
    }

    override suspend fun getCharacterById(id: Int): CharacterModel? {
        TODO("Not yet implemented")
    }

    override suspend fun insertCharacter(character: CharacterModel): Long {
        TODO("Not yet implemented")
    }


    override suspend fun updateCharacter(character: CharacterModel): Int {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCharacter(character: CharacterModel): Int {
        TODO("Not yet implemented")
    }


}

class FakeItemsRepository : ItemsRepository {
    override suspend fun getItems(characterCode: Int): Flow<MutableList<ItemsModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun getConsumables(characterCode: Int): Flow<MutableList<ItemsModel>> {
        TODO("Not yet implemented")
    }


    override fun insertItem(item: ItemsModel) {
        TODO("Not yet implemented")
    }

    override fun updateItem(item: ItemsModel) {
        TODO("Not yet implemented")
    }

    override fun deleteItem(item: ItemsModel) {
        TODO("Not yet implemented")
    }

}

class FakeSpellRepository : SpellRepository {
    override fun getSpells(characterCode: Int): Flow<MutableList<SpellModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSpellsDirectly(id: Int): List<SpellModel> {
        TODO("Not yet implemented")
    }

    override suspend fun insertSpell(spell: SpellModel) {
        TODO("Not yet implemented")
    }

    override suspend fun updateSpell(spell: SpellModel) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSpell(spell: SpellModel) {
        TODO("Not yet implemented")
    }

}
