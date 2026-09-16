package com.example.knjizara.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module
import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.android.ext.koin.androidContext

actual val platformModule: Module = module {
    single<HttpClientEngine> { OkHttp.create() }
    single<Settings> {
        val prefs = androidContext().getSharedPreferences("knjizara_prefs", Context.MODE_PRIVATE)
        SharedPreferencesSettings(prefs)
    }
}
