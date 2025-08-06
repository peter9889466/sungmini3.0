package com.example.hackathon

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.hackathon.data.UserManager
import com.example.hackathon.databinding.ActivityGraddyMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class Graddy_main : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityGraddyMainBinding
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userManager = UserManager(this)
        
        // 로그인 상태 확인
        if (!userManager.isLoggedIn()) {
            // 로그인되지 않은 상태라면 로그인 페이지로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        binding = ActivityGraddyMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarGraddyMain.toolbar)
        
        // 툴바 네비게이션 아이콘 색상을 검정색으로 설정
        binding.appBarGraddyMain.toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.black))

        binding.appBarGraddyMain.toolbar.setOnClickListener { view ->
            Snackbar.make(view, "스터디 모집 플랫폼", Snackbar.LENGTH_LONG)
                .setAction("확인", null).show()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        
        // NavController를 먼저 선언하여 전체 스코프에서 사용 가능하도록 함
        val navController = try {
            findNavController(R.id.nav_host_fragment_content_main)
        } catch (e: Exception) {
            android.util.Log.e("Graddy_main", "NavController 찾기 실패: ${e.message}")
            return
        }

        try {
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_mypage, R.id.nav_profile_edit, R.id.nav_withdraw,
                    R.id.botHome, R.id.botNav, R.id.botSearch
                ), drawerLayout
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        } catch (e: Exception) {
            android.util.Log.e("Graddy_main", "네비게이션 설정 오류: ${e.message}")
        }

        // BottomNavigationView 설정
        val bottomNavView: BottomNavigationView = binding.appBarGraddyMain.bottomNavView

        // 같은 NavController를 BottomNavigationView에도 연결
        try {
            bottomNavView.setupWithNavController(navController)  // ✅ 올바른 변수명 사용
        } catch (e: Exception) {
            android.util.Log.e("Graddy_main", "BottomNavigation 설정 오류: ${e.message}")
        }
        
        // 로그아웃 버튼 설정
        setupLogoutButton()
        
        // 네비게이션 헤더에 닉네임 설정
        setupNavigationHeader()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.graddy_main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    
    private fun setupLogoutButton() {
        val logoutButton = binding.navView.findViewById<android.widget.Button>(R.id.btn_withdraw)
        logoutButton?.setOnClickListener {
            // UserManager를 통한 로그아웃
            userManager.logoutUser()
            
            // 로그인 액티비티로 이동하고 현재 액티비티 종료
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
    
    private fun setupNavigationHeader() {
        val headerView = binding.navView.getHeaderView(0)
        val nicknameTextView = headerView.findViewById<android.widget.TextView>(R.id.nav_header_nickname)
        
        // UserManager를 통해 현재 사용자 정보 가져오기
        val currentUser = userManager.getCurrentUser()
        val nickname = currentUser?.nickname ?: "유저의 닉네임"
        
        nicknameTextView?.text = nickname
    }
}