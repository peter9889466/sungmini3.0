package com.example.hackathon.data.api

/**
 * Spring Boot + JPA를 위한 데이터베이스 스키마 정의
 * 
 * 이 파일은 백엔드 개발자가 참고할 수 있도록 
 * 데이터베이스 테이블 구조를 정의합니다.
 */

/**
 * 사용자 테이블 (users)
 */
data class UserTable(
    val id: Long,                    // Primary Key, Auto Increment
    val user_id: String,             // 사용자 ID (Unique, Not Null)
    val password: String,            // 암호화된 비밀번호 (Not Null)
    val name: String,                // 실명 (Not Null)
    val nickname: String,            // 닉네임 (Unique, Not Null)
    val phone: String,               // 전화번호 (Not Null)
    val created_at: String,          // 생성일시 (Timestamp)
    val updated_at: String,           // 수정일시 (Timestamp)
)

/**
 * 스터디 테이블 (studies)
 */
data class StudyTable(
    val id: Long,                    // Primary Key, Auto Increment
    val name: String,                // 스터디명 (Not Null)
    val description: String,         // 스터디 설명 (Text, Not Null)
    val category: String,            // 카테고리 (Not Null)
    val creator_id: Long,            // 스터디장 ID (Foreign Key -> users.id)
    val max_members: Int,            // 최대 인원 (Default: 10)
    val current_members: Int,        // 현재 인원 (Default: 1)
    val status: String,              // 상태 (RECRUITING, FULL, CLOSED)
    val created_at: String,          // 생성일시 (Timestamp)
    val updated_at: String,           // 수정일시 (Timestamp)
)

/**
 * 댓글 테이블 (comments)
 */
data class CommentTable(
    val id: Long,                    // Primary Key, Auto Increment
    val study_id: Long,              // 스터디 ID (Foreign Key -> studies.id)
    val author_id: Long,             // 작성자 ID (Foreign Key -> users.id)
    val content: String,             // 댓글 내용 (Text, Not Null)
    val created_at: String,          // 생성일시 (Timestamp)
    val updated_at: String,          // 수정일시 (Timestamp)
)

/**
 * 스터디 멤버 테이블 (study_members) - 다대다 관계
 */
data class StudyMemberTable(
    val id: Long,                    // Primary Key, Auto Increment
    val study_id: Long,              // 스터디 ID (Foreign Key -> studies.id)
    val user_id: Long,               // 사용자 ID (Foreign Key -> users.id)
    val role: String,                // 역할 (LEADER, MEMBER)
    val joined_at: String,            // 가입일시 (Timestamp)
)

/**
 * 카테고리 테이블 (categories) - 선택사항
 */
data class CategoryTable(
    val id: Long,                    // Primary Key, Auto Increment
    val name: String,                // 카테고리명 (Unique, Not Null)
    val description: String?,        // 카테고리 설명 (Nullable)
    val created_at: String,           // 생성일시 (Timestamp)
)

/**
 * Spring Boot Entity 클래스 예시:
 * 
 * @Entity
 * @Table(name = "users")
 * public class User {
 *     @Id
 *     @GeneratedValue(strategy = GenerationType.IDENTITY)
 *     private Long id;
 *     
 *     @Column(unique = true, nullable = false)
 *     private String userId;
 *     
 *     @Column(nullable = false)
 *     private String password;
 *     
 *     // ... 기타 필드들
 * }
 */