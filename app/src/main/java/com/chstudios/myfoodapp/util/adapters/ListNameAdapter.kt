package com.chstudios.myfoodapp.util.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.chstudios.myfoodapp.R
import javax.inject.Inject

class ListNameAdapter @Inject constructor(): RecyclerView.Adapter<ListNameAdapter.ListViewHolder>() {

    var listNames = emptyList<String>()

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listNameView: TextView = itemView.findViewById(R.id.list_name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_list_item, parent, false)
        return ListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val current = listNames[position]
        holder.listNameView.text = current

        val args = Bundle()
        args.putString("list_name", current)

        holder.itemView.setOnClickListener{ view ->
            view.findNavController().navigate(R.id.action_mainFragment_to_inventoryFragment, args)
        }
    }

    internal fun setListNames(namesOfLists: List<String>) {
        this.listNames = namesOfLists
        notifyDataSetChanged()
    }

    override fun getItemCount() = listNames.size
}