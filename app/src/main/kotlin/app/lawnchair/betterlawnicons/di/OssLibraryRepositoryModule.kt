package app.lawnchair.betterlawnicons.di

import android.app.Application
import app.lawnchair.betterlawnicons.repository.OssLibraryRepository
import app.lawnchair.betterlawnicons.repository.OssLibraryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OssLibraryRepositoryModule {

    @Provides
    @Singleton
    fun provideOssLibraryRepository(application: Application): OssLibraryRepository = OssLibraryRepositoryImpl(application = application)
}
