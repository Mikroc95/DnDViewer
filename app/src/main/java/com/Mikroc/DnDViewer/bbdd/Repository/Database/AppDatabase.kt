package com.Mikroc.DnDViewer.bbdd.Repository.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.Mikroc.DnDViewer.bbdd.Repository.Character.CharacterDao
import com.Mikroc.DnDViewer.bbdd.Repository.Character.CharacterRepository
import com.Mikroc.DnDViewer.bbdd.Repository.Items.ItemsDao
import com.Mikroc.DnDViewer.bbdd.Repository.Items.ItemsRepository
import com.Mikroc.DnDViewer.bbdd.Repository.Spell.SpellDao
import com.Mikroc.DnDViewer.bbdd.Repository.Spell.SpellRepository
import com.Mikroc.DnDViewer.Models.CharacterModel
import com.Mikroc.DnDViewer.Models.ItemsModel
import com.Mikroc.DnDViewer.Models.SpellModel
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
