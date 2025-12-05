package com.example.yarni.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Fragment의 View로 ComposeView를 설정하고, 그 안에 Composable UI를 정의합니다.
            setContent {
                // ViewModel의 LiveData를 관찰하고 State 객체로 변환합니다.
                // 데이터가 변경되면 UI가 자동으로 다시 그려집니다.
                val chartData by viewModel.chartData.observeAsState(initial = emptyList())
                val tips by viewModel.tips.observeAsState(initial = emptyList())

                // HomeScreen Composable을 호출하면서 관찰한 데이터를 파라미터로 전달합니다.
                HomeScreen(
                    chartSegments = chartData,
                    tips = tips,
                    onNavigate = { actionId ->
                        findNavController().navigate(actionId)
                    }
                )
            }
        }
    }
}
