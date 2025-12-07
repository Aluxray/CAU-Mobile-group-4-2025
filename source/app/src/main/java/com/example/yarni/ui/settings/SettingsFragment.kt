package com.example.yarni.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.navigation.fragment.findNavController

import androidx.fragment.app.Fragment
import com.example.yarni.R

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<TextView>(R.id.textMyAccount).setOnClickListener {
            findNavController().navigate(R.id.action_nav_settings_to_nav_account)
        }


        view.findViewById<TextView>(R.id.textAddAccount).setOnClickListener {
            // TODO: add account
        }

        view.findViewById<TextView>(R.id.textChangePassword).setOnClickListener {
            // TODO: change password
        }

        view.findViewById<TextView>(R.id.textChangeLanguage).setOnClickListener {
            // TODO: change language
        }

        view.findViewById<TextView>(R.id.textUpgradePlan).setOnClickListener {
            // TODO: upgrade plan
        }

        view.findViewById<TextView>(R.id.textMultipleAccount).setOnClickListener {
            // TODO: multiple account
        }

        // ---- SWITCHES ----
        view.findViewById<SwitchCompat>(R.id.switchEnableSync).setOnCheckedChangeListener { _, _ ->
            // TODO: enable/disable sync
        }

        view.findViewById<SwitchCompat>(R.id.switchEnable2Step).setOnCheckedChangeListener { _, _ ->
            // TODO: enable/disable 2-step verification
        }
    }
}
