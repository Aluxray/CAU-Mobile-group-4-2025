package com.example.yarni.ui.patterns

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.yarni.R
import com.example.yarni.databinding.PatternsCardBinding
import com.example.yarni.ui.patterns.data.PatternCardContent
import com.example.yarni.ui.patterns.data.PatternCardContent.PatternCardItem
import java.time.format.DateTimeFormatter
import kotlin.random.Random

/**
 * [RecyclerView.Adapter] that can display a [PatternCardItem].
 */
class PatternsRecyclerViewAdapter(
    private val values: List<PatternCardItem>
) : RecyclerView.Adapter<PatternsRecyclerViewAdapter.ViewHolder>() {
    private var visibleValues: List<PatternCardItem> = values

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            PatternsCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = visibleValues[position]
        holder.titleView.text = item.title
        holder.dateView.text = item.date.format(DateTimeFormatter.ofPattern("MMM dd.yyyy"))

        // Set color of card
        val color = PatternCardContent.COLORS[item.colorIndex]
        val deepColor = PatternCardContent.DEEP_COLORS[item.colorIndex]
        holder.card.setCardBackgroundColor(ContextCompat.getColor(holder.card.context, color))
        holder.folderIcon.setColorFilter(
            ContextCompat.getColor(
                holder.folderIcon.context,
                deepColor
            )
        )
        holder.moreButton.setColorFilter(
            ContextCompat.getColor(
                holder.moreButton.context,
                deepColor
            )
        )
        holder.titleView.setTextColor(ContextCompat.getColor(holder.titleView.context, deepColor))
        holder.dateView.setTextColor(ContextCompat.getColor(holder.dateView.context, deepColor))
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: PatternsCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val titleView: TextView = binding.textTitle
        val dateView: TextView = binding.textDate
        val card = binding.card
        val folderIcon = binding.folderIcon
        val moreButton = binding.moreButton

        override fun toString(): String {
            return super.toString() + " '" + titleView.text + "'"
        }
    }

}