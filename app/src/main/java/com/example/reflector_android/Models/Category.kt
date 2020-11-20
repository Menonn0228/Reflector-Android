package com.example.reflector_android.Models

data class Category(val name: String , val rssVal: CategoryIdentifier , val emoji: String)
enum class CategoryIdentifier(val tag: String, val title: String, val emoji: String) {
    news("news", " News" , getEmoji(0x1F637)),
    covid19("covid-19", " COVID-19" , getEmoji(0x1F942)),
    life("life" , " Life and Entertainment" , getEmoji(0x1F30E)),
    opinion("opinion" , " Opinion" , getEmoji(0x1F4AC)),
    sports("sports" , " Sports" , getEmoji(0x1F3C8)),
}
fun getEmoji(unicode : Int): String {
    return String(Character.toChars(unicode))
}