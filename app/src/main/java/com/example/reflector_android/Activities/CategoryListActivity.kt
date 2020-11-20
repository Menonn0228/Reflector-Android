package com.example.reflector_android.Activities

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reflector_android.Adapters.BlogRecyclerAdapter
import com.example.reflector_android.R
import com.example.reflector_android.RSSService
import com.example.reflector_android.ViewHolders.CategoryListViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate

class CategoryListActivity(): AppCompatActivity()  {
    lateinit var  layoutManager: LinearLayoutManager
    lateinit var adapter: BlogRecyclerAdapter
    lateinit var category: String
    var articles: MutableList<com.example.reflector_android.network.Article>? = null
    var moreArticles: MutableList<com.example.reflector_android.network.Article>? = null
    var isLoading = BlogRecyclerAdapter.isLoading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_articles)
        category = intent.getStringExtra(CategoryListViewHolder.category)
        val myActionBar: ActionBar? = supportActionBar

        if (myActionBar != null) {
            myActionBar.title = getCategory(category)
        }

        RecyclerView.layoutManager = LinearLayoutManager(this)
        layoutManager = RecyclerView.layoutManager as LinearLayoutManager

        //This sets the coroutine to make requesting the data async
        GlobalScope.launch {
            val service = async { RSSService().fetchNewsbyCategory(category) }
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    //This monitors where the user is in relation to the list. When they get to the end, the load function runs.
    private fun addScrollerListener(articles: MutableList<com.example.reflector_android.network.Article>?) {
        RecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    val lastArticlePosition = articles?.size?.minus(1)
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
    private fun loadMoreArticles(list: MutableList<com.example.reflector_android.network.Article>?) {
        val delay: Long = 2500
        val date: LocalDate? = null
        if (list == null){
            System.err.println("article list is null. please restart")
            Toast.makeText(applicationContext, "An unexpected Error occurred. Please restart the app.", Toast.LENGTH_SHORT).show()
            return
        }

        RecyclerView.post(Runnable {
            val nullArticle = com.example.reflector_android.network.Article("", "", date , "", null)
            list.add(nullArticle)
            val listSize = list.size.minus(1)
            if (listSize != 0){
                adapter.notifyItemInserted(listSize)
            }
        })

        //delay to show the user that we are loading more articles
        RecyclerView.postDelayed(Runnable {
            val loadingRemoved: Int?  = list.size.minus(1)
            GlobalScope.launch {
                val service = async { RSSService().fetchMoreNews(list.size , category)}
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

    fun getCategory(categoryID: String): String {
        if (categoryID == "covid-19"){
            return "COVID-19"
        }
        if (categoryID == "life"){
            return "Life and Entertainment"
        }
        if (categoryID == "news"){
            return "News"
        }
        if (categoryID == "opinion"){
            return "Opinion"
        }
        if (categoryID == "sports"){
            return "Sports"
        }
        else {
            return "Reflector"
        }
    }
}