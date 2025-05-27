package com.Mikroc.DnDViewer.bbdd.Repository.Items

import com.Mikroc.DnDViewer.Models.ItemsModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemsRepositoryImpl @Inject constructor(
    private val itemDao: ItemsDao
) : ItemsRepository {
    override suspend fun getItemsByCharacterCode(characterCode: Int): Flow<MutableList<ItemsModel>> {
        return itemDao.getItems()
    }


    override fun insertItem(item: ItemsModel) {
        itemDao.insertItem(item = item)
    }

    override fun updateItem(item: ItemsModel) {
        itemDao.updateItem(item = item)
    }

    override fun deleteItem(item: ItemsModel) {
        itemDao.deleteItem(item = item)
    }


}