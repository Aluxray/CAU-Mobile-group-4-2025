package com.example.yarni.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                // ViewModel의 LiveData를 관찰하고 State 객체로 변환합니다.
                val chartData by viewModel.chartData.observeAsState(initial = emptyList())
                val tips by viewModel.tips.observeAsState(initial = emptyList())
                val totalSize by viewModel.totalSize.observeAsState("0 KB")
                val recentPatterns by viewModel.recentPatterns.observeAsState(initial = emptyList())
                val errorMessage by viewModel.errorMessage.observeAsState()

                // 에러 메시지가 있을 경우 Toast 등으로 표시
                errorMessage?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }

                // HomeScreen Composable을 호출하면서 관찰한 데이터를 파라미터로 전달합니다.
                HomeScreen(
                    chartSegments = chartData,
                    tips = tips,
                    totalSize = totalSize,
                    recentPatterns = recentPatterns,
                    onNavigate = { destinationId ->
                        findNavController().navigate(destinationId)
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 화면이 다시 보일 때마다 데이터를 새로고침하여 최신 상태를 반영
        viewModel.loadHomeScreenData()
    }
}
