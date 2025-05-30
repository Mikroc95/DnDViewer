package com.mikroc.dndviewer.bbdd.repository.database

import android.content.Context
import androidx.room.Room
import com.mikroc.dndviewer.bbdd.repository.character.CharacterDao
import com.mikroc.dndviewer.bbdd.repository.items.ItemsDao
import com.mikroc.dndviewer.bbdd.repository.spell.SpellDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "dndViewer.db"
        ).build()
    }

    @Provides
    fun provideCharacterDao(appDatabase: AppDatabase): CharacterDao {
        return appDatabase.characterDao()
    }

    @Provides
    fun provideItemsDao(appDatabase: AppDatabase): ItemsDao {
        return appDatabase.magicItemDao()
    }

    @Provides
    fun provideSpellDao(appDatabase: AppDatabase): SpellDao {
        return appDatabase.spellDao()
    }
}