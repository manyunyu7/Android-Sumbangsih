package com.feylabs.sumbangsih.presentation.notification

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.response.MNotificatinRes
import com.feylabs.sumbangsih.data.source.remote.response.NewsResponse
import com.feylabs.sumbangsih.databinding.ItemNotifBinding

class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private val notifList = mutableListOf<MNotificatinRes.ResData>()

    lateinit var notifInterface: OnNotifCompactClick

    fun setWholeData(addedList: MutableList<MNotificatinRes.ResData>) {
        notifList.clear()
        notifList.addAll(addedList)
    }

    inner class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemNotifBinding = ItemNotifBinding.bind(view)

        fun bind(model: MNotificatinRes.ResData) {
            binding.tvTitle.text = model.title
            binding.tvSubTitle.text = model.message
            binding.tvDesc.text = model.desc
            binding.tvDate.text = model.createdAt

            binding.root.setOnClickListener {
                if (notifInterface != null) {
                    notifInterface.onclick(model)
                }
            }

            if (model.isRead == 1) {
                binding.viewGreenLine.visibility = View.GONE
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notif, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifList[position])
    }

    override fun getItemCount(): Int {
        return notifList.size
    }

}

interface OnNotifCompactClick {
    fun onclick(model: MNotificatinRes.ResData)
}