package com.example.focusmate.session

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.focusmate.R
import com.example.focusmate.databinding.FragmentSessionHistoryBinding

class SessionHistoryFragment : Fragment() {

    private val sessionHistoryViewModel: SessionHistoryViewModel by viewModels {
        SessionHistoryViewModelFactory(requireActivity().application)
    }

    private var _binding: FragmentSessionHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferencesMusic: SharedPreferences
    private var isMusicOn: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSessionHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val adapter = SessionAdapter { session ->
            sessionHistoryViewModel.delete(session)
            Toast.makeText(requireContext(), "Session deleted", Toast.LENGTH_SHORT).show()
        }
        sharedPreferencesMusic = requireActivity().getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
        isMusicOn = sharedPreferencesMusic.getBoolean("isMusicOn", false)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        sessionHistoryViewModel.allSessions.observe(viewLifecycleOwner, Observer { sessions ->
            sessions?.let { adapter.setSessions(it) }
        })

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        sharedPreferencesMusic.edit().putBoolean("isMusicOn", isMusicOn).apply()
        Log.i("SessionHistoryFragment", "isMusicOn: $isMusicOn")
        _binding = null
    }
    override fun onResume() {
        super.onResume()
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.history)
    }
}
