package com.example.hackathon.ui.studydetail

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hackathon.data.DeleteResult
import com.example.hackathon.data.JoinResult
import com.example.hackathon.data.LeaveResult
import com.example.hackathon.data.StudyManager
import com.example.hackathon.data.UserManager
import com.example.hackathon.databinding.FragmentStudyDetailBinding
import com.example.hackathon.utils.DateUtils

class StudyDetailFragment : Fragment() {

    private var _binding: FragmentStudyDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var userManager: UserManager
    private lateinit var studyManager: StudyManager
    private val comments = mutableListOf<Comment>()
    private var studyId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudyDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferences = requireContext().getSharedPreferences("study_prefs", Context.MODE_PRIVATE)
        userManager = UserManager(requireContext())
        studyManager = StudyManager(requireContext())
        
        // Arguments에서 studyId 가져오기
        studyId = arguments?.getString("studyId")
        
        setupRecyclerView()
        loadStudyDetails()
        setupJoinButton()
        setupDeleteButton()
        setupClickListeners()

        return root
    }

    private fun setupRecyclerView() {
        commentAdapter = CommentAdapter(comments)
        binding.recyclerViewComments.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commentAdapter
            isNestedScrollingEnabled = false
            setHasFixedSize(false)
        }
    }
    
    // RecyclerView를 새로고침하고 높이를 조정하는 메서드
    private fun refreshRecyclerView() {
        binding.recyclerViewComments.post {
            commentAdapter.notifyDataSetChanged()
            
            // RecyclerView의 높이를 내용에 맞게 조정
            val layoutManager = binding.recyclerViewComments.layoutManager as LinearLayoutManager
            binding.recyclerViewComments.viewTreeObserver.addOnGlobalLayoutListener(object : android.view.ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.recyclerViewComments.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    
                    var totalHeight = 0
                    for (i in 0 until commentAdapter.itemCount) {
                        val view = layoutManager.findViewByPosition(i)
                        if (view != null) {
                            totalHeight += view.height
                        } else {
                            // 뷰가 아직 생성되지 않은 경우 예상 높이 사용
                            val density = resources.displayMetrics.density
                            totalHeight += (80 * density).toInt() // 80dp를 px로 변환
                        }
                    }
                    
                    // RecyclerView의 높이를 설정
                    val layoutParams = binding.recyclerViewComments.layoutParams
                    layoutParams.height = if (totalHeight > 0) totalHeight else ViewGroup.LayoutParams.WRAP_CONTENT
                    binding.recyclerViewComments.layoutParams = layoutParams
                }
            })
        }
    }

    private fun loadStudyDetails() {
        studyId?.let { id ->
            val studyName = sharedPreferences.getString("study_${id}_name", "")
            val studyDescription = sharedPreferences.getString("study_${id}_description", "")
            val studyCreator = sharedPreferences.getString("study_${id}_creator", "")
            val studyCategory = sharedPreferences.getString("study_${id}_category", "")
            val startDate = sharedPreferences.getString("study_${id}_start_date", "") ?: ""
            val endDate = sharedPreferences.getString("study_${id}_end_date", "") ?: ""

            binding.textStudyTitle.text = studyName
            binding.textStudyCreator.text = "스터디장: $studyCreator"
            binding.textStudyDescription.text = studyDescription
            binding.textStudyCategory.text = "카테고리: $studyCategory"
            
            // 스터디 기간 표시
            binding.textStudyPeriod.text = DateUtils.formatStudyPeriodSimple(startDate, endDate)
            
            // 멤버 수 표시
            val memberCount = studyManager.getStudyMemberCount(id)
            binding.textMemberCount.text = "멤버: ${memberCount}명"
            
            loadComments(id)
        }
    }

    private fun loadComments(studyId: String) {
        // 댓글 로드 (임시로 SharedPreferences 사용)
        val commentCount = sharedPreferences.getInt("study_${studyId}_comment_count", 0)
        comments.clear()
        
        for (i in 0 until commentCount) {
            val author = sharedPreferences.getString("study_${studyId}_comment_${i}_author", "") ?: ""
            val content = sharedPreferences.getString("study_${studyId}_comment_${i}_content", "") ?: ""
            val timestamp = sharedPreferences.getLong("study_${studyId}_comment_${i}_timestamp", 0)
            
            comments.add(Comment(author, content, timestamp))
        }
        
        commentAdapter.updateComments()
        binding.textCommentCount.text = "댓글 ${comments.size}개"
        
        // RecyclerView 새로고침
        refreshRecyclerView()
    }

    private fun setupJoinButton() {
        val currentUser = userManager.getCurrentUser()
        if (currentUser == null || studyId == null) return
        
        val isJoined = studyManager.isUserJoined(studyId!!, currentUser.userId)
        val isCreator = studyManager.isUserCreator(studyId!!, currentUser.userId)
        
        when {
            isCreator -> {
                // 스터디장인 경우
                binding.btnJoinStudy.text = "스터디장"
                binding.btnJoinStudy.isEnabled = false
                binding.btnJoinStudy.backgroundTintList = 
                    requireContext().getColorStateList(com.example.hackathon.R.color.text_secondary)
            }
            isJoined -> {
                // 이미 가입한 경우
                binding.btnJoinStudy.text = "탈퇴하기"
                binding.btnJoinStudy.backgroundTintList = 
                    requireContext().getColorStateList(com.example.hackathon.R.color.error_red)
            }
            else -> {
                // 가입하지 않은 경우
                binding.btnJoinStudy.text = "가입하기"
                binding.btnJoinStudy.backgroundTintList = 
                    requireContext().getColorStateList(com.example.hackathon.R.color.point_lavender)
            }
        }
    }
    
    private fun setupDeleteButton() {
        val currentUser = userManager.getCurrentUser()
        if (currentUser == null || studyId == null) return
        
        val isCreator = studyManager.isUserCreator(studyId!!, currentUser.userId)
        
        // 스터디장만 삭제 버튼 표시
        binding.btnDeleteStudy.visibility = if (isCreator) View.VISIBLE else View.GONE
    }
    
    private fun setupClickListeners() {
        // 뒤로 가기 버튼
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        
        // 가입/탈퇴 버튼
        binding.btnJoinStudy.setOnClickListener {
            handleJoinLeaveAction()
        }
        
        // 스터디 삭제 버튼
        binding.btnDeleteStudy.setOnClickListener {
            handleDeleteStudy()
        }
        
        // 댓글 추가 버튼
        binding.btnAddComment.setOnClickListener {
            val commentText = binding.editComment.text.toString().trim()
            if (commentText.isNotEmpty()) {
                addComment(commentText)
                binding.editComment.text?.clear()
            } else {
                Toast.makeText(context, "댓글을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun handleJoinLeaveAction() {
        val currentUser = userManager.getCurrentUser()
        if (currentUser == null || studyId == null) return
        
        val isJoined = studyManager.isUserJoined(studyId!!, currentUser.userId)
        val isCreator = studyManager.isUserCreator(studyId!!, currentUser.userId)
        
        if (isCreator) {
            // 스터디장은 탈퇴 불가
            return
        }
        
        if (isJoined) {
            // 탈퇴 처리
            when (val result = studyManager.leaveStudy(studyId!!, currentUser.userId)) {
                LeaveResult.SUCCESS -> {
                    Toast.makeText(context, "스터디에서 탈퇴했습니다", Toast.LENGTH_SHORT).show()
                    setupJoinButton()
                    loadStudyDetails() // 멤버 수 업데이트
                }
                LeaveResult.NOT_JOINED -> {
                    Toast.makeText(context, "가입하지 않은 스터디입니다", Toast.LENGTH_SHORT).show()
                }
                LeaveResult.CREATOR_CANNOT_LEAVE -> {
                    Toast.makeText(context, "스터디장은 탈퇴할 수 없습니다", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // 가입 처리
            when (val result = studyManager.joinStudy(studyId!!, currentUser.userId)) {
                JoinResult.SUCCESS -> {
                    Toast.makeText(context, "스터디에 가입했습니다", Toast.LENGTH_SHORT).show()
                    setupJoinButton()
                    loadStudyDetails() // 멤버 수 업데이트
                }
                JoinResult.ALREADY_JOINED -> {
                    Toast.makeText(context, "이미 가입한 스터디입니다", Toast.LENGTH_SHORT).show()
                }
                JoinResult.STUDY_NOT_FOUND -> {
                    Toast.makeText(context, "스터디를 찾을 수 없습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun handleDeleteStudy() {
        val currentUser = userManager.getCurrentUser()
        if (currentUser == null || studyId == null) return
        
        // 확인 다이얼로그 표시 (간단한 Toast로 대체)
        when (val result = studyManager.deleteStudy(studyId!!, currentUser.userId)) {
            DeleteResult.SUCCESS -> {
                Toast.makeText(context, "스터디가 삭제되었습니다", Toast.LENGTH_SHORT).show()
                // 이전 페이지로 돌아가기
                findNavController().navigateUp()
            }
            DeleteResult.NOT_CREATOR -> {
                Toast.makeText(context, "스터디장만 삭제할 수 있습니다", Toast.LENGTH_SHORT).show()
            }
            DeleteResult.STUDY_NOT_FOUND -> {
                Toast.makeText(context, "스터디를 찾을 수 없습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addComment(content: String) {
        studyId?.let { id ->
            val currentUser = userManager.getCurrentUser()
            val author = currentUser?.nickname ?: "익명"
            val timestamp = System.currentTimeMillis()
            
            val commentCount = sharedPreferences.getInt("study_${id}_comment_count", 0)
            
            // 댓글 저장
            with(sharedPreferences.edit()) {
                putString("study_${id}_comment_${commentCount}_author", author)
                putString("study_${id}_comment_${commentCount}_content", content)
                putLong("study_${id}_comment_${commentCount}_timestamp", timestamp)
                putInt("study_${id}_comment_count", commentCount + 1)
                apply()
            }
            
            // UI 업데이트
            comments.add(Comment(author, content, timestamp))
            binding.textCommentCount.text = "댓글 ${comments.size}개"
            
            // RecyclerView 새로고침 - 새 댓글이 추가되면 전체 높이가 자동으로 조정됨
            refreshRecyclerView()
            
            Toast.makeText(context, "댓글이 추가되었습니다", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}