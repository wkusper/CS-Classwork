package com.example.grocerylist

import GroceryItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GroceryListAdapter(
    private val items: List<GroceryItem>,
    private val onDelete: (GroceryItem) -> Unit
) : RecyclerView.Adapter<GroceryListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val itemDetailsTextView: TextView = itemView.findViewById(R.id.detailsTextView)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grocery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemNameTextView.text = item.name
        holder.itemDetailsTextView.text = "Quantity: ${item.quantity}, Buy Before: ${item.buyBeforeDate}, Price: $${item.price}, Added By: ${item.addedBy}"

        holder.deleteButton.setOnClickListener { onDelete(item) }
    }

    override fun getItemCount() = items.size
}