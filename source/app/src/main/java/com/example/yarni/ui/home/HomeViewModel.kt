package com.example.yarni.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    // LiveData를 사용하여 UI 상태를 관리합니다.// 외부에서는 수정 불가능하도록 private _로 시작하는 MutableLiveData와 public LiveData로 분리합니다.
    private val _chartData = MutableLiveData<List<ChartSegment>>()
    val chartData: LiveData<List<ChartSegment>> = _chartData

    private val _tips = MutableLiveData<List<Tip>>()
    val tips: LiveData<List<Tip>> = _tips

    init {
        // ViewModel이 생성될 때 데이터 로딩을 시작합니다.
        loadData()
    }

    private fun loadData() {
        // 이 부분은 나중에 실제 데이터베이스나 네트워크 API에서 데이터를 가져오는 로직으로 대체됩니다.
        _chartData.value = listOf(
            ChartSegment(0.35f, ChartColors[0]),
            ChartSegment(0.25f, ChartColors[1]),
            ChartSegment(0.20f, ChartColors[2]),
            ChartSegment(0.20f, ChartColors[3])
        )
        _tips.value = listOf(
            Tip("Tip1", "코잡기"),
            Tip("Tip2", "짧은뜨기"),
            Tip("Tip3", "한길긴뜨기")
        )
    }
}
