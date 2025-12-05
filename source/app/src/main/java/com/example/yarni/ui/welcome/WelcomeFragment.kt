package com.example.yarni.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.yarni.R
import com.example.yarni.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createAccountButton = binding.createAccount
        val signInButton = binding.signIn

        createAccountButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_welcome_to_nav_register)
        }

        signInButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_welcome_to_nav_login)
        }
    }
}