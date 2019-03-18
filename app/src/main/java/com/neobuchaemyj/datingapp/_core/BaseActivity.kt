package com.neobuchaemyj.datingapp._core

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.neobuchaemyj.datingapp.BuildConfig
import com.neobuchaemyj.datingapp.R


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 3/13/19.
 */
abstract class BaseActivity : AppCompatActivity() {

    private lateinit var appEventsLogger: AppEventsLogger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        appEventsLogger = AppEventsLogger.newLogger(this)
        initUI()
        setUI()
    }

    @LayoutRes
    abstract fun getContentView(): Int

    abstract fun initUI()

    abstract fun setUI()

    fun fetchRemoteConfig(
        onTaskSuccessful: (FirebaseRemoteConfig) -> Unit,
        onFailure: () -> Unit = {}
    ) {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .build()
        val cacheExpiration: Long = 2000

        remoteConfig.setConfigSettings(configSettings)
        remoteConfig.setDefaults(R.xml.default_parameters)

        remoteConfig.fetch(cacheExpiration).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                remoteConfig.activateFetched()
                onTaskSuccessful(remoteConfig)
            }
        }.addOnFailureListener { onFailure() }
    }

    fun logEvent(event: String) {
        appEventsLogger.logEvent(event)
    }
}