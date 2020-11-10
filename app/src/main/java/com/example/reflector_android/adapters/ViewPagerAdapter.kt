package com.example.reflector_android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reflector_android.R
import kotlinx.android.synthetic.main.viewpager_item.view.*

class ViewPagerAdapter(val tabViews: List<Int>) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {
    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewpager_item, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tabViews.size
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {

    }
}