package com.makemoney.earncash.freecash.inject

import android.content.Context
import com.makemoney.earncash.freecash.core.managers.*
import dagger.Module
import dagger.Provides

@Module
class AppModule(var context: Context) {

    @Provides
    fun providePreferences() : PreferencesManager {
        return PreferencesManager(context)
    }

    @Provides
    fun provideCoins() : CoinsManager {
        return CoinsManager(context)
    }

    @Provides
    fun provideRetrofit() : RetrofitManager {
        return RetrofitManager()
    }

    @Provides
    fun provideDialogs() : DialogsManager {
        return DialogsManager()
    }

    @Provides
    fun provideAnimations() : AnimationsManager {
        return AnimationsManager()
    }
}
