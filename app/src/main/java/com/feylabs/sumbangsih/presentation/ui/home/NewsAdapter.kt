package com.feylabs.sumbangsih.presentation.ui.home

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.response.NewsResponse
import com.feylabs.sumbangsih.databinding.ItemNewsBinding
import com.feylabs.sumbangsih.util.ImageView.loadImage

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private val newsList = mutableListOf<NewsResponse.NewsResponseItem>()

    lateinit var newsGridClickAdapter: OnNewsGridClick

    fun setWholeData(addedList: MutableList<NewsResponse.NewsResponseItem>) {
        newsList.clear()
        newsList.addAll(addedList)
        newsList.shuffle()
        notifyDataSetChanged()
    }

    inner class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemNewsBinding = ItemNewsBinding.bind(view)

        fun bind(model: NewsResponse.NewsResponseItem) {
            binding.labelNewsTitle.text = model.title
            binding.imagePlaceholder.loadImage(model.photoPath, false)

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

            binding.labelNewsDesc.text = text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

}

interface OnNewsGridClick {
    fun onclick(model: NewsResponse.NewsResponseItem)
}