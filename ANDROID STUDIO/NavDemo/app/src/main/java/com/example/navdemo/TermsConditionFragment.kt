package com.example.navdemo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.example.navdemo.databinding.FragmentTermsConditionBinding

class TermsConditionFragment : Fragment() {
private lateinit var binding: FragmentTermsConditionBinding
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTermsConditionBinding.inflate(inflater, container, false)

        binding.apply {
            btnTC.isEnabled = false
            btnTC.text = "Please agree to the terms and conditions"
            val name = arguments?.getString("user_name")
            val email = arguments?.getString("email_add")

            tvTC.text = "Please agree to the terms and conditions ${name.toString()}!" +
                    "We will send you an email to ${email.toString()} for agreeing this terms and conditions."

            ckbTC.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    btnTC.isEnabled = true
                    btnTC.text = "Next"
                    dontAgree.isEnabled = false
                } else {
                    btnTC.isEnabled = false
                    btnTC.text = "Please agree to the terms and conditions"
                    dontAgree.isEnabled = true
                }
            }
            btnTC.setOnClickListener {
                val bundle = bundleOf(
                    "email" to arguments?.getString("email_add"),
                    "user" to arguments?.getString("user_name")
                )
                it.findNavController().navigate(R.id.actionLast, bundle)
            }
            binding.dontAgree.setOnClickListener {
                val dontAccept = "User doesn't accept"
                val bundle = bundleOf(
                    "no" to dontAccept
                )
                it.findNavController().navigate(R.id.actionDontAccept, bundle)
            }
        }
        return binding.root

    }
}