package com.feylabs.sumbangsih.presentation.pengajuan.history

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.response.GetHistoryResponse
import com.feylabs.sumbangsih.data.source.remote.response.NewsResponse
import com.feylabs.sumbangsih.databinding.ItemTimelineHistoryBinding

class HistoryTimelineAdapter : RecyclerView.Adapter<HistoryTimelineAdapter.HistoryViewHolder>() {

    private val historyList = mutableListOf<GetHistoryResponse.ResData>()

    val listRole = mutableListOf<String>()
    val listStatus = mutableListOf<String>()

    fun setWholeData(addedList: MutableList<GetHistoryResponse.ResData>) {
        historyList.clear()
        historyList.addAll(addedList)
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemTimelineHistoryBinding = ItemTimelineHistoryBinding.bind(view)

        fun bind(model: GetHistoryResponse.ResData) {

            binding.tvDesc.text = model.message.toString()
            val position = adapterPosition
            binding.tvTitle.text = model.title.toString()

            if (position != 0) {
                val context = binding.root.context

                binding.indicatorCircle.setImageDrawable(
                    ContextCompat.getDrawable(
                        context, R.drawable.ic_circle_grey
                    )
                )

                listRole.add(model.role)
                listStatus.add(model.status)

                binding.tvTitle.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.grey_7c
                    )
                )
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timeline_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

}

