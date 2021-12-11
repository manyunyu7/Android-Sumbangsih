package com.feylabs.sumbangsih.presentation.ui.home.news

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.response.NewsResponse
import com.feylabs.sumbangsih.databinding.ItemNewsBinding
import com.feylabs.sumbangsih.databinding.ItemNewsCompactBinding
import com.feylabs.sumbangsih.util.ImageView.loadImage

class NewsCompactAdapter : RecyclerView.Adapter<NewsCompactAdapter.NewsViewHolder>() {

    private val newsList = mutableListOf<NewsResponse.NewsResponseItem>()

    lateinit var newsGridClickAdapter: OnNewsCompactClick

    fun setWholeData(addedList: MutableList<NewsResponse.NewsResponseItem>) {
        newsList.clear()
        newsList.addAll(addedList)
    }

    inner class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemNewsCompactBinding = ItemNewsCompactBinding.bind(view)

        fun bind(model: NewsResponse.NewsResponseItem) {
            binding.tvMain.text = model.title
            binding.ivMainImage.loadImage(model.photoPath, false)

            binding.root.setOnClickListener {
                if (newsGridClickAdapter != null) {
                    newsGridClickAdapter.onclick(model)
                }
            }

            var text = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                text = (Html.fromHtml("${model?.dateIndo}", Html.FROM_HTML_MODE_COMPACT)).toString()
            } else {
                text = (Html.fromHtml("${model?.dateIndo}")).toString()
            }

            binding.tvDescription.text = text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_compact, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

}

interface OnNewsCompactClick {
    fun onclick(model: NewsResponse.NewsResponseItem)
}