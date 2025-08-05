# 하단탭 구현 방법

현재 앱은 DrawerLayout(사이드 메뉴) 방식을 사용하고 있습니다.
하단탭을 추가하려면 다음과 같이 구현할 수 있습니다:

## 1. 레이아웃 수정

-   `content_graddy_main.xml`에 `BottomNavigationView` 추가
-   Fragment 영역과 하단탭 영역 분리

## 2. 메뉴 리소스 생성

-   `res/menu/bottom_navigation.xml` 생성
-   하단탭 메뉴 아이템들 정의

## 3. 메인 액티비티 수정

-   `BottomNavigationView` 설정
-   Navigation Controller와 연결

## 4. 아이콘 리소스 추가

-   각 탭별 아이콘 drawable 리소스 필요

하단탭을 추가하시겠습니까?
