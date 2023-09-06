package com.example.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * An activity responsible for displaying an article.
 */
class ArticleActivity : AppCompatActivity() {
    private lateinit var webView : WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        webView = findViewById(R.id.articleWebView)
        webView.webViewClient = WebViewClient()
        var url : String = intent.extras?.get("URL").toString()
        webView.loadUrl(url)
    }


}