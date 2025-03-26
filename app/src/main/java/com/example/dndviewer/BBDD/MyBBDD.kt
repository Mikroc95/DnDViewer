package com.example.dndviewer.BBDD

import android.provider.BaseColumns

object MyBBDD {
    object personatge : BaseColumns {
        const val TABLE_NAME = "personatge"
        const val COLUMN_NAME_NOM = "nom"
        const val COLUMN_NAME_RUTA_HOMEBREW = "ruta_homebrew"
        const val COLUMN_NAME_IMG_FITXA="image_fitxa"
        const val COLUMN_NAME_VIDA="vida"
        const val COLUMN_NAME_VIDA_MAX="vida_max"
        const val COLUMN_NAME_MANA="mana"
        const val COLUMN_NAME_MANA_MAX="mana_max"
        const val COLUMN_NAME_OBS="observacions"
        const val COLUMN_NAME_MAX_SPELL="max_spell"
    }

    object objectes : BaseColumns {
        const val TABLE_NAME = "objectes"
        const val COLUMN_NAME_NOM = "nom"
        const val COLUMN_NAME_DESCRIPCIO = "descripcio"
        const val COLUMN_NAME_CARREGUES ="carregues"
        const val COLUMN_NAME_CARREGUES_ACTUALS ="carregues_actuals"
        const val COLUMN_NAME_EQUIPAT="equipat"
        const val COLUMN_NAME_CONSUMIBLE="consumible"
        const val COLUMN_NAME_PERSONATGE ="personatge"
    }

    object Spells : BaseColumns {
        const val TABLE_NAME = "spells"
        const val COLUMN_NAME_NOM = "nom"
        const val COLUMN_NAME_NIVELL = "nivell"
        const val COLUMN_NAME_PERSONATGE = "personatge"
    }
}