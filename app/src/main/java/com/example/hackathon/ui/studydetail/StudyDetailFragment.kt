package com.example.hackathon.ui.studydetail

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hackathon.databinding.FragmentStudyDetailBinding

class StudyDetailFragment : Fragment() {

    private var _binding: FragmentStudyDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var commentAdapter: CommentAdapter
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
        
        // Arguments에서 studyId 가져오기
        studyId = arguments?.getString("studyId")
        
        setupRecyclerView()
        loadStudyDetails()
        setupClickListeners()

        return root
    }

    private fun setupRecyclerView() {
        commentAdapter = CommentAdapter(comments)
        binding.recyclerViewComments.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commentAdapter
        }
    }

    private fun loadStudyDetails() {
        studyId?.let { id ->
            val studyName = sharedPreferences.getString("study_${id}_name", "")
            val studyDescription = sharedPreferences.getString("study_${id}_description", "")
            val studyCreator = sharedPreferences.getString("study_${id}_creator", "")
            val studyCategory = sharedPreferences.getString("study_${id}_category", "")

            binding.textStudyTitle.text = studyName
            binding.textStudyCreator.text = "스터디장: $studyCreator"
            binding.textStudyDescription.text = studyDescription
            binding.textStudyCategory.text = "카테고리: $studyCategory"
            
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
        
        commentAdapter.notifyDataSetChanged()
        binding.textCommentCount.text = "댓글 ${comments.size}개"
    }

    private fun setupClickListeners() {
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

    private fun addComment(content: String) {
        studyId?.let { id ->
            val userPrefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val author = userPrefs.getString("user_nickname", "익명") ?: "익명"
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
            commentAdapter.notifyItemInserted(comments.size - 1)
            binding.textCommentCount.text = "댓글 ${comments.size}개"
            binding.recyclerViewComments.scrollToPosition(comments.size - 1)
            
            Toast.makeText(context, "댓글이 추가되었습니다", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}