package com.example.hackathon.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hackathon.R
import com.example.hackathon.data.DeleteResult
import com.example.hackathon.data.StudyManager
import com.example.hackathon.data.UserManager
import com.example.hackathon.databinding.FragmentMypageBinding
import com.example.hackathon.ui.search.Study

class MyPageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private lateinit var userManager: UserManager
    private lateinit var studyManager: StudyManager
    private lateinit var myStudyAdapter: MyStudyAdapter

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

        userManager = UserManager(requireContext())
        studyManager = StudyManager(requireContext())

        android.util.Log.d("MyPageFragment", "MyPageFragment 생성됨")

        setupViews()
        setupRecyclerView()
        loadUserInfo()
        loadMyStudies()

        return root
    }

    private fun setupViews() {
        // 버튼 관련 설정이 필요한 경우 여기에 추가
    }
    
    private fun setupRecyclerView() {
        myStudyAdapter = MyStudyAdapter(
            onStudyClick = { study ->
                // 스터디 상세 페이지로 이동
                val bundle = bundleOf("studyId" to study.id)
                findNavController().navigate(R.id.action_mypage_to_study_detail, bundle)
            }
        )
        
        binding.recyclerMyStudies.apply {
            layoutManager = GridLayoutManager(context, 2) // 2열 그리드
            adapter = myStudyAdapter
        }
    }

    private fun loadUserInfo() {
        val currentUser = userManager.getCurrentUser()
        if (currentUser != null) {
            binding.textMypage.text = "안녕하세요, ${currentUser.nickname}님!"
        } else {
            binding.textMypage.text = "안녕하세요!"
        }
    }
    
    private fun loadMyStudies() {
        val currentUser = userManager.getCurrentUser()
        if (currentUser != null) {
            val myStudies = studyManager.getUserJoinedStudies(currentUser.userId)
            myStudyAdapter.submitList(myStudies)
            
            binding.textStudyCount.text = "참여 중인 스터디: ${myStudies.size}개"
        }
    }
    


    override fun onResume() {
        super.onResume()
        // 페이지로 돌아올 때마다 스터디 목록 새로고침
        loadMyStudies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}