package com.Mikroc.DnDViewer.bbdd.Repository.Items

import com.Mikroc.DnDViewer.Models.ItemsModel
import kotlinx.coroutines.flow.Flow

interface ItemsRepository {
    suspend fun getItems(characterCode: Int): Flow<MutableList<ItemsModel>>
    suspend fun getConsumables(characterCode: Int): Flow<MutableList<ItemsModel>>
    fun insertItem(item: ItemsModel)
    fun updateItem(item: ItemsModel)
    fun deleteItem(item: ItemsModel)
}