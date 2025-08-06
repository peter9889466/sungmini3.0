package com.example.hackathon.data

import android.content.Context
import android.content.SharedPreferences
import com.example.hackathon.ui.search.Study
import com.example.hackathon.utils.DateUtils

class StudyManager(private val context: Context) {
    
    private val studyPrefs: SharedPreferences = 
        context.getSharedPreferences("study_prefs", Context.MODE_PRIVATE)
    
    private val membershipPrefs: SharedPreferences = 
        context.getSharedPreferences("study_membership", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_STUDY_LIST = "study_list"
    }
    
    /**
     * 스터디에 가입
     */
    fun joinStudy(studyId: String, userId: String): JoinResult {
        // 이미 가입했는지 확인
        if (isUserJoined(studyId, userId)) {
            return JoinResult.ALREADY_JOINED
        }
        
        // 스터디가 존재하는지 확인
        if (!isStudyExists(studyId)) {
            return JoinResult.STUDY_NOT_FOUND
        }
        
        // 스터디장인지 확인 (스터디장은 자동으로 가입되어 있음)
        val studyCreator = studyPrefs.getString("study_${studyId}_creator", "")
        val userManager = UserManager(context)
        val currentUser = userManager.getCurrentUser()
        
        if (currentUser != null && studyCreator == currentUser.nickname) {
            return JoinResult.ALREADY_JOINED // 스터디장은 이미 가입된 상태
        }
        
        // 멤버십 정보 저장
        val memberKey = "membership_${studyId}_${userId}"
        val joinTime = System.currentTimeMillis()
        
        with(membershipPrefs.edit()) {
            putBoolean(memberKey, true)
            putLong("${memberKey}_joined_at", joinTime)
            apply()
        }
        
        return JoinResult.SUCCESS
    }
    
    /**
     * 스터디에서 탈퇴
     */
    fun leaveStudy(studyId: String, userId: String): LeaveResult {
        // 가입했는지 확인
        if (!isUserJoined(studyId, userId)) {
            return LeaveResult.NOT_JOINED
        }
        
        // 스터디장인지 확인 (스터디장은 탈퇴 불가)
        val studyCreator = studyPrefs.getString("study_${studyId}_creator", "")
        val userManager = UserManager(context)
        val currentUser = userManager.getCurrentUser()
        
        if (currentUser != null && studyCreator == currentUser.nickname) {
            return LeaveResult.CREATOR_CANNOT_LEAVE
        }
        
        // 멤버십 정보 삭제
        val memberKey = "membership_${studyId}_${userId}"
        
        with(membershipPrefs.edit()) {
            remove(memberKey)
            remove("${memberKey}_joined_at")
            apply()
        }
        
        return LeaveResult.SUCCESS
    }
    
    /**
     * 스터디 삭제 (스터디장만 가능)
     */
    fun deleteStudy(studyId: String, userId: String): DeleteResult {
        // 스터디가 존재하는지 확인
        if (!isStudyExists(studyId)) {
            return DeleteResult.STUDY_NOT_FOUND
        }
        
        // 스터디장인지 확인
        val studyCreator = studyPrefs.getString("study_${studyId}_creator", "")
        val userManager = UserManager(context)
        val currentUser = userManager.getCurrentUser()
        
        if (currentUser == null || studyCreator != currentUser.nickname) {
            return DeleteResult.NOT_CREATOR
        }
        
        // 스터디 정보 삭제
        val studyList = getStudyList().toMutableSet()
        studyList.remove(studyId)
        
        with(studyPrefs.edit()) {
            putStringSet(KEY_STUDY_LIST, studyList)
            remove("study_${studyId}_name")
            remove("study_${studyId}_description")
            remove("study_${studyId}_category")
            remove("study_${studyId}_creator")
            remove("study_${studyId}_start_date")
            remove("study_${studyId}_end_date")
            remove("study_${studyId}_created_time")
            
            // 댓글도 삭제
            val commentCount = studyPrefs.getInt("study_${studyId}_comment_count", 0)
            for (i in 0 until commentCount) {
                remove("study_${studyId}_comment_${i}_author")
                remove("study_${studyId}_comment_${i}_content")
                remove("study_${studyId}_comment_${i}_timestamp")
            }
            remove("study_${studyId}_comment_count")
            
            apply()
        }
        
        // 모든 멤버십 정보 삭제
        deleteAllMemberships(studyId)
        
        return DeleteResult.SUCCESS
    }
    
