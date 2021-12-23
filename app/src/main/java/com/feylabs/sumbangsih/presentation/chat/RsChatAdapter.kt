package com.feylabs.sumbangsih.presentation.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.sumbangsih.R
import com.feylabs.sumbangsih.data.source.remote.response.ListChatResponse
import com.feylabs.sumbangsih.databinding.ItemRschatBinding
import com.feylabs.sumbangsih.util.ImageView.loadImage
import com.feylabs.sumbangsih.util.sharedpref.RazPreferenceHelper

class RsChatAdapter : RecyclerView.Adapter<RsChatAdapter.RsChatViewHolder>() {

    val data = mutableListOf<ListChatResponse.ResData>()
    lateinit var itemInterface: RsChatItemInterface


    fun setInterface(itemInterface: RsChatItemInterface) {
        this.itemInterface = itemInterface
    }

    inner class RsChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemRschatBinding.bind(view)

        fun bind(data: ListChatResponse.ResData) {
            val mContext = binding.root.context
            val userID = RazPreferenceHelper.getUserId(mContext)

            // if chat if from current user
            if (userID == data.senderId.toString()) {
                binding.containerChatRight.visibility = View.VISIBLE
                binding.containerChatLeft.visibility = View.GONE

//                binding.ivProfileRight.loadImage(mContext, data. senderPhoto)
                binding.tvUserNameRight.visibility = View.GONE
                binding.tvRight.text = data.message
                binding.tvTimeRight.text = data.createdAt.toString()
            }

            // if chat is not from current user
            if (userID != data.senderId.toString()) {
                binding.containerChatLeft.visibility = View.VISIBLE
                binding.containerChatRight.visibility = View.GONE

//                binding.ivProfileLeft.loadImageFromURL(mContext, data.senderPhoto)
                binding.tvUserNameLeft.visibility = View.GONE
                binding.tvLeft.text = data.message
                binding.tvTimeLeft.text = data.createdAt.toString()
            }

        }
    }


    fun addData(model: MutableList<ListChatResponse.ResData>) {
        this.data.clear()
        data.addAll(model)
        notifyDataSetChanged()
    }

    fun insertItem(model: ListChatResponse.ResData) {
        data.add(model)
        notifyItemInserted(data.size + 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RsChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rschat, parent, false)
        return RsChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RsChatViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface RsChatItemInterface {
        fun onclick(model: ListChatResponse.ResData)
    }

}


