package com.example.hackathon.utils

import com.example.hackathon.data.StudyManager
import com.example.hackathon.ui.search.Study

object StudyDisplayUtils {
    
    /**
     * 스터디 정보를 한 줄로 요약해서 표시
     */
    fun getStudySummary(study: Study): String {
        val period = DateUtils.formatStudyPeriodSimple(study.startDate, study.endDate)
        return "${study.category} | $period"
    }
    
    /**
     * 스터디 카드에 표시할 상세 정보
     */
    fun getStudyCardInfo(study: Study, studyManager: StudyManager): StudyCardInfo {
        val memberCount = studyManager.getStudyMemberCount(study.id)
        val formattedPeriod = DateUtils.formatStudyPeriodSimple(study.startDate, study.endDate)
        
        return StudyCardInfo(
            title = study.name,
            creator = "스터디장: ${study.creator}",
            category = "카테고리: ${study.category}",
            memberCount = "멤버: ${memberCount}명",
            period = formattedPeriod,
            description = study.description
        )
    }
    
    /**
     * 스터디 상태 확인 (진행중, 예정, 종료)
     */
    fun getStudyStatus(startDate: String, endDate: String): StudyStatus {
        if (startDate.isEmpty() || endDate.isEmpty()) {
            return StudyStatus.UNKNOWN
        }
        
        val currentDate = DateUtils.getCurrentDate()
        
        return when {
            currentDate < startDate -> StudyStatus.UPCOMING
            currentDate > endDate -> StudyStatus.COMPLETED
            else -> StudyStatus.ONGOING
        }
    }
}

data class StudyCardInfo(
    val title: String,
    val creator: String,
    val category: String,
    val memberCount: String,
    val period: String,
    val description: String
)

enum class StudyStatus {
    UPCOMING,   // 예정
    ONGOING,    // 진행중
    COMPLETED,  // 종료
    UNKNOWN     // 미정
}