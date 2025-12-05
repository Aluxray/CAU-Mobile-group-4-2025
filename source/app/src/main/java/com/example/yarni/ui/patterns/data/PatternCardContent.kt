package com.example.yarni.ui.patterns.data

import com.example.yarni.R
import com.google.firebase.Timestamp
import java.time.LocalDateTime
import kotlin.random.Random

object PatternCardContent {

    var ITEMS: MutableList<PatternCardItem> = ArrayList()
    private val _ITEMS: MutableList<PatternCardItem> = ArrayList()

    private var _sortAscending = false

    val COLORS = listOf(
        R.color.pink,
        R.color.blue,
        R.color.purple,
        R.color.yellow,
        R.color.green,
    )

    val DEEP_COLORS = listOf(
        R.color.deep_pink,
        R.color.deep_blue,
        R.color.deep_purple,
        R.color.deep_yellow,
        R.color.deep_green,
    )

    fun setItems(patterns: List<PatternCardItem>) {
        _ITEMS.clear()
        ITEMS.clear()

        _ITEMS.addAll(patterns)
        ITEMS.addAll(patterns)

        sortByDate(_sortAscending)
    }

    fun search(query: String) {
        val q = query.trim().lowercase()
        ITEMS.clear()

        ITEMS.addAll(
            if (q.isNotEmpty())
                _ITEMS.filter { it.title.lowercase().contains(q) }
            else
                _ITEMS
        )

        sortByDate(_sortAscending)
    }

    fun sortByDate(ascending: Boolean = false) {
        if (ascending) ITEMS.sortBy { it.date }
        else ITEMS.sortByDescending { it.date }

        _sortAscending = ascending
    }
    data class PatternCardItem(
        val id: String,
        var title: String,
        val date: Timestamp,
        val fileName: String,
        val fileSize: Int,
        val data: ArrayList<Int>
    ) {
        val colorIndex: Int = Random.nextInt(COLORS.size)
    }
}
