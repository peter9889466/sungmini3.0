package com.example.hackathon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hackathon.databinding.FragmentHomeBinding

// HomeFragment.kt
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnToMypage.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_mypage)
        }

        binding.calendarView.date = System.currentTimeMillis()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
