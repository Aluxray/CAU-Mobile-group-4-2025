package com.example.yarni.ui.login.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.yarni.R
import com.example.yarni.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null

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

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    public override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            Log.d("LoginFragment", "Utilisateur déjà connecté: ${auth.currentUser?.email}")
            findNavController().navigate(R.id.action_nav_login_to_nav_home)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val registerButton = binding.createAccount

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.login
        val loadingProgressBar = binding.loading

        loginButton.setOnClickListener {
            val email = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotBlank() && password.isNotBlank()) {
                loadingProgressBar.visibility = View.VISIBLE

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        loadingProgressBar.visibility = View.GONE
                        if (task.isSuccessful) {
                            Log.d("LoginFragment", "signInWithEmail:success")
                            val user = auth.currentUser
                            Toast.makeText(
                                requireContext(),
                                "Connexion réussie: ${user?.email}",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.action_nav_login_to_nav_home)
                        } else {
                            Log.w("LoginFragment", "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                requireContext(),

                                "Échec de l'authentification.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Veuillez entrer un e-mail et un mot de passe.", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_login_to_nav_register)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}