package com.neobuchaemyj.datingapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.mazur.app.EXTRA_TASK_URL
import com.neobuchaemyj.datingapp.R
import com.neobuchaemyj.datingapp._core.BaseActivity
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.activity_web_view.*


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 3/13/19.
 */
class WebViewActivity : BaseActivity(), AdvancedWebView.Listener {

    private lateinit var webView: AdvancedWebView
    private lateinit var progressBar: ProgressBar

    override fun getContentView(): Int = R.layout.activity_web_view

    override fun initUI() {
        webView = web_view
        progressBar = progress_bar
    }

    override fun setUI() {
        logEvent("web-view-screen")
        progressBar.visibility = View.VISIBLE

        configureWebView()

        webView.loadUrl(intent.getStringExtra(EXTRA_TASK_URL))
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configureWebView() {
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                progressBar.visibility = View.GONE
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
    }

    override fun onBackPressed() {
        if (!webView.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        webView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        webView.onDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        webView.onActivityResult(requestCode, resultCode, intent)
    }

    override fun onPageFinished(url: String?) {
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
    }

    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {
    }

    override fun onExternalPageRequest(url: String?) {
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
    }
}