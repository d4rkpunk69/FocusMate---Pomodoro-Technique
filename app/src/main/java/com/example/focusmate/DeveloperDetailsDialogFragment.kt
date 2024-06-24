package com.example.focusmate.ui.aboutus

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.focusmate.R

class DeveloperDetailsDialogFragment : DialogFragment() {

    companion object {
        const val ARG_IMAGE_RES_ID = "image_res_id"
        const val ARG_NAME = "name"
        const val ARG_DETAILS = "details"

        fun newInstance(imageResId: Int, name: String, details: String): DeveloperDetailsDialogFragment {
            val fragment = DeveloperDetailsDialogFragment()
            val args = Bundle()
            args.putInt(ARG_IMAGE_RES_ID, imageResId)
            args.putString(ARG_NAME, name)
            args.putString(ARG_DETAILS, details)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_developer_details, container, false)

        val imageView = view.findViewById<ImageView>(R.id.dev_image)
        val nameTextView = view.findViewById<TextView>(R.id.dev_name)
        val detailsTextView = view.findViewById<TextView>(R.id.dev_details)

        val imageResId = arguments?.getInt(ARG_IMAGE_RES_ID) ?: 0
        val name = arguments?.getString(ARG_NAME) ?: ""
        val details = arguments?.getString(ARG_DETAILS) ?: ""

        imageView.setImageResource(imageResId)
        nameTextView.text = name
        detailsTextView.text = details

        return view
    }
}
