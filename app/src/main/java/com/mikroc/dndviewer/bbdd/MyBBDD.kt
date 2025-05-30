package com.mikroc.dndviewer.bbdd

import android.provider.BaseColumns

object MyBbDd {
    object Personatge : BaseColumns {
        const val TABLE_NAME = "personatge"
    }

    object Objectes : BaseColumns {
        const val TABLE_NAME = "objectes"
    }

    object Spells : BaseColumns {
        const val TABLE_NAME = "spells"
    }
}