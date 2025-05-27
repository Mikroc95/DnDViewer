package com.Mikroc.DnDViewer.BBDD.Repository.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.Mikroc.DnDViewer.BBDD.Repository.Character.CharacterDao
import com.Mikroc.DnDViewer.BBDD.Repository.Character.CharacterRepository
import com.Mikroc.DnDViewer.BBDD.Repository.Items.ItemsDao
import com.Mikroc.DnDViewer.BBDD.Repository.Items.ItemsRepository
import com.Mikroc.DnDViewer.BBDD.Repository.Spell.SpellDao
import com.Mikroc.DnDViewer.BBDD.Repository.Spell.SpellRepository
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

class FakeCharacterRepository :
    CharacterRepository { /* Implementación vacía o con datos de prueba */

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


    override suspend fun updateCharacter(character: CharacterModel) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCharacter(character: CharacterModel) {
        TODO("Not yet implemented")
    }


}

class FakeItemsRepository : ItemsRepository {
    override suspend fun getItemsByCharacterCode(characterCode: Int): Flow<MutableList<ItemsModel>> {
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
    } /* ... */


    override fun insertSpell(spell: SpellModel) {
        TODO("Not yet implemented")
    }

    override fun updateSpell(spell: SpellModel) {
        TODO("Not yet implemented")
    }

    override fun deleteSpell(spell: SpellModel) {
        TODO("Not yet implemented")
    }

}
