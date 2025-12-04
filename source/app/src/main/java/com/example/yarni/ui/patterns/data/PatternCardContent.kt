package com.example.yarni.ui.patterns.data

import com.example.yarni.R
import java.time.LocalDateTime
import kotlin.random.Random

/**
 * Helper class for providing content for user interfaces
 */
object PatternCardContent {

    /**
     * Array of visible items.
     */
    var ITEMS: MutableList<PatternCardItem> = ArrayList()

    /**
     * Array of all items.
     */
    private val _ITEMS: MutableList<PatternCardItem> = ArrayList()

    private var _sortAscending = false

    val COLORS: List<Int> = listOf(
        R.color.pink,
        R.color.blue,
        R.color.purple,
        R.color.yellow,
        R.color.green,
    )

    val DEEP_COLORS: List<Int> = listOf(
        R.color.deep_pink,
        R.color.deep_blue,
        R.color.deep_purple,
        R.color.deep_yellow,
        R.color.deep_green,
    )

    private val COUNT = 25

    init {
        // TODO: call REST API for items and delete COUNT
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createPlaceholderItem(i))
        }
        sortByDate(_sortAscending)
    }

    private fun addItem(item: PatternCardItem) {
        _ITEMS.add(item)
        ITEMS.add(item)
    }

    private fun createPlaceholderItem(position: Int): PatternCardItem {
        return PatternCardItem(
            position.toString(),
            "Item $position",
            LocalDateTime.parse("2020-12-10T12:00:${if (position < 10) "0$position" else position}")
        )
    }

    fun search(query: String) {
        val q = query.trim().lowercase()
        ITEMS.clear()
        ITEMS.addAll(
            if (q.isNotEmpty())
                _ITEMS.filter {
                    it.title.lowercase().contains(q)
                }
            else
                _ITEMS
        )
        sortByDate(_sortAscending)
    }

    fun sortByDate(ascending: Boolean = false) {
        if (ascending) {
            ITEMS.sortBy { it.date }
        } else {
            ITEMS.sortByDescending { it.date }
        }
        _sortAscending = ascending
    }

    /**
     * Class for pattern cards data handling.
     */
    data class PatternCardItem(
        val id: String,
        val title: String,
        val date: LocalDateTime
    ) {
        val colorIndex: Int = Random.nextInt(COLORS.size)

        override fun toString(): String = title
    }
}