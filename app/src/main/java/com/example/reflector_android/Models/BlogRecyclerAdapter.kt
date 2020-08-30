package com.example.reflector_android.Models

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.reflector_android.R
import kotlinx.android.synthetic.main.article_list_item.view.*

class BlogRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var items: List<BlogPost> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return items.size

    }


    class BlogViewHolder constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView) {

        val blog_image: ImageView = itemView.blog_image
        val blog_title: TextView = itemView.blog_title

        fun bind(blogPost: BlogPost){

            blog_title.setText(blogPost.title)

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
            Glide.with(itemView.context)
                .load(blogPost.image)
                .into(blogImage)


        }
    }


}