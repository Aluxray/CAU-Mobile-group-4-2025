package com.example.yarni.ui.patterns.subpage

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.yarni.R
import com.example.yarni.data.firebase.PatternFirebaseDataSource
import com.example.yarni.data.repository.PatternRepositoryImpl
import com.example.yarni.databinding.FragmentPatternBinding
import com.example.yarni.ui.patterns.data.PatternsViewModel
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private const val ID = "id"
private const val TITLE = "title"
private const val DATE = "date"
private const val FILE_NAME = "fileName"
private const val FILE_SIZE = "fileSize"

class PatternFragment : Fragment() {

    private class PatternInfo(
        val id: String,
        var title: String,
        val date: String,
        val fileName: String,
        val fileSize: Int
    )

    private var info: PatternInfo? = null
    private var _binding: FragmentPatternBinding? = null
    private val binding get() = _binding!!

    private var isEditing = false

    private val viewModel: PatternsViewModel by viewModels {
        PatternsViewModel.Factory(
            PatternRepositoryImpl(PatternFirebaseDataSource())
        )
    }

    private val pickFile =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                val file = DocumentFile.fromSingleUri(requireContext(), uri)
                viewModel.addPattern(
                    uri,
                    fileName = file?.name ?: "defaultName",
                    fileSize = file?.length() ?: 0
                )
                Toast.makeText(requireContext(), "File added successfully!", Toast.LENGTH_SHORT).show()
            }
        }

    private fun saveTitle() {
        val newTitle = binding.patternTitleEdit.text.toString().trim()
        if (newTitle.isNotEmpty()) {
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

        binding.patternTitleEdit.visibility = View.GONE
        binding.patternTitle.visibility = View.VISIBLE
        binding.saveButton.visibility = View.GONE
        binding.renameButton.visibility = View.VISIBLE

        isEditing = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            val dateMillis = it.getString(DATE, "0")!!.toLong()

            val formattedDate =
                Instant.ofEpochMilli(dateMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("MMM dd yyyy"))

            info = PatternInfo(
                id = it.getString(ID, ""),
                title = it.getString(TITLE, "N/A"),
                date = formattedDate,
                fileName = it.getString(FILE_NAME, "N/A"),
                fileSize = it.getInt(FILE_SIZE, -1)
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

        binding.fabAdd.setOnClickListener {
            pickFile.launch("*/*")
        }

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

        binding.patternTitle.text = info!!.title
        binding.patternDate.text = info!!.date
        binding.patternFileName.text = info!!.fileName
        binding.patternFileType.text = info!!.fileName.substringAfterLast('.', "N/A").uppercase()

        val sizes = listOf(
            0 to "B",
            1000 to "Kb",
            1000000 to "Mb",
            1000000000 to "Gb"
        )
        val sizeInfo = sizes.findLast { pair -> pair.first <= info!!.fileSize } ?: sizes.first()
        binding.patternFileSize.text = getString(
            R.string.file_size_formatted,
            (info!!.fileSize.toFloat() / sizeInfo.first.toFloat()),
            sizeInfo.second
        )

        binding.renameButton.setOnClickListener {
            if (!isEditing) {
                binding.patternTitle.visibility = View.GONE
                binding.patternTitleEdit.visibility = View.VISIBLE
                binding.patternTitleEdit.setText(binding.patternTitle.text)
                binding.patternTitleEdit.requestFocus()

                binding.renameButton.visibility = View.GONE
                binding.saveButton.visibility = View.VISIBLE

                isEditing = true
            }
        }
        binding.saveButton.setOnClickListener {
            if (isEditing) saveTitle()
        }

        binding.patternTitleEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveTitle()
                true
            } else false
        }

        binding.downloadButton.setOnClickListener {
            Toast.makeText(requireContext(), "Download started...", Toast.LENGTH_SHORT).show()
            val toDownload = ByteArray(info!!.fileSize)
            val downloadedFile = File(context?.filesDir, info!!.fileName)
            downloadedFile.writeBytes(toDownload)
            Toast.makeText(requireContext(), "Download done!", Toast.LENGTH_SHORT).show()
        }

        binding.deleteButton.setOnClickListener {
            val result = Bundle().apply { putString("id", info?.id) }
            parentFragmentManager.setFragmentResult("itemDeleted", result)
            Toast.makeText(requireContext(), "File deleted successfully!", Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
