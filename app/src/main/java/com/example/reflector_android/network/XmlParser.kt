package com.example.reflector_android

import android.util.Xml
import com.example.reflector_android.network.Article
import okio.IOException
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.InputStream
import java.lang.IllegalStateException


class XmlParser {
    private val ns: String? = null

    @Throws(XmlPullParserException::class, IOException::class)
    suspend fun parse(inputStream: InputStream): MutableList<Article> {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readFeed(parser)
        }
    }

    suspend private fun readFeed(parser: XmlPullParser): MutableList<Article> {
        val items = mutableListOf<Article>()

        parser.require(XmlPullParser.START_TAG, ns, "rss")
        parser.nextTag()
        parser.require(XmlPullParser.START_TAG, ns, "channel")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            //looks for tag
            if (parser.name == "item") {
                items.add(readItem(parser))
            } else {
                skip(parser)
            }
        }
        return items
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
    @Throws(XmlPullParserException::class, java.io.IOException::class)
    suspend fun readItem(parser: XmlPullParser): Article {
        parser.require(XmlPullParser.START_TAG, ns, "item")
        var title: String? = null
        var description: String? = null
        var pubDate: String? = null
        var link: String? = null

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "title" -> title = readTitle(parser)
                "description" -> description = readDescription(parser)
                "pubDate" -> pubDate = readPubDate(parser)
                "link" -> link = readLink(parser)
                else -> skip(parser)
            }
        }
        return Article(title, description, pubDate, link)
    }

    //processes title tag
    @Throws(java.io.IOException::class, XmlPullParserException::class)
    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "title")
        return title
    }

    //processes link tags
    @Throws(java.io.IOException::class, XmlPullParserException::class)
    private fun readLink(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "link")
        val link = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "link")
        return link
    }

    @Throws(java.io.IOException::class, XmlPullParserException::class)
    private fun readPubDate(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate")
        val pubDate = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "pubDate")
        return pubDate
    }

    //processes description tag
    @Throws(java.io.IOException::class, XmlPullParserException::class)
    private fun readDescription(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "description")
        val description = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "description")
        return description
    }

    // extracts the text from title and description
    @Throws(java.io.IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    //skips tags we arent interested in
    @Throws(XmlPullParserException::class, java.io.IOException::class)
    fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}




