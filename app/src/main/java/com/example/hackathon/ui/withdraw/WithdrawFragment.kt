package com.example.hackathon.ui.withdraw

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
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
import com.example.hackathon.LoginActivity
import com.example.hackathon.R
import com.example.hackathon.data.StudyManager
import com.example.hackathon.data.UserManager
import com.example.hackathon.databinding.FragmentWithdrawBinding

class WithdrawFragment : Fragment() {

    private var _binding: FragmentWithdrawBinding? = null
    private lateinit var userManager: UserManager
    private lateinit var studyManager: StudyManager

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

        // Manager 초기화
        userManager = UserManager(requireContext())
        studyManager = StudyManager(requireContext())

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
        val currentUser = userManager.getCurrentUser()
        val userInfo = if (currentUser != null) {
            "사용자: ${currentUser.nickname} (${currentUser.userId})"
        } else {
            "현재 사용자"
        }
        
        AlertDialog.Builder(requireContext())
            .setTitle("회원탈퇴 확인")
            .setMessage("정말로 탈퇴하시겠습니까?\n\n$userInfo\n\n탈퇴 후에는 다음 데이터가 모두 삭제됩니다:\n• 개인정보\n• 가입한 스터디 정보\n• 작성한 댓글\n• 생성한 스터디\n\n이 작업은 되돌릴 수 없습니다.")
            .setPositiveButton("탈퇴") { _, _ ->
                performWithdraw()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun performWithdraw() {
        val currentUser = userManager.getCurrentUser()
        if (currentUser == null) {
            Toast.makeText(context, "로그인 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // 1. 사용자가 생성한 스터디들 삭제
            val createdStudies = studyManager.getUserCreatedStudies(currentUser.userId)
            for (study in createdStudies) {
                studyManager.deleteStudy(study.id, currentUser.userId)
            }

            // 2. 가입한 스터디에서 탈퇴
            val joinedStudies = studyManager.getUserJoinedStudies(currentUser.userId)
            for (study in joinedStudies) {
                if (study.creator != currentUser.nickname) { // 본인이 만든 스터디가 아닌 경우만
                    studyManager.leaveStudy(study.id, currentUser.userId)
                }
            }

            // 3. 사용자 계정 삭제
            val deleteSuccess = userManager.deleteUser(currentUser.userId)
            
            if (deleteSuccess) {
                Toast.makeText(context, "회원탈퇴가 완료되었습니다.", Toast.LENGTH_LONG).show()
                
                // 4. 로그인 화면으로 이동
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            } else {
                Toast.makeText(context, "회원탈퇴 처리 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
            
        } catch (e: Exception) {
            Toast.makeText(context, "회원탈퇴 처리 중 오류가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}