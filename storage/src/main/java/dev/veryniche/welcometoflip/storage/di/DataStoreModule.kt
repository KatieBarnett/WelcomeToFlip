package dev.veryniche.welcometoflip.storage.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.veryniche.welcometoflip.core.AppDispatchers
import dev.veryniche.welcometoflip.core.Dispatcher
import dev.veryniche.welcometoflip.storage.SavedGamesDataSource.Companion.PROTO_FILE_NAME
// import dev.veryniche.welcometoflip.storage.SavedGamesDataSource.Companion.PROTO_FILE_NAME
import dev.veryniche.welcometoflip.storage.SavedGamesSerializer
import dev.veryniche.welcometoflip.storage.models.Savedgames
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val USER_PREFERENCES = "user_preferences"

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

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(appContext, USER_PREFERENCES)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(USER_PREFERENCES) }
        )
    }
}
