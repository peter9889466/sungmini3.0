package com.example.hackathon.data.api

/**
 * Spring Boot 백엔드 API 엔드포인트 정의
 * 
 * 기본 URL: http://your-server.com/api
 */
object ApiEndpoints {
    
    // 기본 설정
    const val BASE_URL = "http://localhost:8080/api/"
    
    // 사용자 관련 API
    object User {
        const val LOGIN = "auth/login"                    // POST
        const val SIGNUP = "auth/signup"                  // POST
        const val LOGOUT = "auth/logout"                  // POST
        const val PROFILE = "user/profile"                // GET, PUT
        const val UPDATE_PROFILE = "user/profile/update"  // PUT
    }
    
    // 스터디 관련 API
    object Study {
        const val CREATE = "study"                        // POST
        const val LIST = "study"                          // GET
        const val DETAIL = "study/{id}"                   // GET
        const val UPDATE = "study/{id}"                   // PUT
        const val DELETE = "study/{id}"                   // DELETE
        const val SEARCH = "study/search"                 // GET
        const val JOIN = "study/{id}/join"                // POST
        const val LEAVE = "study/{id}/leave"              // DELETE
        const val MEMBERS = "study/{id}/members"          // GET
    }
    
    // 댓글 관련 API
    object Comment {
        const val CREATE = "study/{studyId}/comment"      // POST
        const val LIST = "study/{studyId}/comment"        // GET
        const val UPDATE = "comment/{id}"                 // PUT
        const val DELETE = "comment/{id}"                 // DELETE
    }
    
    // 카테고리 관련 API
    object Category {
        const val LIST = "category"                       // GET
    }
}

/**
 * HTTP 메소드별 사용 예시:
 * 
 * 1. 회원가입: POST /api/auth/signup
 * 2. 로그인: POST /api/auth/login
 * 3. 스터디 생성: POST /api/study
 * 4. 스터디 목록: GET /api/study?page=0&size=20
 * 5. 스터디 검색: GET /api/study/search?keyword=알고리즘&category=프로그래밍
 * 6. 스터디 상세: GET /api/study/123
 * 7. 댓글 작성: POST /api/study/123/comment
 * 8. 댓글 목록: GET /api/study/123/comment
 */