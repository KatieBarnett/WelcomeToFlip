package dev.katiebarnett.welcometoflip.storage.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.katiebarnett.welcometoflip.core.AppDispatchers
import dev.katiebarnett.welcometoflip.core.Dispatcher
import dev.katiebarnett.welcometoflip.storage.SavedGamesDataSource.Companion.PROTO_FILE_NAME
import dev.katiebarnett.welcometoflip.storage.SavedGamesSerializer
import dev.katiebarnett.welcometoflip.storage.models.Savedgames
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providesSavedGamesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(AppDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        savedGamesSerializer: SavedGamesSerializer
    ): DataStore<Savedgames> =
        DataStoreFactory.create(
            serializer = savedGamesSerializer,
            scope = CoroutineScope(ioDispatcher + SupervisorJob()),
            migrations = listOf()
        ) {
            context.dataStoreFile(PROTO_FILE_NAME)
        }
}
