package uz.pdp.carpet

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CarpetApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}