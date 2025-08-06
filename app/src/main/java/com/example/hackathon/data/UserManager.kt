package com.example.hackathon.data

import android.content.Context
import android.content.SharedPreferences
import com.example.hackathon.data.model.User

class UserManager(context: Context) {
    
    private val userPrefs: SharedPreferences = 
        context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    
    private val sessionPrefs: SharedPreferences = 
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_USER_LIST = "user_list"
        private const val KEY_CURRENT_USER_ID = "current_user_id"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
    
    /**
     * 회원가입
     */
    fun registerUser(userId: String, password: String, name: String, nickname: String, phone: String, email: String? = null): RegisterResult {
        // 중복 체크
        if (isUserIdExists(userId)) {
            return RegisterResult.DUPLICATE_USER_ID
        }
        
        if (isNicknameExists(nickname)) {
            return RegisterResult.DUPLICATE_NICKNAME
        }
        
        if (isPhoneExists(phone)) {
            return RegisterResult.DUPLICATE_PHONE
        }
        
        // 사용자 정보 저장
        val userKey = "user_$userId"
        val currentTime = System.currentTimeMillis().toString()
        
        with(userPrefs.edit()) {
            // 사용자 목록에 추가
            val userList = getUserList().toMutableSet()
            userList.add(userId)
            putStringSet(KEY_USER_LIST, userList)
            
            // 사용자 정보 저장
            putString("${userKey}_password", password)
            putString("${userKey}_name", name)
            putString("${userKey}_nickname", nickname)
            putString("${userKey}_phone", phone)
            putString("${userKey}_email", email ?: "")
            putString("${userKey}_created_at", currentTime)
            putString("${userKey}_updated_at", currentTime)
            apply()
        }
        
        return RegisterResult.SUCCESS
    }
    
