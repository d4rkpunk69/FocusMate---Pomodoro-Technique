package com.example.navdemo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.navdemo.databinding.FragmentLastScreenBinding

class LastScreen : Fragment() {
    private lateinit var binding: FragmentLastScreenBinding
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentLastScreenBinding.inflate(
            inflater,
            container,
            false
        )
        binding.apply {
            tvSummary.text = "Welcome ${arguments?.getString("user")}\n Your email is ${arguments?.getString("email")}"
        }
        return binding.root
    }
}