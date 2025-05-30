package com.mikroc.dndviewer.bbdd.repository.items


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class BindItemsRepository {

    @Binds
    @Singleton
    abstract fun bindItemsRepository(
        itemsRepositoryImpl: ItemsRepositoryImpl
    ): ItemsRepository
}