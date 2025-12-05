package com.example.yarni.ui.patterns.subpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.yarni.R
import com.example.yarni.databinding.FragmentPatternBinding
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val ID = "id"
private const val TITLE = "title"
private const val DATE = "date"
private const val FILE_NAME = "fileName"
private const val FILE_SIZE = "fileSize"
private const val FILE_DATA = "data"

class PatternFragment : Fragment() {

    private class PatternInfo(
        val id: String,
        var title: String,
        val date: LocalDateTime,
        val fileName: String,
        val fileSize: Int,
        val data: ArrayList<Int>
    )

    private var info: PatternInfo? = null
    private var _binding: FragmentPatternBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Text editing properties
    private var isEditing = false

    private fun saveTitle() {
        val newTitle = binding.patternTitleEdit.text.toString().trim()
        if (newTitle.isNotEmpty()) {
            // Notify previous fragment with new title
            val result = Bundle().apply {
                putString("id", info?.id)
                putString("newTitle", newTitle)
            }
            parentFragmentManager.setFragmentResult("titleChanged", result)
            info?.title = newTitle
            binding.patternTitle.text = newTitle
        } else {
            binding.patternTitle.text = info?.title
        }

        // Hide EditText, show TextView
        binding.patternTitleEdit.visibility = View.GONE
        binding.patternTitle.visibility = View.VISIBLE

        // Hide Save button, show Rename button
        binding.saveButton.visibility = View.GONE
        binding.renameButton.visibility = View.VISIBLE

        isEditing = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            info = PatternInfo(
                id = it.getString(ID, ""),
                title = it.getString(TITLE, "N/A"),
                date = LocalDateTime.parse(it.getString(DATE, LocalDateTime.MIN.toString())),
                fileName = it.getString(FILE_NAME, "N/A"),
                fileSize = it.getInt(FILE_SIZE, -1),
                data = it.getIntegerArrayList(FILE_DATA) ?: arrayListOf()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPatternBinding.inflate(inflater, container, false)

        if (info == null) {
            Toast.makeText(requireContext(), "Error loading pattern", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return null
        }

        // Setup navigation buttons
        binding.navHome.setOnClickListener {
            findNavController().navigate(R.id.action_nav_pattern_to_nav_home)
        }
        binding.navFolder.setOnClickListener {
            findNavController().navigate(R.id.action_nav_pattern_to_nav_patterns)
        }
        binding.navFolder.setColorFilter(
            ContextCompat.getColor(
                binding.navFolder.context,
                R.color.pink_secondary
            )
        )

        // Setup text values
        binding.patternTitle.text = info!!.title
        binding.patternDate.text = info!!.date.format(DateTimeFormatter.ofPattern("MMM dd.yyyy"))
        binding.patternFileName.text = info!!.fileName
        binding.patternFileType.text = info!!.fileName.substringAfterLast('.', "N/A").uppercase()
        val sizes = listOf(
            Pair(0, "B"),
            Pair(1000, "Kb"),
            Pair(1000000, "Mb"),
            Pair(1000000000, "Gb")
        )
        val sizeInfo = sizes.findLast { pair -> pair.first <= info!!.fileSize } ?: sizes.first()
        binding.patternFileSize.text = getString(
            R.string.file_size_formatted,
            (info!!.fileSize.toFloat() / sizeInfo.first.toFloat()),
            sizeInfo.second
        )

        // Setup buttons behavior

        // Rename button
        binding.renameButton.setOnClickListener {
            if (!isEditing) {
                // Hide TextView, show EditText, set text
                binding.patternTitle.visibility = View.GONE
                binding.patternTitleEdit.visibility = View.VISIBLE
                binding.patternTitleEdit.setText(binding.patternTitle.text)
                binding.patternTitleEdit.requestFocus()

                // Hide Rename button, show Save button
                binding.renameButton.visibility = View.GONE
                binding.saveButton.visibility = View.VISIBLE

                isEditing = true
            }
        }
        binding.saveButton.setOnClickListener {
            if (isEditing) {
                saveTitle()
            }
        }
        binding.patternTitleEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveTitle()
                true
            } else
                false
        }
        // End of rename button

        binding.downloadButton.setOnClickListener {
            Toast.makeText(requireContext(), "Download started...", Toast.LENGTH_SHORT).show()
            val toDownload = info!!.data.map { it.toByte() }.toByteArray()
            val downloadedFile = File(context?.filesDir, info!!.fileName)
            downloadedFile.writeBytes(toDownload)
            Toast.makeText(requireContext(), "Download done!", Toast.LENGTH_SHORT).show()
        }
        binding.deleteButton.setOnClickListener {
            val result = Bundle().apply {
                putString("id", info?.id)
            }
            parentFragmentManager.setFragmentResult("itemDeleted", result)
            Toast.makeText(requireContext(), "File deleted successfully!", Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment PatternFragment.
         */
        @JvmStatic
        fun newInstance(
            id: String,
            title: String,
            date: String,
            fileName: String,
            fileSize: Int,
            data: ArrayList<Int>
        ) =
            PatternFragment().apply {
                arguments = Bundle().apply {
                    putString(ID, id)
                    putString(TITLE, title)
                    putString(DATE, date)
                    putString(FILE_NAME, fileName)
                    putInt(FILE_SIZE, fileSize)
                    putIntegerArrayList(FILE_DATA, data)
                }
            }
    }
}