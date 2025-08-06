package com.example.hackathon.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hackathon.R
import com.example.hackathon.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("HomeFragment", "HomeFragment onCreateView 시작")
        
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Log.d("HomeFragment", "HomeFragment 생성 완료")
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("HomeFragment", "HomeFragment onViewCreated 시작")
        
        try {
            // 캘린더 뷰 설정
            binding.calendarView?.date = System.currentTimeMillis()
            
            // 마이페이지 버튼 설정 (있는 경우에만)
            binding.btnToMypage?.setOnClickListener {
                try {
                    // mobile_navigation2.xml의 nav_mypage로 이동
                    findNavController().navigate(R.id.nav_mypage)
                } catch (e: Exception) {
                    Log.e("HomeFragment", "네비게이션 오류: ${e.message}")
                    // 대안: 직접 MyPageFragment로 이동
                    try {
                        val action = com.example.hackathon.R.id.nav_mypage
                        findNavController().navigate(action)
                    } catch (e2: Exception) {
                        Log.e("HomeFragment", "대안 네비게이션도 실패: ${e2.message}")
                    }
                }
            }
            
            Log.d("HomeFragment", "HomeFragment 설정 완료")
        } catch (e: Exception) {
            Log.e("HomeFragment", "HomeFragment 설정 중 오류: ${e.message}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("HomeFragment", "HomeFragment onDestroyView")
        _binding = null
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d("HomeFragment", "HomeFragment onDestroy")
    }
}