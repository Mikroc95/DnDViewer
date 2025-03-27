package com.example.dndviewer.BBDD

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.dndviewer.Models.CharacterModel
import com.example.dndviewer.Models.ItemsModel
import com.example.dndviewer.Models.SpellModel

class MyHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    private val createPersonatge =
        "CREATE TABLE ${MyBBDD.Personatge.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${MyBBDD.Personatge.COLUMN_NAME_NOM} TEXT," +
                "${MyBBDD.Personatge.COLUMN_NAME_IMG_FITXA} BLOB," +
                "${MyBBDD.Personatge.COLUMN_NAME_RUTA_HOMEBREW} TEXT," +
                "${MyBBDD.Personatge.COLUMN_NAME_VIDA} TEXT," +
                "${MyBBDD.Personatge.COLUMN_NAME_VIDA_MAX} TEXT," +
                "${MyBBDD.Personatge.COLUMN_NAME_MANA} TEXT," +
                "${MyBBDD.Personatge.COLUMN_NAME_MANA_MAX} TEXT," +
                "${MyBBDD.Personatge.COLUMN_NAME_MAX_SPELL} INTEGER," +
                "${MyBBDD.Personatge.COLUMN_NAME_OBS} TEXT)"

    private val createObjectes =
        "CREATE TABLE ${MyBBDD.Objectes.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${MyBBDD.Objectes.COLUMN_NAME_NOM} TEXT," +
                "${MyBBDD.Objectes.COLUMN_NAME_DESCRIPCIO} TEXT," +
                "${MyBBDD.Objectes.COLUMN_NAME_CARREGUES} TEXT," +
                "${MyBBDD.Objectes.COLUMN_NAME_CARREGUES_ACTUALS} TEXT," +
                "${MyBBDD.Objectes.COLUMN_NAME_EQUIPAT} INTEGER," +
                "${MyBBDD.Objectes.COLUMN_NAME_CONSUMIBLE} INTEGER," +
                "${MyBBDD.Objectes.COLUMN_NAME_PERSONATGE} TEXT)"

    private val createSpells =
        "CREATE TABLE ${MyBBDD.Spells.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${MyBBDD.Spells.COLUMN_NAME_NOM} TEXT," +
                "${MyBBDD.Spells.COLUMN_NAME_PERSONATGE} TEXT," +
                "${MyBBDD.Spells.COLUMN_NAME_NIVELL} TEXT)"


    private val deleteEntries = "DROP TABLE IF EXISTS ${MyBBDD.Personatge.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        try {
            db.execSQL(createPersonatge)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            db.execSQL(createObjectes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            db.execSQL(createSpells)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(deleteEntries)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun getCharacters(): MutableList<CharacterModel> {
        val projection = arrayOf(
            MyBBDD.Personatge.COLUMN_NAME_NOM,
            MyBBDD.Personatge.COLUMN_NAME_RUTA_HOMEBREW,
            MyBBDD.Personatge.COLUMN_NAME_IMG_FITXA,
            MyBBDD.Personatge.COLUMN_NAME_VIDA,
            MyBBDD.Personatge.COLUMN_NAME_VIDA_MAX,
            MyBBDD.Personatge.COLUMN_NAME_MANA,
            MyBBDD.Personatge.COLUMN_NAME_MANA_MAX,
            MyBBDD.Personatge.COLUMN_NAME_OBS,
            MyBBDD.Personatge.COLUMN_NAME_MAX_SPELL
        )

        val cursor = readableDatabase.query(
            MyBBDD.Personatge.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        val list = mutableListOf<CharacterModel>()
        with(cursor) {
            while (moveToNext()) {
                val character = CharacterModel(
                    name = cursor.getString(0).orEmpty(),
                    homebrewRoute = cursor.getString(1).orEmpty(),
                    imageCharacter = cursor.getBlob(2),
                    vida = cursor.getInt(3).or(0),
                    vidaMax = cursor.getInt(4).or(0),
                    mana = cursor.getInt(5).or(0),
                    manaMax = cursor.getInt(6).or(0),
                    observations = cursor.getString(7).orEmpty(),
                    maxSpell = cursor.getInt(8).or(0)
                )
                list.add(character)
            }
        }
        return list

    }

    fun deleteCharacter(characterName: String) {
        val db = this.writableDatabase
        db.execSQL(
            "DELETE FROM ${MyBBDD.Personatge.TABLE_NAME}" +
                    " WHERE ${MyBBDD.Personatge.COLUMN_NAME_NOM} = '$characterName'"
        )
        db.execSQL(
            "DELETE FROM ${MyBBDD.Objectes.TABLE_NAME}" +
                    " WHERE ${MyBBDD.Objectes.COLUMN_NAME_PERSONATGE} = '$characterName'"
        )
        db.execSQL(
            "DELETE FROM ${MyBBDD.Spells.TABLE_NAME}" +
                    " WHERE ${MyBBDD.Objectes.COLUMN_NAME_PERSONATGE} = '$characterName'"
        )
        db.close()
    }

    fun getObjectes(personatge: String): MutableList<ItemsModel> {

        val projection = arrayOf(
            MyBBDD.Objectes.COLUMN_NAME_NOM,
            MyBBDD.Objectes.COLUMN_NAME_DESCRIPCIO,
            MyBBDD.Objectes.COLUMN_NAME_CARREGUES,
            MyBBDD.Objectes.COLUMN_NAME_EQUIPAT,
            MyBBDD.Objectes.COLUMN_NAME_CONSUMIBLE,
            MyBBDD.Objectes.COLUMN_NAME_PERSONATGE,
            MyBBDD.Objectes.COLUMN_NAME_CARREGUES_ACTUALS,
        )

        val cursor = readableDatabase.query(
            MyBBDD.Objectes.TABLE_NAME,
            projection,
            null,
            null,
            MyBBDD.Objectes.COLUMN_NAME_NOM,
            "${MyBBDD.Objectes.COLUMN_NAME_PERSONATGE} = '$personatge'",
            "${MyBBDD.Objectes.COLUMN_NAME_CONSUMIBLE} DESC"
        )
        val list = mutableListOf<ItemsModel>()
        with(cursor) {
            while (moveToNext()) {
                val item = ItemsModel().apply {
                    name = cursor.getString(0)
                    description = cursor.getString(1)
                    charges = cursor.getString(2)
                    actualCharges = cursor.getString(6)
                    isEquiped = cursor.getInt(3) == 1
                    isConsumible = cursor.getInt(4) == 1
                    character = cursor.getString(5)
                }
                list.add(item)
            }
        }
        return list
    }

    fun deleteObjectes(itemName: String, characterName: String) {
        val db = this.writableDatabase
        db.execSQL(
            "DELETE FROM ${MyBBDD.Objectes.TABLE_NAME}" +
                    " WHERE ${MyBBDD.Objectes.COLUMN_NAME_NOM} = '$itemName' " +
                    "AND ${MyBBDD.Objectes.COLUMN_NAME_PERSONATGE} = '$characterName'"
        )
        db.close()
    }

    fun getSpells(personatge: String): MutableList<SpellModel> {

        val projection = arrayOf(
            BaseColumns._ID,
            MyBBDD.Spells.COLUMN_NAME_NOM,
            MyBBDD.Spells.COLUMN_NAME_NIVELL,
            MyBBDD.Spells.COLUMN_NAME_PERSONATGE,
        )

        val cursor = readableDatabase.query(
            MyBBDD.Spells.TABLE_NAME,
            projection,
            null,
            null,
            BaseColumns._ID,
            "${MyBBDD.Spells.COLUMN_NAME_PERSONATGE} = '$personatge'",
            "${MyBBDD.Spells.COLUMN_NAME_NIVELL} ASC"
        )
        val list = mutableListOf<SpellModel>()
        with(cursor) {
            while (moveToNext()) {
                val item = SpellModel().apply {
                    id = cursor.getInt(0)
                    name = cursor.getString(1)
                    level = cursor.getString(2)
                    character = cursor.getString(3)
                }
                list.add(item)
            }
        }
        return list
    }

    fun deleteSpells(id: Int) {
        val db = this.writableDatabase
        db.execSQL(
            "DELETE FROM ${MyBBDD.Spells.TABLE_NAME}" +
                    " WHERE ${BaseColumns._ID} = '$id' "
        )
        db.close()
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "dndViewer.db"
    }
}