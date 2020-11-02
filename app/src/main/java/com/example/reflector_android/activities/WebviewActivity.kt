package com.example.reflector_android.activities

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.reflector_android.viewHolders.ArticleListViewHolder


class WebviewActivity(): AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                loadJs(view)
                runOnUiThread {
                    view.visibility = View.VISIBLE
                }
            }
        }

        val webView = WebView(this)
        webView.webViewClient = webViewClient
        webView.settings.javaScriptEnabled = true
        setContentView(webView)
        val url = intent.getStringExtra(ArticleListViewHolder.articleUrl)
        webView.loadUrl(url)
        runOnUiThread {
            webView.visibility = View.INVISIBLE
        }
    }

    private fun loadJs(webView: WebView) {
        val navbarId = "document.getElementById(\"site-navbar-container\")"
        val adId = "document.getElementById(\"tncms-region-global-container-top\")"
        val bottomContentId = "document.getElementById(\"tncms-region-article_side_top\")"
        val tagsId = "document.getElementById(\"asset-below\")"
        val footerId = "document.getElementById(\"site-footer-container\")"
        val socialId = "document.getElementsByClassName(\"social-share-links hidden-print list-inline icon\")"
        webView.loadUrl(
            """
            javascript:(function() { 
                                        $navbarId.parentNode.removeChild($navbarId);
                                        $adId.parentNode.removeChild($adId);
                                        $bottomContentId.parentNode.parentNode.remove();
                                        $tagsId.parentNode.removeChild($tagsId);
                                        $footerId.parentNode.removeChild($footerId);
                                        var elements = $socialId;
                                        while(elements.length > 0) {
                                            elements[0].parentNode.removeChild(elements[0]);
                                        }
                                    })()
            """
        )
    }
}