    /**
     * 사용자가 가입한 스터디 목록 가져오기
     */
    fun getUserJoinedStudies(userId: String): List<Study> {
        val userManager = UserManager(context)
        val currentUser = userManager.getCurrentUser() ?: return emptyList()
        
        val joinedStudies = mutableListOf<Study>()
        val allStudies = getAllStudies()
        
        for (study in allStudies) {
            // 스터디장이거나 가입한 스터디인 경우
            if (study.creator == currentUser.nickname || isUserJoined(study.id, userId)) {
                joinedStudies.add(study)
            }
        }
        
        return joinedStudies
    }
    
    /**
     * 사용자가 만든 스터디 목록 가져오기
     */
    fun getUserCreatedStudies(userId: String): List<Study> {
        val userManager = UserManager(context)
        val currentUser = userManager.getCurrentUser() ?: return emptyList()
        
        val createdStudies = mutableListOf<Study>()
        val allStudies = getAllStudies()
        
        for (study in allStudies) {
            if (study.creator == currentUser.nickname) {
                createdStudies.add(study)
            }
        }
        
        return createdStudies
    }
    
    /**
     * 사용자가 스터디에 가입했는지 확인
     */
    fun isUserJoined(studyId: String, userId: String): Boolean {
        val memberKey = "membership_${studyId}_${userId}"
        return membershipPrefs.getBoolean(memberKey, false)
    }
    
    /**
     * 사용자가 스터디장인지 확인
     */
    fun isUserCreator(studyId: String, userId: String): Boolean {
        val studyCreator = studyPrefs.getString("study_${studyId}_creator", "")
        val userManager = UserManager(context)
        val currentUser = userManager.getCurrentUser()
        
        return currentUser != null && studyCreator == currentUser.nickname
    }
    
    // Private helper methods
    private fun isStudyExists(studyId: String): Boolean {
        val studyList = getStudyList()
        return studyList.contains(studyId)
    }
    
    private fun getStudyList(): Set<String> {
        return studyPrefs.getStringSet(KEY_STUDY_LIST, emptySet()) ?: emptySet()
    }
    
    private fun getAllStudies(): List<Study> {
        val studyIds = getStudyList()
        val studies = mutableListOf<Study>()
        
        for (id in studyIds) {
            val name = studyPrefs.getString("study_${id}_name", "") ?: ""
            val description = studyPrefs.getString("study_${id}_description", "") ?: ""
            val category = studyPrefs.getString("study_${id}_category", "") ?: ""
            val creator = studyPrefs.getString("study_${id}_creator", "") ?: ""
            val startDate = studyPrefs.getString("study_${id}_start_date", "") ?: ""
            val endDate = studyPrefs.getString("study_${id}_end_date", "") ?: ""
            
            if (name.isNotEmpty()) {
                studies.add(Study(id, name, description, category, creator, startDate, endDate))
            }
        }
        
        return studies
    }
    
    private fun deleteAllMemberships(studyId: String) {
        val editor = membershipPrefs.edit()
        val allKeys = membershipPrefs.all.keys
        
        for (key in allKeys) {
            if (key.startsWith("membership_${studyId}_")) {
                editor.remove(key)
            }
        }
        
        editor.apply()
    }
    
    /**
     * 스터디 멤버 수 가져오기
     */
    fun getStudyMemberCount(studyId: String): Int {
        var count = 1 // 스터디장 포함
        val allKeys = membershipPrefs.all.keys
        
        for (key in allKeys) {
            if (key.startsWith("membership_${studyId}_") && !key.contains("_joined_at")) {
                if (membershipPrefs.getBoolean(key, false)) {
                    count++
                }
            }
        }
        
        return count
    }
    
    /**
     * 스터디 기간을 포맷된 문자열로 가져오기
     */
    fun getFormattedStudyPeriod(studyId: String): String {
        val startDate = studyPrefs.getString("study_${studyId}_start_date", "") ?: ""
        val endDate = studyPrefs.getString("study_${studyId}_end_date", "") ?: ""
        return DateUtils.formatStudyPeriodSimple(startDate, endDate)
    }
    
    /**
     * 스터디 시작일 가져오기
     */
    fun getStudyStartDate(studyId: String): String {
        return studyPrefs.getString("study_${studyId}_start_date", "") ?: ""
    }
    
    /**
     * 스터디 종료일 가져오기
     */
    fun getStudyEndDate(studyId: String): String {
        return studyPrefs.getString("study_${studyId}_end_date", "") ?: ""
    }
}

// Result classes
enum class JoinResult {
    SUCCESS,
    ALREADY_JOINED,
    STUDY_NOT_FOUND
}

enum class LeaveResult {
    SUCCESS,
    NOT_JOINED,
    CREATOR_CANNOT_LEAVE
}

enum class DeleteResult {
    SUCCESS,
    STUDY_NOT_FOUND,
    NOT_CREATOR
}