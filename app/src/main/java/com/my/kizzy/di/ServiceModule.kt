/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * ServiceModule.kt is part of Kizzy
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of yzziK(Vaibhav)
 *  *  *****************************************************************
 *
 *
 */

package com.my.kizzy.di

import android.content.ComponentName
import android.content.Context
import com.my.kizzy.data.rpc.KizzyRPC
import com.my.kizzy.domain.interfaces.Logger
import com.my.kizzy.domain.repository.KizzyRepository
import com.my.kizzy.feature_rpc_base.services.NotificationListener
import com.my.kizzy.preference.Prefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kizzy.gateway.DiscordWebSocket
import kizzy.gateway.DiscordWebSocketImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {
    @Provides
    fun providesDiscordWebsocket(
        logger: Logger
    ): DiscordWebSocket =
        DiscordWebSocketImpl(Prefs[Prefs.TOKEN, ""], logger)

    @Provides
    fun provideKizzyRpc(
        kizzyRepository: KizzyRepository,
        discordWebSocket: DiscordWebSocket,
        logger: Logger
    ) = KizzyRPC(Prefs[Prefs.TOKEN, ""], kizzyRepository, discordWebSocket,logger)

    @Provides
    fun providesCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }
    @Provides
    fun providesComponentName(
        @ApplicationContext context: Context
    ) = ComponentName(context, NotificationListener::class.java)
}