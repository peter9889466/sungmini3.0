package com.example.hackathon.ui.join

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hackathon.databinding.FragmentJoinBinding
import com.example.hackathon.ui.join.JoinViewModel

class JoinFragment : Fragment() {
    private var _binding: FragmentJoinBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val joinViewModel = ViewModelProvider(this).get(JoinViewModel::class.java)
        _binding = FragmentJoinBinding.inflate(inflater, container, false)

        val root: View = binding.root

        // 버튼 클릭 이벤트 처리
        binding.btnJoin.setOnClickListener {
            val name = binding.joinName.text.toString()
            val email = binding.joinEmail.text.toString()
            val password = binding.joinPw.text.toString()

            if (name.isBlank() || email.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "모든 항목을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO: 여기서 ViewModel 또는 서버로 데이터 전달 등 처리
            Toast.makeText(requireContext(), "회원가입 성공: $name ($email)", Toast.LENGTH_LONG).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}