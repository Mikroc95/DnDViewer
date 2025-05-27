package com.Mikroc.DnDViewer.bbdd.Repository.Items

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.Mikroc.DnDViewer.bbdd.MyBBDD
import com.Mikroc.DnDViewer.Models.ItemsModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemsDao {

    @Query("SELECT * FROM ${MyBBDD.Objectes.TABLE_NAME}")
    fun getItems(): Flow<MutableList<ItemsModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: ItemsModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateItem(item: ItemsModel)

    @Delete
    fun deleteItem(item: ItemsModel)

}