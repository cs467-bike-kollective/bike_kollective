package com.example.bikekollective

import android.app.Application
import android.util.Log

class ApplicationContext: Application() {

    // Todo(Kally) set up data for database
    override fun onCreate() {
        Log.v("ApplicationContext", "HERE")
        super.onCreate()
    }
}