    /**
     * 로그인
     */
    fun loginUser(userId: String, password: String): LoginResult {
        if (!isUserIdExists(userId)) {
            return LoginResult.USER_NOT_FOUND
        }
        
        val storedPassword = userPrefs.getString("user_${userId}_password", "")
        if (storedPassword != password) {
            return LoginResult.WRONG_PASSWORD
        }
        
        // 로그인 세션 저장
        with(sessionPrefs.edit()) {
            putString(KEY_CURRENT_USER_ID, userId)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
        
        return LoginResult.SUCCESS
    }
    
    /**
     * 로그아웃
     */
    fun logoutUser() {
        with(sessionPrefs.edit()) {
            remove(KEY_CURRENT_USER_ID)
            putBoolean(KEY_IS_LOGGED_IN, false)
            apply()
        }
    }
    
    /**
     * 현재 로그인 상태 확인
     */
    fun isLoggedIn(): Boolean {
        return sessionPrefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    /**
     * 현재 로그인된 사용자 정보 가져오기
     */
    fun getCurrentUser(): User? {
        if (!isLoggedIn()) return null
        
        val currentUserId = sessionPrefs.getString(KEY_CURRENT_USER_ID, "") ?: return null
        if (currentUserId.isEmpty()) return null
        
        return getUserById(currentUserId)
    }
    
    /**
     * 사용자 ID로 사용자 정보 가져오기
     */
    fun getUserById(userId: String): User? {
        if (!isUserIdExists(userId)) return null
        
        val userKey = "user_$userId"
        val password = userPrefs.getString("${userKey}_password", "") ?: ""
        val name = userPrefs.getString("${userKey}_name", "") ?: ""
        val nickname = userPrefs.getString("${userKey}_nickname", "") ?: ""
        val phone = userPrefs.getString("${userKey}_phone", "") ?: ""
        val email = userPrefs.getString("${userKey}_email", "") ?: ""
        val createdAt = userPrefs.getString("${userKey}_created_at", "") ?: ""
        val updatedAt = userPrefs.getString("${userKey}_updated_at", "") ?: ""
        
        return User(
            id = userId.hashCode().toLong(),
            userId = userId,
            password = password,
            name = name,
            nickname = nickname,
            phone = phone,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
    
    /**
     * 사용자 정보 업데이트
     */
    fun updateUser(userId: String, name: String, nickname: String, phone: String, email: String? = null): UpdateResult {
        if (!isUserIdExists(userId)) {
            return UpdateResult.USER_NOT_FOUND
        }
        
        // 현재 사용자가 아닌 다른 사용자의 닉네임/전화번호와 중복 체크
        val currentUser = getUserById(userId)
        if (currentUser != null) {
            if (nickname != currentUser.nickname && isNicknameExists(nickname)) {
                return UpdateResult.DUPLICATE_NICKNAME
            }
            if (phone != currentUser.phone && isPhoneExists(phone)) {
                return UpdateResult.DUPLICATE_PHONE
            }
        }
        
        val userKey = "user_$userId"
        val currentTime = System.currentTimeMillis().toString()
        
        with(userPrefs.edit()) {
            putString("${userKey}_name", name)
            putString("${userKey}_nickname", nickname)
            putString("${userKey}_phone", phone)
            putString("${userKey}_email", email ?: "")
            putString("${userKey}_updated_at", currentTime)
            apply()
        }
        
        return UpdateResult.SUCCESS
    }
    
    /**
     * 비밀번호 변경
     */
    fun changePassword(userId: String, oldPassword: String, newPassword: String): PasswordChangeResult {
        if (!isUserIdExists(userId)) {
            return PasswordChangeResult.USER_NOT_FOUND
        }
        
        val storedPassword = userPrefs.getString("user_${userId}_password", "")
        if (storedPassword != oldPassword) {
            return PasswordChangeResult.WRONG_OLD_PASSWORD
        }
        
        val userKey = "user_$userId"
        val currentTime = System.currentTimeMillis().toString()
        
        with(userPrefs.edit()) {
            putString("${userKey}_password", newPassword)
            putString("${userKey}_updated_at", currentTime)
            apply()
        }
        
        return PasswordChangeResult.SUCCESS
    }
    
    // Private helper methods
    private fun isUserIdExists(userId: String): Boolean {
        val userList = getUserList()
        return userList.contains(userId)
    }
    
    private fun isNicknameExists(nickname: String): Boolean {
        val userList = getUserList()
        for (userId in userList) {
            val storedNickname = userPrefs.getString("user_${userId}_nickname", "")
            if (storedNickname == nickname) {
                return true
            }
        }
        return false
    }
    
    private fun isPhoneExists(phone: String): Boolean {
        val userList = getUserList()
        for (userId in userList) {
            val storedPhone = userPrefs.getString("user_${userId}_phone", "")
            if (storedPhone == phone) {
                return true
            }
        }
        return false
    }
    
    private fun getUserList(): Set<String> {
        return userPrefs.getStringSet(KEY_USER_LIST, emptySet()) ?: emptySet()
    }
    
    /**
     * 전체 사용자 목록 가져오기 (관리자용)
     */
    fun getAllUsers(): List<User> {
        val userList = getUserList()
        val users = mutableListOf<User>()
        
        for (userId in userList) {
            getUserById(userId)?.let { user ->
                users.add(user)
            }
        }
        
        return users
    }
    
    /**
     * 사용자 삭제 (회원탈퇴)
     */
    fun deleteUser(userId: String): Boolean {
        if (!isUserIdExists(userId)) return false
        
        val userKey = "user_$userId"
        val userList = getUserList().toMutableSet()
        userList.remove(userId)
        
        with(userPrefs.edit()) {
            putStringSet(KEY_USER_LIST, userList)
            remove("${userKey}_password")
            remove("${userKey}_name")
            remove("${userKey}_nickname")
            remove("${userKey}_phone")
            remove("${userKey}_email")
            remove("${userKey}_created_at")
            remove("${userKey}_updated_at")
            apply()
        }
        
        // 현재 로그인된 사용자라면 로그아웃
        val currentUserId = sessionPrefs.getString(KEY_CURRENT_USER_ID, "")
        if (currentUserId == userId) {
            logoutUser()
        }
        
        return true
    }
}

// Result classes
enum class RegisterResult {
    SUCCESS,
    DUPLICATE_USER_ID,
    DUPLICATE_NICKNAME,
    DUPLICATE_PHONE
}

enum class LoginResult {
    SUCCESS,
    USER_NOT_FOUND,
    WRONG_PASSWORD
}

enum class UpdateResult {
    SUCCESS,
    USER_NOT_FOUND,
    DUPLICATE_NICKNAME,
    DUPLICATE_PHONE
}

enum class PasswordChangeResult {
    SUCCESS,
    USER_NOT_FOUND,
    WRONG_OLD_PASSWORD
}