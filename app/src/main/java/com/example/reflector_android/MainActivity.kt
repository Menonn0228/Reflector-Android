package com.example.reflector_android

import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reflector_android.Models.Article
import com.example.reflector_android.Models.BlogRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate

open class MainActivity : AppCompatActivity() {
    var handler: Handler = Handler()
    lateinit var  layoutManager: LinearLayoutManager
    lateinit var adapter: BlogRecyclerAdapter
    var articles: MutableList<com.example.reflector_android.network.Article>? = null
    var moreArticles: MutableList<com.example.reflector_android.network.Article>? = null
    var isLoading = BlogRecyclerAdapter.isLoading
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RecyclerView.layoutManager = LinearLayoutManager(this)
        layoutManager = RecyclerView.layoutManager as LinearLayoutManager

        //This sets the coroutine to make requesting the data async
        GlobalScope.launch {
            val service = async { RSSService().fetchNews() }
            articles = service.await()
            runOnUiThread() {
                //This updates the recycler view with our parsed data. This has to be ran on the ui thread
                RecyclerView.adapter = BlogRecyclerAdapter(articles)
                adapter = RecyclerView.adapter as BlogRecyclerAdapter
                addScrollerListener(articles)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when(item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addScrollerListener(articls: MutableList<com.example.reflector_android.network.Article>?){
        RecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading){
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == articls?.size?.minus(1)){
                        loadMore(articls)
                        isLoading = true
                    }
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadMore(articlesList: MutableList<com.example.reflector_android.network.Article>?){
        var date: LocalDate? = null
        RecyclerView.post(Runnable {
            var nullArticle = com.example.reflector_android.network.Article("", "", date , "", "", isLoading)
            articlesList?.add(nullArticle)
            adapter.notifyItemInserted(articlesList?.size?.minus(1)!!)
        })

        RecyclerView.postDelayed(Runnable {
            var loadingRemoved = articlesList?.size?.minus(1)
            GlobalScope.launch {
                val service = async { RSSService().fetchMoreNews(articlesList?.size!!)}
                moreArticles = service.await()
                if (loadingRemoved != null) {
                    runOnUiThread {
                        articlesList?.removeAt(loadingRemoved)
                    }
                }
                if (articlesList != null) {
                    runOnUiThread {
                        adapter.notifyItemRemoved(articlesList.size)
                    }
                }
                for (i in moreArticles!!){
                    runOnUiThread {
                        articlesList?.add(i)
                        adapter.notifyItemInserted(articlesList?.size?.minus(1)!!)
                    }
                }
                isLoading = false
            }

        }, 2500)
    }
}
