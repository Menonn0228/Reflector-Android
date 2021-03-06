package com.example.reflector_android.Activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reflector_android.Adapters.BlogRecyclerAdapter
import com.example.reflector_android.Adapters.CategoryRecyclerAdapter
import com.example.reflector_android.R
import com.example.reflector_android.RSSService
import com.example.reflector_android.ViewHolders.ArticleListViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.categories_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate

open class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
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
        setUpNavigationDrawer()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //This monitors where the user is in relation to the list. When they get to the end, the load function runs.
    private fun addScrollerListener(articles: MutableList<com.example.reflector_android.network.Article>?) {
        RecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
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
//
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
                val service = async { RSSService().fetchMoreNews(list.size , null)}
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

    private fun setUpNavigationDrawer() {
        toggle = ActionBarDrawerToggle(this, dl_drawer_layout, R.string.open,R.string.close)
        dl_drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nv_navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    finish()
                    this.startActivity(intent)
                }
                R.id.nav_categories -> {
                    val intent = Intent(this, CategoryActivity::class.java)
                    finish()
                    this.startActivity(intent)
                }
            }
            true
        }
    }
}
