package com.example.reflector_android

import android.os.Build
import android.util.Xml
import androidx.annotation.RequiresApi
import com.example.reflector_android.network.Article
import okio.IOException
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.InputStream
import java.lang.IllegalStateException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class XmlParser {
    companion object Tags {
        const val rss = "rss"
        const val channel = "channel"
        const val item = "item"
        const val title = "title"
        const val description = "description"
        const val pubDate = "pubDate"
        const val link = "link"
        const val author = "dc:creator"
    }
    private val nameSpace: String? = null

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

        parser.require(XmlPullParser.START_TAG, nameSpace, Tags.rss)
        parser.nextTag()
        parser.require(XmlPullParser.START_TAG, nameSpace, Tags.channel)
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            //looks for tag
            if (parser.name == Tags.item) {
                items.add(readItem(parser))
            } else {
                skip(parser)
            }
        }
        return items
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(XmlPullParserException::class, java.io.IOException::class)
    suspend fun readItem(parser: XmlPullParser): Article {
        parser.require(XmlPullParser.START_TAG, nameSpace, Tags.item)
        var title: String? = null
        var description: String? = null
        var pubDate: LocalDate? = null
        var link: String? = null
        var author: String? = null

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                Tags.title -> title = readTitle(parser)
                Tags.description -> description = readDescription(parser)
                Tags.pubDate -> pubDate = readPubDate(parser)
                Tags.link -> link = readLink(parser)
                Tags.author -> author = readAuthor(parser)
                else -> skip(parser)
            }
        }
        return Article(title, description, pubDate, link, author)
    }

    //processes title tag
    @Throws(java.io.IOException::class, XmlPullParserException::class)
    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, nameSpace, Tags.title)
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, nameSpace, Tags.title)
        return title
    }

    //processes link tags
    @Throws(java.io.IOException::class, XmlPullParserException::class)
    private fun readLink(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, nameSpace, Tags.link)
        val link = readText(parser)
        parser.require(XmlPullParser.END_TAG, nameSpace, Tags.link)
        return link
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(java.io.IOException::class, XmlPullParserException::class)
    private fun readPubDate(parser: XmlPullParser): LocalDate? {
        parser.require(XmlPullParser.START_TAG, nameSpace, Tags.pubDate)
        //val pubDate = readText(parser)
        val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM uuuu kk:mm:ss Z")
        val date = LocalDate.parse(readText(parser), formatter)
        val preferredFormatter = DateTimeFormatter.ofPattern("MM/dd/uuuu")
        date.format(preferredFormatter)
        parser.require(XmlPullParser.END_TAG, nameSpace, Tags.pubDate)
        return date
    }

    //processes description tag
    @Throws(java.io.IOException::class, XmlPullParserException::class)
    private fun readDescription(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, nameSpace, Tags.description)
        val description = readText(parser)
        parser.require(XmlPullParser.END_TAG, nameSpace, Tags.description)
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

    //processes creator tag
    @Throws(java.io.IOException::class, XmlPullParserException::class)
    private fun readAuthor(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, nameSpace, Tags.author)
        val authors = readText(parser)
        parser.require(XmlPullParser.END_TAG, nameSpace, Tags.author)
        return authors
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