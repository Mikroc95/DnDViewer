package com.Mikroc.DnDViewer.BBDD

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.Mikroc.DnDViewer.Models.CharacterModel
import com.Mikroc.DnDViewer.Models.ItemsModel
import com.Mikroc.DnDViewer.Models.SpellModel
import com.Mikroc.DnDViewer.Utils.stringToInt

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
    private val alterTabletMetaMagia = "ALTER TABLE ${MyBBDD.Personatge.TABLE_NAME}" +
            " ADD COLUMN ${MyBBDD.Personatge.COLUMN_NAME_METAMAGIA} TEXT"
    private val alterTabletMetaMagiaMax = "ALTER TABLE ${MyBBDD.Personatge.TABLE_NAME}" +
            " ADD COLUMN ${MyBBDD.Personatge.COLUMN_NAME_METAMAGIA_MAX} TEXT"

    override fun onCreate(db: SQLiteDatabase) {
        try {
            db.execSQL(createPersonatge)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            db.execSQL(alterTabletMetaMagia)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            db.execSQL(alterTabletMetaMagiaMax)
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
        try {
            val projection = arrayOf(
                MyBBDD.Personatge.COLUMN_NAME_NOM,
                MyBBDD.Personatge.COLUMN_NAME_RUTA_HOMEBREW,
                MyBBDD.Personatge.COLUMN_NAME_IMG_FITXA,
                MyBBDD.Personatge.COLUMN_NAME_VIDA,
                MyBBDD.Personatge.COLUMN_NAME_VIDA_MAX,
                MyBBDD.Personatge.COLUMN_NAME_MANA,
                MyBBDD.Personatge.COLUMN_NAME_MANA_MAX,
                MyBBDD.Personatge.COLUMN_NAME_METAMAGIA,
                MyBBDD.Personatge.COLUMN_NAME_METAMAGIA_MAX,
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
                        vida = cursor.getString(3).orEmpty().stringToInt(),
                        vidaMax = cursor.getString(4).orEmpty().stringToInt(),
                        mana = cursor.getString(5).orEmpty().stringToInt(),
                        manaMax = cursor.getString(6).orEmpty().stringToInt(),
                        metaMagia = cursor.getString(7).orEmpty().stringToInt(),
                        metaMagiaMax = cursor.getString(8).orEmpty().stringToInt(),
                        observations = cursor.getString(9).orEmpty(),
                        maxSpell = cursor.getString(10).orEmpty().stringToInt()
                    )
                    list.add(character)
                }
            }
            cursor.close()
            return list
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mutableListOf()
    }

    fun getCharacter(character: String): CharacterModel {
        try {
            val projection = arrayOf(
                MyBBDD.Personatge.COLUMN_NAME_NOM,
                MyBBDD.Personatge.COLUMN_NAME_RUTA_HOMEBREW,
                MyBBDD.Personatge.COLUMN_NAME_IMG_FITXA,
                MyBBDD.Personatge.COLUMN_NAME_VIDA,
                MyBBDD.Personatge.COLUMN_NAME_VIDA_MAX,
                MyBBDD.Personatge.COLUMN_NAME_MANA,
                MyBBDD.Personatge.COLUMN_NAME_MANA_MAX,
                MyBBDD.Personatge.COLUMN_NAME_METAMAGIA,
                MyBBDD.Personatge.COLUMN_NAME_METAMAGIA_MAX,
                MyBBDD.Personatge.COLUMN_NAME_OBS,
                MyBBDD.Personatge.COLUMN_NAME_MAX_SPELL
            )

            val cursor = readableDatabase.query(
                MyBBDD.Personatge.TABLE_NAME,
                projection,
                null,
                null,
                MyBBDD.Personatge.COLUMN_NAME_NOM,
                "${MyBBDD.Personatge.COLUMN_NAME_NOM} = '$character'",
                MyBBDD.Personatge.COLUMN_NAME_NOM
            )
            with(cursor) {
                moveToNext()
                return CharacterModel(
                    name = getString(0).orEmpty(),
                    homebrewRoute = getString(1).orEmpty(),
                    imageCharacter = getBlob(2),
                    vida = getString(3).orEmpty().stringToInt(),
                    vidaMax = getString(4).orEmpty().stringToInt(),
                    mana = getString(5).orEmpty().stringToInt(),
                    manaMax = getString(6).orEmpty().stringToInt(),
                    metaMagia = getString(7).orEmpty().stringToInt(),
                    metaMagiaMax = getString(8).orEmpty().stringToInt(),
                    observations = getString(9).orEmpty(),
                    maxSpell = getString(10).orEmpty().stringToInt()
                )
                close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return CharacterModel()
    }

    fun deleteCharacter(characterName: String) {
        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getObjectes(personatge: String): MutableList<ItemsModel> {
        try {
            val projection = arrayOf(
                MyBBDD.Objectes.COLUMN_NAME_NOM,
                MyBBDD.Objectes.COLUMN_NAME_DESCRIPCIO,
                MyBBDD.Objectes.COLUMN_NAME_CARREGUES,
                MyBBDD.Objectes.COLUMN_NAME_EQUIPAT,
                MyBBDD.Objectes.COLUMN_NAME_CONSUMIBLE,
                MyBBDD.Objectes.COLUMN_NAME_PERSONATGE,
                MyBBDD.Objectes.COLUMN_NAME_CARREGUES_ACTUALS,
                BaseColumns._ID
            )

            val cursor = readableDatabase.query(
                MyBBDD.Objectes.TABLE_NAME,
                projection,
                null,
                null,
                BaseColumns._ID,
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
                        id = cursor.getInt(7)
                    }
                    list.add(item)
                }
            }
            cursor.close()
            return list
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mutableListOf()
    }

    fun deleteObjectes(id: Int) {
        try {
            val db = this.writableDatabase
            db.execSQL(
                "DELETE FROM ${MyBBDD.Objectes.TABLE_NAME}" +
                        " WHERE ${BaseColumns._ID} = '$id' "
            )
            db.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getSpells(personatge: String): MutableList<SpellModel> {
        try {
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
                BaseColumns._ID
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
            cursor.close()
            return list
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mutableListOf()
    }

    fun deleteSpells(id: Int) {
        try {
            val db = this.writableDatabase
            db.execSQL(
                "DELETE FROM ${MyBBDD.Spells.TABLE_NAME}" +
                        " WHERE ${BaseColumns._ID} = '$id' "
            )
            db.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "dndViewer.db"
    }
}