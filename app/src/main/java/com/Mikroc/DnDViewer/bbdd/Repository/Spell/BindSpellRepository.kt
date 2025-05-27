package com.Mikroc.DnDViewer.bbdd.Repository.Spell

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class bindSpellRepository {

    @Binds
    @Singleton
    abstract fun bindSpellRepository(
        spellRepository: SpellRepositoryImpl
    ): SpellRepository
}