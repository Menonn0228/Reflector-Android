package com.example.reflector_android.Activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reflector_android.Adapters.CategoryRecyclerAdapter
import com.example.reflector_android.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.categories_list.*

class CategoryActivity(): AppCompatActivity()  {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var  layoutManager: LinearLayoutManager
    lateinit var Categoryadapter: CategoryRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.categories_list)
        CategoryRecyclerView.layoutManager = LinearLayoutManager(this)
        layoutManager = CategoryRecyclerView.layoutManager as LinearLayoutManager
        CategoryRecyclerView.adapter = CategoryRecyclerAdapter()
        Categoryadapter = CategoryRecyclerView.adapter as CategoryRecyclerAdapter
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
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

    private fun setUpNavigationDrawer() {
        toggle = ActionBarDrawerToggle(this, dl_drawer_layoutTwo, R.string.open,R.string.close)
        dl_drawer_layoutTwo.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nv_navViewTwo.setNavigationItemSelectedListener {
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