package com.example.navdemo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.example.navdemo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        binding.btnSubmit.setOnClickListener{
            binding.apply {
                if (etName.text.isEmpty()) {
                    etName.error = "Please enter your name"
                    return@setOnClickListener
                } else {
                    val bundle = bundleOf("user_input" to etName.text.toString())
                    Log.d("Fragment", binding.etName.text.toString())
                    it.findNavController().navigate(R.id.action1, bundle)
                    etName.text.clear()
                }
            }
           }
        return binding.root
    }
}