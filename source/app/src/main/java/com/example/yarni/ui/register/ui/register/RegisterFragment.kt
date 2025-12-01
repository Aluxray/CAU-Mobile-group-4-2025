package com.example.yarni.ui.register.ui.register

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.yarni.databinding.FragmentRegisterBinding

import com.example.yarni.R
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : Fragment() {

    private lateinit var registerViewModel: RegisterViewModel
    private var _binding: FragmentRegisterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root

    }

    public override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            Log.d("LoginFragment", "Utilisateur déjà connecté: ${auth.currentUser?.email}")
            findNavController().navigate(R.id.action_nav_register_to_nav_home)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton = binding.login

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val confirmPasswordEditText = binding.confirmPassword
        val registerButton = binding.createAccount
        val loadingProgressBar = binding.loading

        registerButton.setOnClickListener {
            val email = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotBlank() && password.isNotBlank() && password == confirmPasswordEditText.text.toString()) {
                loadingProgressBar.visibility = View.VISIBLE

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        loadingProgressBar.visibility = View.GONE
                        if (task.isSuccessful) {
                            Log.d("LoginFragment", "createUserWithEmail:success")
                            Toast.makeText(requireContext(), "Compte créé avec succès.", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_nav_register_to_nav_home)
                        } else {
                            Log.w("LoginFragment", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(requireContext(), "Échec de l'inscription: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Veuillez remplir tous les champs pour vous inscrire.", Toast.LENGTH_SHORT).show()
            }
        }

        loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_register_to_nav_login)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}