package com.chrrissoft.intents

import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideNotificationManager(@ApplicationContext ctx: Context): NotificationManager {
        return ctx.getSystemService(NotificationManager::class.java)
    }

    @Provides
    fun providePackageManager(@ApplicationContext ctx: Context): PackageManager {
        return ctx.packageManager
    }
}
