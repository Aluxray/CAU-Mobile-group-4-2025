package com.example.yarni.data.repository

import com.example.yarni.data.firebase.PatternFirebaseDataSource
import com.example.yarni.data.model.PatternDto
import com.example.yarni.domain.model.Pattern
import com.example.yarni.domain.repository.PatternRepository
import com.google.firebase.Timestamp

class PatternRepositoryImpl(
    private val source: PatternFirebaseDataSource
) : PatternRepository {

    override suspend fun addPattern(pattern: Pattern) {
        val dto = PatternDto(
            date = Timestamp(pattern.date / 1000, 0),
            filename = pattern.filename,
            name = pattern.name,
            size = pattern.size
        )
        source.addPattern(pattern.id, dto)
    }

    override suspend fun getPattern(id: String): Pattern? {
        val dto = source.getPattern(id) ?: return null
        return Pattern(
            id = id,
            date = (dto.date?.seconds ?: 0) * 1000,
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
                date = (dto.date?.seconds ?: 0) * 1000,
                filename = dto.filename,
                name = dto.name,
                size = dto.size
            )
        }
    }
}
