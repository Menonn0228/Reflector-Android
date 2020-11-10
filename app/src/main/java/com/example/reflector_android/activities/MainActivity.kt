package com.example.reflector_android.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.example.reflector_android.R
import com.example.reflector_android.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

open class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private var tab_names: List<Int> = listOf(R.string.tab_home, R.string.tab_saved, R.string.tab_categories)
    private var tab_icons: List<Int> = listOf(R.drawable.ic_home_24, R.drawable.ic_saved_24, R.drawable.ic_categories_24)
    private var tab_views: List<Int> = listOf(R.id.home_RecyclerView, R.id.saved_RecyclerView, R.id.categories_RecyclerView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = ViewPagerAdapter(tab_views)
        tabLayout = findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(tab_names[position])
            tab.icon = getDrawable(tab_icons[position])
        }.attach()
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
}
