package com.example.yarni.ui.patterns

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yarni.R
import com.example.yarni.databinding.FragmentPatternsListBinding
import com.example.yarni.ui.patterns.data.PatternCardContent
import com.example.yarni.data.repository.PatternRepositoryImpl
import com.example.yarni.data.firebase.PatternFirebaseDataSource
import com.example.yarni.ui.patterns.data.PatternsViewModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.util.Date

class PatternsFragment : Fragment() {

    private var columnCount = 2
    private var sortAscending = false

    private var _binding: FragmentPatternsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PatternsViewModel by viewModels {
        PatternsViewModel.Factory(
            PatternRepositoryImpl(PatternFirebaseDataSource())
        )
    }

    private val pickFile =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                val file = DocumentFile.fromSingleUri(requireContext(), uri)
                val stream = requireContext().contentResolver.openInputStream(uri)
                viewModel.addPattern(uri, fileName = file?.name?:"defaultName", fileSize = file?.length()?:0)
                stream?.close()
            }
        }


    private fun refresh() {
        viewModel.loadPatterns()  // On recharge Firestore
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        columnCount = arguments?.getInt(ARG_COLUMN_COUNT) ?: 2
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatternsListBinding.inflate(inflater, container, false)

        binding.navHome.setOnClickListener {
            findNavController().navigate(R.id.action_nav_patterns_to_nav_home)
        }
        binding.navFolder.setColorFilter(
            ContextCompat.getColor(binding.navFolder.context, R.color.pink_secondary)
        )

        binding.fabAdd.setOnClickListener {
            pickFile.launch("*/*")
        }

        val adapter = PatternsRecyclerViewAdapter(PatternCardContent.ITEMS) { item ->
            val bundle = Bundle().apply {
                putString("id", item.id)
                putString("title", item.title)
                putString("date", item.date.toString())
                putString("fileName", item.fileName)
                putInt("fileSize", item.fileSize)
            }
            findNavController().navigate(R.id.action_nav_patterns_to_nav_pattern, bundle)
        }

        setFragmentResultListener("titleChanged") { _, result ->
            val itemId = result.getString("id")
            val newTitle = result.getString("newTitle")
            if (itemId != null && newTitle != null) {
                viewModel.updatePattern(itemId, newTitle)
            }
        }

        setFragmentResultListener("itemDeleted") { _, result ->
            val itemId = result.getString("id")
            if (itemId != null) {
                viewModel.deletePattern(itemId)
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            refresh()
            adapter.notifyDataSetChanged()
        }

        binding.searchBar.addTextChangedListener { text ->
            PatternCardContent.search(text?.toString().orEmpty())
            adapter.notifyDataSetChanged()
        }

        binding.sortButton.setOnClickListener {
            sortAscending = !sortAscending
            PatternCardContent.sortByDate(sortAscending)
            binding.sortButton.scaleY = if (sortAscending) -1f else 1f
            adapter.notifyDataSetChanged()
        }

        binding.list.layoutManager =
            if (columnCount <= 1) LinearLayoutManager(context)
            else GridLayoutManager(context, columnCount)

        binding.list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.patterns.collect { patternList ->

                val items = patternList.map { p ->
                    PatternCardContent.PatternCardItem(
                        id = p.id,
                        title = p.name,
                        date = p.date,
                        fileName = p.filename,
                        fileSize = p.size
                    )
                }

                PatternCardContent.setItems(items)
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.loadPatterns()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            PatternsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
