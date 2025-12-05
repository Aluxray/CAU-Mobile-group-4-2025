package com.example.yarni.domain.model

import com.google.firebase.Timestamp

data class Pattern(
    val id: String = "",
    val date: Timestamp = Timestamp.now(),
    val filename: String = "",
    val name: String = "",
    val size: Int = 0
)
