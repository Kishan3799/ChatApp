package com.kishan.chatapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.kishan.chatapp.R
import com.kishan.chatapp.databinding.RecieverMessageLayoutBinding
import com.kishan.chatapp.databinding.SentMessageLayoutBinding
import com.kishan.chatapp.modal.MessageModal

class MessageAdapter(var context: Context, var list: ArrayList<MessageModal>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_SENT = 1
    val ITEM_RECIEVE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_SENT)
            SentViewHolder(
                LayoutInflater.from(context).inflate(R.layout.sent_message_layout, parent, false)
            )
        else
            ReciverViewHolder(
                LayoutInflater.from(context).inflate(R.layout.reciever_message_layout,parent,false)
            )
    }

    override fun getItemViewType(position: Int): Int {
        return  if(FirebaseAuth.getInstance().uid == list[position].senderid) ITEM_SENT else ITEM_RECIEVE
    }

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = list[position]
        if (holder.itemViewType == ITEM_SENT){
            val viewHolder = holder as SentViewHolder
            viewHolder.binding.userMsg.text = message.message
        }else {
            val viewHolder = holder as ReciverViewHolder
            viewHolder.binding.userMsg.text = message.message
        }
    }

    inner class SentViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding = SentMessageLayoutBinding.bind(view)
    }

    inner class ReciverViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding = RecieverMessageLayoutBinding.bind(view)
    }
}
