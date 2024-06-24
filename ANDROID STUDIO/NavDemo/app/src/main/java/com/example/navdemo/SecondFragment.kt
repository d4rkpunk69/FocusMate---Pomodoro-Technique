package com.example.navdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.example.navdemo.databinding.FragmentSecondBinding
import java.util.Locale

class SecondFragment : Fragment() {
    private lateinit var binding: FragmentSecondBinding
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSecondBinding.inflate(
            inflater,
            container,
            false
        )
        val name = arguments?.getString("user_input")
        binding.apply {
            btnSubmit2.setOnClickListener {
                if(etEmail.text.toString().isNotEmpty()) {
                    val bundle = bundleOf(
                        "email_add" to etEmail.text.toString(),
                        "user_name" to name.toString())
                    it.findNavController().navigate(R.id.action2, bundle)
                } else {
                    etEmail.error = "Please input your E-mail"
                    return@setOnClickListener
                }
            }
        }
        val input = arguments?.getString("user_input", "VISITOR")
        val terms = arguments?.getString("no", "")
        Log.d("Fragment", "From FirstFragment: $input")
        binding.tvName.text = "Welcome ${input.toString().replaceFirstChar {u -> u.uppercase(Locale.getDefault())}},\nPlease input your E-mail"
        if(terms!="") {
            binding.notAccepted.text = "You haven't accepted the terms!"
        } else {
            binding.notAccepted.text = ""
        }
        return binding.root
    }
}