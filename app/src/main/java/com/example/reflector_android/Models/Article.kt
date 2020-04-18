package com.example.reflector_android.Models


// The Article class is a model that represents the data that an Article in the Reflector would have.
// The input parameters of this article are what are returned when you do an RSS GET request from reflector tab like reflector-online.com/news/
data class Article(var title: String, var desc: String, var pubDate: String, var link: String, var creator: String) {




}