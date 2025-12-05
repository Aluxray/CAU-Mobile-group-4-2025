package com.example.yarni.ui.patterns.data

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yarni.domain.model.Pattern
import com.example.yarni.domain.repository.PatternRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PatternsViewModel(
    private val repo: PatternRepository
) : ViewModel() {

    private val _patterns = MutableStateFlow<List<Pattern>>(emptyList())
    val patterns: StateFlow<List<Pattern>> = _patterns

    fun loadPatterns() = viewModelScope.launch {
        _patterns.value = repo.getPatterns()
    }

    fun addPattern(uri: Uri, fileName: String, fileSize: Long) {
        viewModelScope.launch {
            repo.addPattern(uri, fileName, fileSize)
            loadPatterns()
        }
    }

    fun updatePattern(id: String, newTitle: String) {
        viewModelScope.launch {
            repo.updatePattern(id, newTitle)
            loadPatterns() // recharge Firestore
        }
    }

    fun deletePattern(id: String) {
        viewModelScope.launch {
            repo.deletePattern(id)
            loadPatterns() // recharge Firestore
        }
    }

    class Factory(
        private val repository: PatternRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PatternsViewModel(repository) as T
        }
    }
}
