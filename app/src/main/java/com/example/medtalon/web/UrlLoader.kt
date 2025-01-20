package com.example.medtalon.web

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.test2.R

class UrlLoader : AppCompatActivity() {
    private lateinit var webView: WebView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_url_loader)

        // Получаем URL из Intent
        val url = intent.getStringExtra("url") ?: "https://example.com"

        webView = findViewById(R.id.webView)

        // Включить JavaScript (если требуется)
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true

        // Настроить WebViewClient для открытия ссылок внутри WebView
        webView.webViewClient = WebViewClient()

        // Загрузить URL
        webView.loadUrl(url)
    }
}
