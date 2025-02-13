package com.example.grocerylist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListItemAdapter(
    private val listItems: List<ListItem>,
    private val context: Context
) : RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder>() {

    class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listItemNameTextView: TextView = itemView.findViewById(R.id.listItemNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_list, parent, false)
        return ListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val listItem = listItems[position]
        holder.listItemNameTextView.text = listItem.name

        // Handle click events to go to JoinListActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(context, JoinListActivity::class.java)
            intent.putExtra("token", listItem.token)  // Pass the token to JoinListActivity
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listItems.size
}
