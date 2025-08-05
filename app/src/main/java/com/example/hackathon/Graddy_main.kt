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
import com.example.hackathon.databinding.ActivityGraddyMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class Graddy_main : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityGraddyMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        val navController = findNavController(R.id.nav_host_fragment_content_main)

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

        // BottomNavigationView 설정
        val bottomNavView: BottomNavigationView = binding.appBarGraddyMain.bottomNavView

        // 수정된 부분: 이미 선언된 navController를 재사용
        // 기존의 잘못된 코드: val bottomNavController = findNavController(R.id.fab)
        // 올바른 코드: 같은 NavController 사용
        bottomNavView.setupWithNavController(navController)
        
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
        
        // SharedPreferences에서 닉네임 가져오기
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val nickname = sharedPreferences.getString("user_nickname", "유저의 닉네임")
        
        nicknameTextView?.text = nickname
    }
}