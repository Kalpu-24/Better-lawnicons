package app.lawnchair.betterlawnicons.di

import android.app.Application
import app.lawnchair.betterlawnicons.repository.IconRepository
import app.lawnchair.betterlawnicons.repository.IconRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IconRepositoryModule {

    @Provides
    @Singleton
    fun provideIconRepository(application: Application): IconRepository = IconRepositoryImpl(application)
}
