package com.neobuchaemyj.datingapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.neobuchaemyj.datingapp.*
import com.neobuchaemyj.datingapp._core.BaseActivity
import kotlinx.android.synthetic.main.activity_web_view.*


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 3/13/19.
 */
class SplashActivity : BaseActivity() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun getContentView(): Int = R.layout.activity_web_view

    override fun initUI() {
        webView = web_view
        progressBar = progress_bar
    }

    override fun setUI() {
        logEvent("splash-screen")
        webView.webViewClient = object : WebViewClient() {
            /**
             * Check if url contains key words:
             * /money - needed user (launch WebViewActivity or show in browser)
             * /main - bot or unsuitable user (launch ContentActivity)
             */
            @SuppressLint("deprecated")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.contains("/money")) {
                    // task url for web view or browser
                    val taskUrl = remoteConfig.getString(TASK_URL)
                    val value = remoteConfig.getString(SHOW_IN)

                    if (value == WEB_VIEW) {
                        startActivity(
                            Intent(this@SplashActivity, ChooseAgeActivity::class.java)
                                .putExtra(EXTRA_TASK_URL, taskUrl)
                        )
                        finish()
                    } else if (value == BROWSER) {
                        // launch browser with task url
                        val browserIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(taskUrl)
                        )

                        logEvent("task-url-browser")
                        startActivity(browserIntent)
                        finish()
                    }
                } else if (url.contains("/main")) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
                progressBar.visibility = View.GONE
                return false
            }
        }

        progressBar.visibility = View.VISIBLE

        fetchRemoteConfig({
            remoteConfig = it
            // load needed url to determine if user is suitable
            webView.loadUrl(it.getString(SPLASH_URL))
        }, {
            progressBar.visibility = View.GONE
        })
    }
}