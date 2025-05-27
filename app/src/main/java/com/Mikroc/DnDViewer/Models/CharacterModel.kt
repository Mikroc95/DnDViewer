package com.Mikroc.DnDViewer.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.Mikroc.DnDViewer.bbdd.MyBBDD
import java.io.Serializable

@Entity(tableName = MyBBDD.Personatge.TABLE_NAME)
data class CharacterModel(
    @PrimaryKey(autoGenerate = true)
    var code: Int = 0,
    var name: String = "",
    var imageCharacter: ByteArray = byteArrayOf(),
    var homebrewRoute: String = "",
    var vida: Int = 0,
    var vidaMax: Int = 0,
    var mana: Int = 0,
    var manaMax: Int = 0,
    var metaMagia: Int = 0,
    var metaMagiaMax: Int = 0,
    var observations: String = "",
    var maxSpell: Int = 0
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CharacterModel

        if (name != other.name) return false
        if (!imageCharacter.contentEquals(other.imageCharacter)) return false
        if (homebrewRoute != other.homebrewRoute) return false
        if (vida != other.vida) return false
        if (vidaMax != other.vidaMax) return false
        if (mana != other.mana) return false
        if (manaMax != other.manaMax) return false
        if (metaMagia != other.metaMagia) return false
        if (metaMagiaMax != other.metaMagiaMax) return false
        if (observations != other.observations) return false
        if (maxSpell != other.maxSpell) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + imageCharacter.contentHashCode()
        result = 31 * result + homebrewRoute.hashCode()
        result = 31 * result + vida
        result = 31 * result + vidaMax
        result = 31 * result + mana
        result = 31 * result + manaMax
        result = 31 * result + metaMagia
        result = 31 * result + metaMagiaMax
        result = 31 * result + observations.hashCode()
        result = 31 * result + maxSpell
        return result
    }
}