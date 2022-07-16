package com.km.real_convenience_store.application

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.soloader.SoLoader
import com.km.real_convenience_store.BuildConfig
import com.km.real_convenience_store.network.NetworkModule

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        initFlipper()
    }

    private fun initFlipper() {
        SoLoader.init(this, false)

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            AndroidFlipperClient.getInstance(this).apply {
                // layout
                addPlugin(InspectorFlipperPlugin(this@App, DescriptorMapping.withDefaults()))
                // network
                addPlugin(NetworkModule.networkFlipperPlugin)
                // database
                addPlugin(DatabasesFlipperPlugin(this@App))
            }.start()
        }
    }
}
