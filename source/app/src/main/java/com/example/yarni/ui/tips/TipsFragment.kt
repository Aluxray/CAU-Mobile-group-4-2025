package com.example.yarni.ui.tips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.yarni.R

class TipsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tips = TipsRepository.allTips

        val cardViews = listOf(
            Triple(
                view.findViewById<LinearLayout>(R.id.cardTip1),
                view.findViewById<TextView>(R.id.textTip1),
                tips[0]
            ),
            Triple(
                view.findViewById<LinearLayout>(R.id.cardTip2),
                view.findViewById<TextView>(R.id.textTip2),
                tips[1]
            ),
            Triple(
                view.findViewById<LinearLayout>(R.id.cardTip3),
                view.findViewById<TextView>(R.id.textTip3),
                tips[2]
            ),
            Triple(
                view.findViewById<LinearLayout>(R.id.cardTip4),
                view.findViewById<TextView>(R.id.textTip4),
                tips[3]
            ),
            Triple(
                view.findViewById<LinearLayout>(R.id.cardTip5),
                view.findViewById<TextView>(R.id.textTip5),
                tips[4]
            ),
            Triple(
                view.findViewById<LinearLayout>(R.id.cardTip6),
                view.findViewById<TextView>(R.id.textTip6),
                tips[5]
            ),
            Triple(
                view.findViewById<LinearLayout>(R.id.cardTip7),
                view.findViewById<TextView>(R.id.textTip7),
                tips[6]
            ),
            Triple(
                view.findViewById<LinearLayout>(R.id.cardTip8),
                view.findViewById<TextView>(R.id.textTip8),
                tips[7]
            ),
            Triple(
                view.findViewById<LinearLayout>(R.id.cardTip9),
                view.findViewById<TextView>(R.id.textTip9),
                tips[8]
            ),
            Triple(
                view.findViewById<LinearLayout>(R.id.cardTip10),
                view.findViewById<TextView>(R.id.textTip10),
                tips[9]
            ),
            Triple(
                view.findViewById<LinearLayout>(R.id.cardTip11),
                view.findViewById<TextView>(R.id.textTip11),
                tips[10]
            ),
            Triple(
                view.findViewById<LinearLayout>(R.id.cardTip12),
                view.findViewById<TextView>(R.id.textTip12),
                tips[11]
            ),
            Triple(
                view.findViewById<LinearLayout>(R.id.cardTip13),
                view.findViewById<TextView>(R.id.textTip13),
                tips[12]
            ),
            Triple(
                view.findViewById<LinearLayout>(R.id.cardTip14),
                view.findViewById<TextView>(R.id.textTip14),
                tips[13]
            ),
            Triple(
                view.findViewById<LinearLayout>(R.id.cardTip15),
                view.findViewById<TextView>(R.id.textTip15),
                tips[14]
            )
        )

        cardViews.forEach { (card, titleView, tip) ->
            titleView.text = tip.title

            card.setOnClickListener {
                showTipDialog(tip.title, tip.description)
            }
        }
    }

    private fun showTipDialog(title: String, description: String) {

        val dialogView = layoutInflater.inflate(R.layout.dialog_tip, null)

        val titleView = dialogView.findViewById<TextView>(R.id.dialogTipTitle)
        val descView = dialogView.findViewById<TextView>(R.id.dialogTipDesc)

        titleView.text = title
        descView.text = description

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.show()
    }
}
