package com.example.yarni.data.repository

import android.net.Uri
import com.example.yarni.data.firebase.PatternFirebaseDataSource
import com.example.yarni.data.model.PatternDto
import com.example.yarni.domain.model.Pattern
import com.example.yarni.domain.repository.PatternRepository
import com.google.firebase.Timestamp

class PatternRepositoryImpl(
    private val source: PatternFirebaseDataSource
) : PatternRepository {

    override suspend fun addPattern(uri: Uri, fileName: String, fileSize: Long) {
        source.addPattern(uri, fileName, fileSize)
    }

    override suspend fun getPattern(id: String): Pattern? {
        val dto = source.getPattern(id) ?: return null
        return Pattern(
            id = id,
            date = dto.date ?: Timestamp.now(),
            filename = dto.filename,
            name = dto.name,
            size = dto.size
        )
    }

    override suspend fun deletePattern(id: String) {
        source.deletePattern(id)
    }

    override suspend fun updatePattern(id: String, newTitle: String) {
        source.updatePattern(id, newTitle)
    }

    override suspend fun getPatterns(): List<Pattern> {
        return source.getPatterns().map { (id, dto) ->
            Pattern(
                id = id,
                date = dto.date ?: Timestamp.now(),
                filename = dto.filename,
                name = dto.name,
                size = dto.size
            )
        }
    }
}
