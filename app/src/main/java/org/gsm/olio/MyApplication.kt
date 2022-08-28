package org.gsm.olio

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.gsm.olio.preference.Prefs

@HiltAndroidApp
class MyApplication : Application() {
    companion object{
        lateinit var prefs: Prefs
    }
    override fun onCreate() {
        prefs=Prefs(applicationContext)
        super.onCreate()
    }
}