package com.mikroc.dndviewer.bbdd.repository.items

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mikroc.dndviewer.bbdd.MyBbDd
import com.mikroc.dndviewer.models.ItemsModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemsDao {

    @Query("SELECT * FROM ${MyBbDd.Objectes.TABLE_NAME} WHERE character = :characterCode AND isConsumable = 0")
    fun getItems(characterCode: Int): Flow<MutableList<ItemsModel>>

    @Query("SELECT * FROM ${MyBbDd.Objectes.TABLE_NAME} WHERE character = :characterCode AND isConsumable = 1")
    fun getConsumables(characterCode: Int): Flow<MutableList<ItemsModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: ItemsModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateItem(item: ItemsModel)

    @Delete
    fun deleteItem(item: ItemsModel)

}