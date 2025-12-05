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
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yarni.R
import com.example.yarni.databinding.FragmentPatternsListBinding
import com.example.yarni.ui.patterns.data.PatternCardContent

/**
 * A fragment representing a list of Items.
 */
class PatternsFragment : Fragment() {

    private var columnCount = 2

    private var sortAscending = false

    private var _binding: FragmentPatternsListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val pickFile =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                // Use contentResolver to read file
                val stream = requireContext().contentResolver.openInputStream(uri)
                // TODO: read / upload / parse, then close stream
            }
        }

    private fun refresh() {
        PatternCardContent.loadPatterns()
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatternsListBinding.inflate(inflater, container, false)

        // Setup navigation buttons
        binding.navHome.setOnClickListener {
            findNavController().navigate(R.id.action_nav_patterns_to_nav_home)
        }
        binding.navFolder.setColorFilter(
            ContextCompat.getColor(
                binding.navFolder.context,
                R.color.pink_secondary
            )
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
                adapter.updateTitle(PatternCardContent, itemId, newTitle)
            }
        }
        setFragmentResultListener("itemDeleted") { _, result ->
            val itemId = result.getString("id")
            if (itemId != null) {
                adapter.removeItem(PatternCardContent, itemId)
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
            // Rotate button to match sorting mode
            binding.sortButton.scaleY = if (sortAscending) -1f else 1f
            adapter.notifyDataSetChanged()
        }
        binding.list.layoutManager = if (columnCount <= 1) {
            LinearLayoutManager(context)
        } else {
            GridLayoutManager(context, columnCount)
        }
        binding.list.adapter = adapter
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            PatternsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}