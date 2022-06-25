package com.lowe.wanandroid.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.lowe.wanandroid.account.AccountManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AccountManagerModule {

    @Singleton
    @Provides
    fun provideAccountManager(
        @IOApplicationScope ioApplicationScope: CoroutineScope,
        dataStore: DataStore<Preferences>
    ) = AccountManager(ioApplicationScope, dataStore)

}