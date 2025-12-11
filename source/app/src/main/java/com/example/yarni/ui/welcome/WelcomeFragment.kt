package com.example.yarni.ui.welcome

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.yarni.R
import com.example.yarni.databinding.FragmentWelcomeBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            Log.d("WelcomeFragment", "User already logged in: ${auth.currentUser?.email}")
            findNavController().navigate(R.id.action_nav_welcome_to_nav_home)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createAccountButton = binding.createAccount
        val signInButton = binding.signIn

        credentialManager = CredentialManager.create(requireContext())
        binding.googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }

        createAccountButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_welcome_to_nav_register)
        }

        signInButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_welcome_to_nav_login)
        }
    }

    private fun signInWithGoogle() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.default_web_client_id))
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = requireContext()
                )

                handleGoogleSignIn(result)

            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Google Sign-In error", e)
                Toast.makeText(requireContext(), "Google Sign-In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("GoogleSignIn", "Google Sign-In successful")
                    findNavController().navigate(R.id.action_nav_welcome_to_nav_home)
                } else {
                    Log.e("GoogleSignIn", "Firebase authentication error", task.exception)
                    Toast.makeText(requireContext(), "Firebase authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun handleGoogleSignIn(result: GetCredentialResponse) {
        when (val cred = result.credential) {

            is GoogleIdTokenCredential -> {
                Log.d("GoogleSignIn", "Google ID token received")
                firebaseAuthWithGoogle(cred.idToken)
            }

            is CustomCredential -> {
                if (cred.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleCred = GoogleIdTokenCredential.createFrom(cred.data)
                    firebaseAuthWithGoogle(googleCred.idToken)
                }
            }

            else -> {
                Log.e("GoogleSignIn", "Unsupported credential type: ${cred::class.java.name}")
                Toast.makeText(requireContext(), "Unknown credential type", Toast.LENGTH_SHORT).show()
            }
        }
    }
}