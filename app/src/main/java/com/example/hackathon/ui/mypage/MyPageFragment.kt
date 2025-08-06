package com.example.hackathon.ui.mypage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hackathon.R
import com.example.hackathon.databinding.FragmentMypageBinding

class MyPageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private lateinit var sharedPreferences: SharedPreferences

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val myPageViewModel =
            ViewModelProvider(this).get(MyPageViewModel::class.java)

        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        android.util.Log.d("MyPageFragment", "MyPageFragment 생성됨")

        // SharedPreferences 초기화
        sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        setupViews()
        loadUserInfo()

        return root
    }

    private fun setupViews() {
        // 사용자 정보 표시
        val userNameText: TextView = binding.textMypage
        val editProfileButton: Button = binding.btnEditProfile
        val withdrawButton: Button = binding.btnWithdraw

        // 회원정보 수정 버튼 클릭
        editProfileButton?.setOnClickListener {
            findNavController().navigate(R.id.action_nav_mypage_to_nav_profile_edit)
        }

        // 회원탈퇴 버튼 클릭
        withdrawButton?.setOnClickListener {
            findNavController().navigate(R.id.action_nav_mypage_to_nav_withdraw)
        }
    }

    private fun loadUserInfo() {
        val userName = sharedPreferences.getString("user_name", "사용자")
        val userEmail = sharedPreferences.getString("user_email", "user@example.com")
        
        binding.textMypage.text = "안녕하세요, ${userName}님!"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}