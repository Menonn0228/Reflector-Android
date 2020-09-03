package com.example.reflector_android
import okhttp3.*
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream

class RSSService {
    fun fetchNews() {
        val url = "http://reflector-online.com/search/?f=rss&t=article&l=5&c=news/*"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        var test = XmlParser()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body!!.byteStream()
                val articles = test.parse(body)
            }

            override fun onFailure(call: Call, e: IOException) {
                println(e)
            }
        })
    }

}