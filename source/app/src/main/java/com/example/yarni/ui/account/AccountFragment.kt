package com.example.yarni.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.yarni.R
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailText = view.findViewById<TextView>(R.id.textUserEmail)
        val logoutButton = view.findViewById<Button>(R.id.buttonLogout)

        val user = auth.currentUser
        emailText.text = user?.email ?: "No email found"

        // Logout
        logoutButton.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_nav_account_to_nav_welcome)
            // TODO: navigate to login screen
        }
    }
}
