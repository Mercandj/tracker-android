package com.mercandalli.tracker.root

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RootModule {

    @Singleton
    @Provides
    fun provideRootManager(): RootManager {
        return RootManagerImpl()
    }
}