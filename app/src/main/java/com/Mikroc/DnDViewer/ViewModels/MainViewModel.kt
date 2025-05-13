package com.Mikroc.DnDViewer.ViewModels

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.Mikroc.DnDViewer.BBDD.MyBBDD
import com.Mikroc.DnDViewer.BBDD.MyHelper
import com.Mikroc.DnDViewer.Models.CharacterModel
import com.Mikroc.DnDViewer.Models.ItemsModel
import com.Mikroc.DnDViewer.Models.SpellModel
import java.io.File


class MainViewModel : ViewModel() {
    private lateinit var helper: MyHelper

    var characterSelected = mutableStateOf(CharacterModel())
    fun getBBDD(context: Context) {
        helper = MyHelper(context)
    }

    fun getCharacters(): MutableList<CharacterModel> {
        return helper.getCharacters()
    }

    fun getCharacter(character: String): CharacterModel {
        return helper.getCharacter(characterCode = character)
    }

    fun getCharacterByName(characterName: String): CharacterModel {
        return helper.getCharacterByName(characterName = characterName)
    }

    fun deleteHomeBrew(characterName: String, homebrewRoute: String, context: Context) {
        try {
            val directory = File(context.filesDir.absolutePath + "/${characterName}")
            if (directory.exists()) {
                directory.deleteRecursively()
            }
            val file = File(homebrewRoute)
            if (file.exists()) {
                file.deleteRecursively()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun deleteCharacter(character: CharacterModel, context: Context) {
        deleteHomeBrew(
            characterName = character.name,
            homebrewRoute = character.homebrewRoute,
            context = context
        )
        helper.deleteCharacter(character.code)
    }

    fun insertCharacter(character: CharacterModel) {
        try {
            val db = helper.writableDatabase

            val values = ContentValues().apply {
                put(MyBBDD.Personatge.COLUMN_NAME_NOM, character.name)
                put(MyBBDD.Personatge.COLUMN_NAME_RUTA_HOMEBREW, character.homebrewRoute)
                put(MyBBDD.Personatge.COLUMN_NAME_IMG_FITXA, character.imageCharacter)
                put(MyBBDD.Personatge.COLUMN_NAME_VIDA, character.vida)
                put(MyBBDD.Personatge.COLUMN_NAME_VIDA_MAX, character.vidaMax)
                put(MyBBDD.Personatge.COLUMN_NAME_MANA, character.mana)
                put(MyBBDD.Personatge.COLUMN_NAME_MANA_MAX, character.manaMax)
                put(MyBBDD.Personatge.COLUMN_NAME_METAMAGIA, character.metaMagia)
                put(MyBBDD.Personatge.COLUMN_NAME_METAMAGIA_MAX, character.metaMagiaMax)
                put(MyBBDD.Personatge.COLUMN_NAME_MAX_SPELL, character.maxSpell)
            }
            db?.insert(MyBBDD.Personatge.TABLE_NAME, null, values)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun updateCharacters(character: CharacterModel) {
        try {
            val db = helper.writableDatabase

            val values = ContentValues().apply {
                put(MyBBDD.Personatge.COLUMN_NAME_NOM, character.name)
                put(MyBBDD.Personatge.COLUMN_NAME_RUTA_HOMEBREW, character.homebrewRoute)
                put(MyBBDD.Personatge.COLUMN_NAME_IMG_FITXA, character.imageCharacter)
                put(MyBBDD.Personatge.COLUMN_NAME_VIDA, character.vida)
                put(MyBBDD.Personatge.COLUMN_NAME_VIDA_MAX, character.vidaMax)
                put(MyBBDD.Personatge.COLUMN_NAME_MANA, character.mana)
                put(MyBBDD.Personatge.COLUMN_NAME_MANA_MAX, character.manaMax)
                put(MyBBDD.Personatge.COLUMN_NAME_METAMAGIA, character.metaMagia)
                put(MyBBDD.Personatge.COLUMN_NAME_METAMAGIA_MAX, character.metaMagiaMax)
                put(MyBBDD.Personatge.COLUMN_NAME_MAX_SPELL, character.maxSpell)
                put(MyBBDD.Personatge.COLUMN_NAME_OBS, character.observations)
            }
            db?.update(
                MyBBDD.Personatge.TABLE_NAME,
                values,
                "${BaseColumns._ID} = ?",
                arrayOf(character.code)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getObjectes(characterCode: String): MutableList<ItemsModel> {
        return helper.getObjectes(characterCode)
    }

    fun insertObjectes(item: ItemsModel) {
        try {
            val db = helper.writableDatabase
            val intEquiped = if (item.isEquiped) 1 else 0
            val intConsumible = if (item.isConsumible) 1 else 0

            val values = ContentValues().apply {
                put(MyBBDD.Objectes.COLUMN_NAME_NOM, item.name)
                put(MyBBDD.Objectes.COLUMN_NAME_CARREGUES, item.charges)
                put(MyBBDD.Objectes.COLUMN_NAME_CARREGUES_ACTUALS, item.actualCharges)
                put(MyBBDD.Objectes.COLUMN_NAME_DESCRIPCIO, item.description)
                put(MyBBDD.Objectes.COLUMN_NAME_EQUIPAT, intEquiped)
                put(MyBBDD.Objectes.COLUMN_NAME_CONSUMIBLE, intConsumible)
                put(MyBBDD.Objectes.COLUMN_NAME_PERSONATGE, item.character)
            }

            db?.insert(MyBBDD.Objectes.TABLE_NAME, null, values)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateObjectes(item: ItemsModel) {
        try {
            val db = helper.writableDatabase
            val intEquiped = if (item.isEquiped) 1 else 0
            val intConsumible = if (item.isConsumible) 1 else 0

            val values = ContentValues().apply {
                put(MyBBDD.Objectes.COLUMN_NAME_NOM, item.name)
                put(MyBBDD.Objectes.COLUMN_NAME_CARREGUES, item.charges)
                put(MyBBDD.Objectes.COLUMN_NAME_CARREGUES_ACTUALS, item.actualCharges)
                put(MyBBDD.Objectes.COLUMN_NAME_DESCRIPCIO, item.description)
                put(MyBBDD.Objectes.COLUMN_NAME_EQUIPAT, intEquiped)
                put(MyBBDD.Objectes.COLUMN_NAME_CONSUMIBLE, intConsumible)
            }

            db?.update(
                MyBBDD.Objectes.TABLE_NAME,
                values,
                "${BaseColumns._ID} = ? AND ${MyBBDD.Objectes.COLUMN_NAME_PERSONATGE} = ?",
                arrayOf(item.id.toString(), item.character)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteObjecte(id: Int) {
        helper.deleteObjectes(id = id)
    }

    fun getSpells(characterCode: String): MutableList<SpellModel> {
        return helper.getSpells(characterCode)
    }

    fun insertSpells(characterCode: String, numerSpells: Int) {
        try {
            val list = helper.getSpells(characterCode)
            var counter = list.size
            if (counter < numerSpells) {
                while (counter < numerSpells) {
                    setSpells(item = SpellModel(character = characterCode))
                    counter++
                }
            } else {
                while (counter > numerSpells) {
                    val index = list.last().id
                    helper.deleteSpells(index)
                    counter--
                    list.remove(list.last())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setSpells(item: SpellModel) {
        try {
            val db = helper.writableDatabase

            val values = ContentValues().apply {
                put(MyBBDD.Spells.COLUMN_NAME_NOM, item.name)
                put(MyBBDD.Spells.COLUMN_NAME_NIVELL, item.level)
                put(MyBBDD.Spells.COLUMN_NAME_PERSONATGE, item.character)
            }
            db?.insert(MyBBDD.Spells.TABLE_NAME, null, values)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateSpells(item: SpellModel) {
        try {
            val db = helper.writableDatabase
            val values = ContentValues().apply {
                put(MyBBDD.Spells.COLUMN_NAME_NOM, item.name)
                put(MyBBDD.Spells.COLUMN_NAME_NIVELL, item.level)
                put(MyBBDD.Spells.COLUMN_NAME_PERSONATGE, item.character)
            }

            db?.update(
                MyBBDD.Spells.TABLE_NAME,
                values,
                "${MyBBDD.Spells.COLUMN_NAME_PERSONATGE} = ? AND ${BaseColumns._ID} = ?",
                arrayOf(item.character, item.id.toString())
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun checkUselessFolder(context: Context) {
        try {
            val characterList = helper.getCharacters().map { it.name }.toSet()
            val filesDir = context.filesDir.listFiles()

            filesDir?.forEach { item ->
                if (item.isDirectory && !characterList.contains(item.name)) {
                    try {
                        item.deleteRecursively()
                    } catch (deleteException: Exception) {
                        deleteException.printStackTrace()
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}