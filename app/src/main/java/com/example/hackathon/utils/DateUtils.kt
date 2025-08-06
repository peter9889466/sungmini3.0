package com.example.hackathon.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    
    private const val DATE_FORMAT = "yyyy.MM.dd"
    private const val DISPLAY_DATE_FORMAT = "yyyy년 MM월 dd일"
    
    /**
     * 날짜 문자열을 표시용 형식으로 변환
     * @param dateString "yyyy.MM.dd" 형식의 날짜 문자열
     * @return "yyyy년 MM월 dd일" 형식의 문자열
     */
    fun formatDateForDisplay(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            val outputFormat = SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault())
            val date = inputFormat.parse(dateString)
            date?.let { outputFormat.format(it) } ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }
    
    /**
     * 스터디 기간을 표시용 문자열로 변환
     * @param startDate 시작일 ("yyyy.MM.dd" 형식)
     * @param endDate 종료일 ("yyyy.MM.dd" 형식)
     * @return "기간: yyyy년 MM월 dd일 ~ yyyy년 MM월 dd일" 형식의 문자열
     */
    fun formatStudyPeriod(startDate: String, endDate: String): String {
        if (startDate.isEmpty() || endDate.isEmpty()) {
            return "기간: 미정"
        }
        
        val formattedStartDate = formatDateForDisplay(startDate)
        val formattedEndDate = formatDateForDisplay(endDate)
        
        return "기간: $formattedStartDate ~ $formattedEndDate"
    }
    
    /**
     * 간단한 스터디 기간 표시 (년도가 같으면 년도 생략)
     * @param startDate 시작일 ("yyyy.MM.dd" 형식)
     * @param endDate 종료일 ("yyyy.MM.dd" 형식)
     * @return 간단한 형식의 기간 문자열
     */
    fun formatStudyPeriodSimple(startDate: String, endDate: String): String {
        if (startDate.isEmpty() || endDate.isEmpty()) {
            return "기간: 미정"
        }
        
        return try {
            val inputFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            val startDateObj = inputFormat.parse(startDate)
            val endDateObj = inputFormat.parse(endDate)
            
            if (startDateObj != null && endDateObj != null) {
                val startCalendar = Calendar.getInstance().apply { time = startDateObj }
                val endCalendar = Calendar.getInstance().apply { time = endDateObj }
                
                val startYear = startCalendar.get(Calendar.YEAR)
                val endYear = endCalendar.get(Calendar.YEAR)
                
                if (startYear == endYear) {
                    // 같은 년도면 시작일에서 년도 생략
                    val startFormat = SimpleDateFormat("MM월 dd일", Locale.getDefault())
                    val endFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
                    
                    val formattedStart = startFormat.format(startDateObj)
                    val formattedEnd = endFormat.format(endDateObj)
                    
                    "기간: $formattedStart ~ $formattedEnd"
                } else {
                    formatStudyPeriod(startDate, endDate)
                }
            } else {
                "기간: $startDate ~ $endDate"
            }
        } catch (e: Exception) {
            "기간: $startDate ~ $endDate"
        }
    }
    
    /**
     * 현재 날짜를 "yyyy.MM.dd" 형식으로 반환
     */
    fun getCurrentDate(): String {
        val format = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return format.format(Date())
    }
    
    /**
     * 날짜 문자열이 유효한지 확인
     * @param dateString "yyyy.MM.dd" 형식의 날짜 문자열
     * @return 유효하면 true, 아니면 false
     */
    fun isValidDate(dateString: String): Boolean {
        return try {
            val format = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            format.isLenient = false
            format.parse(dateString)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * 시작일이 종료일보다 이전인지 확인
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 유효하면 true, 아니면 false
     */
    fun isValidPeriod(startDate: String, endDate: String): Boolean {
        return try {
            val format = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            val start = format.parse(startDate)
            val end = format.parse(endDate)
            
            start != null && end != null && !start.after(end)
        } catch (e: Exception) {
            false
        }
    }
}