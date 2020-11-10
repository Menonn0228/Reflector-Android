package com.example.reflector_android.activities

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reflector_android.adapters.BlogRecyclerAdapter
import com.example.reflector_android.R
import com.example.reflector_android.RSSService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate

open class HomeTabActivity : AppCompatActivity() {
    lateinit var  recyclerView: RecyclerView
    lateinit var  layoutManager: LinearLayoutManager
    lateinit var adapter: BlogRecyclerAdapter
    var articles: MutableList<com.example.reflector_android.network.Article>? = null
    var moreArticles: MutableList<com.example.reflector_android.network.Article>? = null
    var isLoading = BlogRecyclerAdapter.isLoading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewpager_item)
        recyclerView = findViewById(R.id.home_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        layoutManager = recyclerView.layoutManager as LinearLayoutManager

        //This sets the coroutine to make requesting the data async
        GlobalScope.launch {
            val service = async { RSSService().fetchNews() }
            articles = service.await()
            runOnUiThread() {
                //This updates the recycler view with our parsed data. This has to be ran on the ui thread
                recyclerView.adapter = BlogRecyclerAdapter(articles)
                adapter = recyclerView.adapter as BlogRecyclerAdapter
                addScrollerListener(articles)
            }
        }
    }

    //This monitors where the user is in relation to the list. When they get to the end, the load function runs.
    private fun addScrollerListener(articles: MutableList<com.example.reflector_android.network.Article>?) {
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    var lastArticlePosition = articles?.size?.minus(1)
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == lastArticlePosition) {
                        loadMoreArticles(articles)
                        isLoading = true
                    }
                }
            }
        })
    }

    //This loads more articles and adds them to the list and view
    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadMoreArticles(list: MutableList<com.example.reflector_android.network.Article>?){
        val delay: Long = 2500
        val date: LocalDate? = null
        if (list == null){
            System.err.println("article list is null. please restart")
            Toast.makeText(applicationContext, "An unexpected Error occurred. Please restart the app.", Toast.LENGTH_SHORT).show()
            return
        }

        recyclerView.post(Runnable {
            val nullArticle = com.example.reflector_android.network.Article("", "", date , "", null)
            list.add(nullArticle)
            val listSize = list.size.minus(1)
            if (listSize != 0){
                adapter.notifyItemInserted(listSize)
            }
        })

        //delay to show the user that we are loading more articles
        recyclerView.postDelayed(Runnable {
            val loadingRemoved: Int?  = list.size.minus(1)
            GlobalScope.launch {
                val service = async { RSSService().fetchMoreNews(list.size)}
                moreArticles = service.await()
                if (loadingRemoved == null) {
                    System.err.println("loadingRemoved is null")
                    runOnUiThread {
                        Toast.makeText(applicationContext, "An unexpected Error occurred. Please restart the app.", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                runOnUiThread {
                    list.removeAt(loadingRemoved)
                    adapter.notifyItemRemoved(list.size)
                }

                if (moreArticles == null) {
                    System.err.println("moreArticles is null")
                    runOnUiThread {
                        Toast.makeText(applicationContext, "An unexpected Error occurred. Please restart the app.", Toast.LENGTH_SHORT).show()
                    }

                    return@launch
                }

                for (i in moreArticles!!) {
                    runOnUiThread {
                        list.add(i)
                        adapter.notifyItemInserted(list.size.minus(1))
                    }
                }
                isLoading = false
            }
        }, delay)
    }
}