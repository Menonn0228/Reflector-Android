package com.example.reflector_android.ViewHolders

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.reflector_android.Activities.CategoryListActivity
import com.example.reflector_android.Activities.MainActivity
import com.example.reflector_android.Activities.WebviewActivity

class CategoryListViewHolder(val customView: View, var categoryName: String) : RecyclerView.ViewHolder(customView) {
    companion object {
        const val category = "category"
    }

    init {
        customView.setOnClickListener {
            val intent = Intent(customView.context, CategoryListActivity::class.java)
            intent.putExtra(category, categoryName)
            customView.context.startActivity(intent)
        }
    }
}