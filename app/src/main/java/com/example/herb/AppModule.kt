package com.example.herb

import android.app.Application
import androidx.room.Room
import com.example.herb.database.HerbDatabase
import com.example.herb.database.dao.HerbDao
import com.example.herb.database.entity.Herb
import com.example.herb.factory.HerbDetailViewModelAssistedFactory
import com.example.herb.model.HerbDetailViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): HerbDatabase {
        return Room.databaseBuilder(
            app,
            HerbDatabase::class.java,
            "herb-database.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideHerbDao(db: HerbDatabase): HerbDao {
        return db.herbDao()
    }

    @Provides
    @Singleton
    fun provideHerbDetailViewModelAssistedFactory (
        factory: HerbDetailViewModelAssistedFactory
    ): (Herb) -> HerbDetailViewModel = {
        it -> factory.create(it)
    }

}