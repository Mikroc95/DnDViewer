package com.mikroc.dndviewer.bbdd.repository.items

import com.mikroc.dndviewer.models.ItemsModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemsRepositoryImpl @Inject constructor(
    private val itemDao: ItemsDao
) : ItemsRepository {
    override suspend fun getItems(characterCode: Int): Flow<MutableList<ItemsModel>> {
        return itemDao.getItems(characterCode = characterCode)
    }

    override suspend fun getConsumables(characterCode: Int): Flow<MutableList<ItemsModel>> {
        return itemDao.getConsumables(characterCode = characterCode)
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