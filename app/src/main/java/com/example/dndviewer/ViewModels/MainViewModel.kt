package com.example.dndviewer.ViewModels

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import androidx.lifecycle.ViewModel
import com.example.dndviewer.BBDD.MyBBDD
import com.example.dndviewer.BBDD.MyHelper
import com.example.dndviewer.Models.CharacterModel
import com.example.dndviewer.Models.ItemsModel
import com.example.dndviewer.Models.SpellModel
import com.example.dndviewer.Screens.context
import java.io.File


class MainViewModel:ViewModel() {
    private lateinit var helper: MyHelper
    fun getBBDD(context: Context){
        helper = MyHelper(context)
    }

    fun getCharacters():MutableList<CharacterModel>{
        return helper.getCharacters()
    }
    fun deleteCharacter(character: CharacterModel){
        val file = File(context.filesDir.absolutePath + "/${character.name}")
        if(file.exists()){
            file.deleteRecursively()
        }
        helper.deleteCharacter(character.name)
    }

    fun setCharacter(character:CharacterModel){
        try{
            val db = helper.writableDatabase

            val values = ContentValues().apply {
                put(MyBBDD.personatge.COLUMN_NAME_NOM, character.name)
                put(MyBBDD.personatge.COLUMN_NAME_RUTA_HOMEBREW,character.homebrewRoute)
                put(MyBBDD.personatge.COLUMN_NAME_IMG_FITXA,character.image_character)
                put(MyBBDD.personatge.COLUMN_NAME_VIDA,character.vida)
                put(MyBBDD.personatge.COLUMN_NAME_VIDA_MAX,character.vidaMax)
                put(MyBBDD.personatge.COLUMN_NAME_MANA,character.mana)
                put(MyBBDD.personatge.COLUMN_NAME_MANA_MAX,character.manaMax)
                put(MyBBDD.personatge.COLUMN_NAME_MAX_SPELL,character.maxSpell)
            }
           db?.insert(MyBBDD.personatge.TABLE_NAME, null, values)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    fun updateCharacters(character:CharacterModel){
        try{
            // Gets the data repository in write mode
            val db = helper.writableDatabase

            // Create a new map of values, where column names are the keys
            val values = ContentValues().apply {
                put(MyBBDD.personatge.COLUMN_NAME_RUTA_HOMEBREW,character.homebrewRoute)
                put(MyBBDD.personatge.COLUMN_NAME_IMG_FITXA,character.image_character)
                put(MyBBDD.personatge.COLUMN_NAME_VIDA,character.vida)
                put(MyBBDD.personatge.COLUMN_NAME_VIDA_MAX,character.vidaMax)
                put(MyBBDD.personatge.COLUMN_NAME_MANA,character.mana)
                put(MyBBDD.personatge.COLUMN_NAME_MANA_MAX,character.manaMax)
                put(MyBBDD.personatge.COLUMN_NAME_OBS,character.observations)
                put(MyBBDD.personatge.COLUMN_NAME_MAX_SPELL,character.maxSpell)

            }
            // Insert the new row, returning the primary key value of the new row
            db?.update(
                MyBBDD.personatge.TABLE_NAME,
                values,
                "${MyBBDD.personatge.COLUMN_NAME_NOM} = ?",
                arrayOf(character.name)
            )
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun getObjectes(name:String):MutableList<ItemsModel>{
        return helper.getObjectes(name)
    }

    fun setObjectes(item:ItemsModel){
        try{
            val db = helper.writableDatabase
            val intEquiped = if(item.isEquiped)1 else 0
            val intConsumible = if(item.isConsumible)1 else 0

            val values = ContentValues().apply {
                put(MyBBDD.objectes.COLUMN_NAME_NOM, item.name)
                put(MyBBDD.objectes.COLUMN_NAME_CARREGUES,item.charges)
                put(MyBBDD.objectes.COLUMN_NAME_CARREGUES_ACTUALS,item.actualCharges)
                put(MyBBDD.objectes.COLUMN_NAME_DESCRIPCIO,item.description)
                put(MyBBDD.objectes.COLUMN_NAME_EQUIPAT,intEquiped)
                put(MyBBDD.objectes.COLUMN_NAME_CONSUMIBLE,intConsumible)
                put(MyBBDD.objectes.COLUMN_NAME_PERSONATGE,item.character)
            }

           db?.insert(MyBBDD.objectes.TABLE_NAME, null, values)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun updateObjectes(item:ItemsModel){
        try{
            val db = helper.writableDatabase
            val intEquiped = if(item.isEquiped)1 else 0
            val intConsumible = if(item.isConsumible)1 else 0

            val values = ContentValues().apply {
                put(MyBBDD.objectes.COLUMN_NAME_NOM, item.name)
                put(MyBBDD.objectes.COLUMN_NAME_CARREGUES,item.charges)
                put(MyBBDD.objectes.COLUMN_NAME_CARREGUES_ACTUALS,item.actualCharges)
                put(MyBBDD.objectes.COLUMN_NAME_DESCRIPCIO,item.description)
                put(MyBBDD.objectes.COLUMN_NAME_EQUIPAT,intEquiped)
                put(MyBBDD.objectes.COLUMN_NAME_CONSUMIBLE,intConsumible)
            }

             db?.update(
                MyBBDD.objectes.TABLE_NAME,
                values,
                "${MyBBDD.objectes.COLUMN_NAME_PERSONATGE} = ? AND ${MyBBDD.objectes.COLUMN_NAME_NOM} = ?",
                arrayOf(item.character,item.name)
            )
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun deleteObjecte(itemName:String,characterName:String){
        helper.deleteObjectes(itemName = itemName,characterName = characterName)
    }

    fun getSpells(name:String):MutableList<SpellModel>{
        return helper.getSpells(name)
    }
    fun insertSpells(name:String,numerSpells:Int){
        val list = helper.getSpells(name)
        var counter = list.size
        if(counter<numerSpells){
            while(counter<numerSpells){
                setSpells(item = SpellModel(character = name))
                counter++
            }
        }else{
            while (counter>numerSpells){
                var index = list.last().id
                helper.deleteSpells(index)
                counter --
                list.remove(list.last())
            }
        }
    }
    fun setSpells(item:SpellModel){
        try{
            val db = helper.writableDatabase

            val values = ContentValues().apply {
                put(MyBBDD.Spells.COLUMN_NAME_NOM,item.name)
                put(MyBBDD.Spells.COLUMN_NAME_NIVELL,item.level)
                put(MyBBDD.Spells.COLUMN_NAME_PERSONATGE,item.character)
            }
           db?.insert(MyBBDD.Spells.TABLE_NAME, null, values)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun updateSpells(item:SpellModel){
        try{
            val db = helper.writableDatabase
            val values = ContentValues().apply {
                put(MyBBDD.Spells.COLUMN_NAME_NOM,item.name)
                put(MyBBDD.Spells.COLUMN_NAME_NIVELL,item.level)
                put(MyBBDD.Spells.COLUMN_NAME_PERSONATGE,item.character)
            }

            db?.update(
                MyBBDD.Spells.TABLE_NAME,
                values,
                "${MyBBDD.Spells.COLUMN_NAME_PERSONATGE} = ? AND ${BaseColumns._ID} = '${item.id}'",
                arrayOf(item.character)
            )
        }catch (e:Exception){
            e.printStackTrace()
        }
    }





}