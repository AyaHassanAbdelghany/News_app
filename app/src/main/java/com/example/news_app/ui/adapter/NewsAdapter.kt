package com.example.news_app.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news_app.R
import com.example.news_app.databinding.NewsItemBinding
import com.example.news_app.pojo.Articles


class NewsAdapter( private val context: Context): RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private var newsList: List<Articles> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentItem = newsList[position]

        holder.binding.apply {

            title.text = currentItem.title
            name.text = currentItem.source.name
            if(currentItem.description !=null &&  currentItem.description.length > 70){
                description.text = "${currentItem.description.slice(IntRange(0,70))}...."
            }else{
                description.text = currentItem.description

            }
            author.text = currentItem.author
            date.text = currentItem.publishedAt.slice(IntRange(0,9))

            holder.binding.cardView.setOnClickListener{
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(currentItem.url))
                context.startActivity(browserIntent)

            }

            holder.binding.imageShare.setOnClickListener {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT,"${currentItem.title}: ${currentItem.url}")
                sendIntent.type = "text/plain"
                context.startActivity(sendIntent)
            }

        }

        Glide.with(context).load(currentItem.urlToImage)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .into(holder.binding.imageNews)

    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun setData(articles: List<Articles>){
        this.newsList = articles
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: NewsItemBinding): RecyclerView.ViewHolder(binding.root)

//    private fun shareArticle(article: Articles){
//        val sharingIntent = Intent(Intent.ACTION_SEND)
//        sharingIntent.setType("text/plain")
//        sharingIntent.putExtra(Intent.EXTRA_TEXT, "${article.title} : ${article.url}")
//        context.startActivity(Intent.createChooser(sharingIntent, "Share Article"))
//    }

}