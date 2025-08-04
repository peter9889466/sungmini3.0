package com.example.hackathon.ui.withdraw

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hackathon.R
import com.example.hackathon.databinding.FragmentWithdrawBinding

class WithdrawFragment : Fragment() {

    private var _binding: FragmentWithdrawBinding? = null
    private lateinit var sharedPreferences: SharedPreferences

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val withdrawViewModel =
            ViewModelProvider(this).get(WithdrawViewModel::class.java)

        _binding = FragmentWithdrawBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // SharedPreferences 초기화
        sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        setupViews()

        return root
    }

    private fun setupViews() {
        val reasonEditText: EditText = binding.root.findViewById(R.id.edit_withdraw_reason)
        val agreeCheckBox: CheckBox = binding.root.findViewById(R.id.checkbox_agree)
        val withdrawButton: Button = binding.root.findViewById(R.id.btn_withdraw_confirm)
        val cancelButton: Button = binding.root.findViewById(R.id.btn_cancel_withdraw)

        // 회원탈퇴 확인 버튼 클릭
        withdrawButton?.setOnClickListener {
            val reason = reasonEditText?.text.toString().trim()
            val isAgreed = agreeCheckBox?.isChecked ?: false

            if (validateWithdrawInput(reason, isAgreed)) {
                showWithdrawConfirmDialog()
            }
        }

        // 취소 버튼 클릭
        cancelButton?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun validateWithdrawInput(reason: String, isAgreed: Boolean): Boolean {
        if (reason.isEmpty()) {
            Toast.makeText(context, "탈퇴 사유를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!isAgreed) {
            Toast.makeText(context, "탈퇴 동의에 체크해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun showWithdrawConfirmDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("회원탈퇴 확인")
            .setMessage("정말로 탈퇴하시겠습니까?\n탈퇴 후에는 모든 데이터가 삭제되며 복구할 수 없습니다.")
            .setPositiveButton("탈퇴") { _, _ ->
                performWithdraw()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun performWithdraw() {
        // 사용자 데이터 삭제
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }

        Toast.makeText(context, "회원탈퇴가 완료되었습니다.", Toast.LENGTH_LONG).show()
        
        // 로그인 화면으로 이동 (또는 메인 화면)
        // 실제 앱에서는 로그인 액티비티로 이동해야 함
        findNavController().popBackStack(R.id.nav_mypage, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}