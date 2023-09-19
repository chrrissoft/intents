package com.chrrissoft.intents.di

import android.content.Context
import com.chrrissoft.intents.IntentsApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideApplication(@ApplicationContext context: Context): IntentsApplication {
        return context as IntentsApplication
    }
}
