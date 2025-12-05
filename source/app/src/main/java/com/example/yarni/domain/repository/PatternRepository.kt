package com.example.yarni.domain.repository

import android.net.Uri
import com.example.yarni.domain.model.Pattern

interface PatternRepository {

    suspend fun addPattern(uri: Uri, fileName: String, fileSize: Long)
    suspend fun getPattern(id: String): Pattern?
    suspend fun deletePattern(id: String)
    suspend fun updatePattern(id: String, newTitle: String)
    suspend fun getPatterns(): List<Pattern>
}