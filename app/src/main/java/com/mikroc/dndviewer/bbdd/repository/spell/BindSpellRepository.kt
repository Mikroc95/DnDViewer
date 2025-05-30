package com.mikroc.dndviewer.bbdd.repository.spell

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class BindSpellRepository {

    @Binds
    @Singleton
    abstract fun bindSpellRepository(
        spellRepository: SpellRepositoryImpl
    ): SpellRepository
}