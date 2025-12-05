package com.example.yarni.data.model

import com.google.firebase.Timestamp

data class PatternDto(
    val date: Timestamp? = null,
    val filename: String = "",
    val name: String = "",
    val size: Int = 0
)