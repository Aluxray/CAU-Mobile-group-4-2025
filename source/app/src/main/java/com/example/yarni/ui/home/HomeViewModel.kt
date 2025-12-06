package com.example.yarni.ui.home

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yarni.domain.model.Pattern
import com.example.yarni.domain.repository.PatternRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val patternRepository: PatternRepository
) : ViewModel() {

    // Storage Chart 데이터
    private val _chartData = MutableLiveData<List<ChartSegment>>()
    val chartData: LiveData<List<ChartSegment>> = _chartData

    private val _totalSize = MutableLiveData("0 KB")
    val totalSize: LiveData<String> = _totalSize

    // Recent Patterns 데이터
    private val _recentPatterns = MutableLiveData<List<Pattern>>()
    val recentPatterns: LiveData<List<Pattern>> = _recentPatterns

    // Tips 데이터 (기존 로직 유지)
    private val _tips = MutableLiveData<List<Tip>>()
    val tips: LiveData<List<Tip>> = _tips

    // UI 상태 (로딩, 에러 등)
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        // ViewModel 생성 시 데이터 로드 시작
        loadHomeScreenData()

        // 기존 Tips 데이터 로드
        loadTips()
    }

    // 화면에 필요한 모든 데이터를 Firebase에서 로드하는 함수
    fun loadHomeScreenData() {
        viewModelScope.launch {
            try {
                // Repository를 통해 모든 패턴 데이터를 가져옵니다.
                val patterns = patternRepository.getPatterns()

                // 1. 저장 공간 데이터 계산
                calculateStorageData(patterns)

                // 2. 최근 추가된 파일 데이터 필터링
                // 'date' 필드를 기준으로 내림차순 정렬 후 상위 5개만 선택합니다.
                val recent = patterns.sortedByDescending { it.date }.take(5)
                _recentPatterns.value = recent

            } catch (e: Exception) {
                _errorMessage.value = "데이터를 불러오는 데 실패했습니다: ${e.message}"
            }
        }
    }

    private fun calculateStorageData(patterns: List<Pattern>) {
        if (patterns.isEmpty()) {
            _totalSize.value = "0 KB"
            _chartData.value = emptyList()
            return
        }

        // 전체 파일 크기 합산 (PatternDto의 size가 Int이므로 toLong()으로 변환)
        val total = patterns.sumOf { it.size.toLong() }
        _totalSize.value = formatFileSize(total)

        // 차트 데이터 생성 (여기서는 모든 패턴을 하나의 카테고리로 간주)
        // TODO: 추후 파일 타입이나 다른 기준으로 세그먼트를 나눠야 합니다.
        if (total > 0) {
            _chartData.value = listOf(
                ChartSegment(1.0f, ChartColors[0]) // 100%를 하나의 세그먼트로 표시
            )
        } else {
            _chartData.value = emptyList()
        }
    }

    // 파일 크기를 GB, MB, KB 단위로 변환해주는 유틸리티 함수
    private fun formatFileSize(size: Long): String {
        if (size <= 0) return "0 KB"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
    }

    // 기존의 Tips 데이터 로딩 함수
    private fun loadTips() {
        _tips.value = listOf(
            Tip("Tip1", "코잡기"),
            Tip("Tip2", "짧은뜨기"),
            Tip("Tip3", "한길긴뜨기")
        )
    }
}
