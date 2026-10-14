package com.galindo.ayudacampus

import android.app.Application
import com.galindo.ayudacampus.data.local.AyudaDatabase
import com.galindo.ayudacampus.data.local.DatosIniciales
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AyudaApp : Application() {

    val database: AyudaDatabase by lazy {
        AyudaDatabase.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            DatosIniciales.sembrar(database)
        }
    }
}
