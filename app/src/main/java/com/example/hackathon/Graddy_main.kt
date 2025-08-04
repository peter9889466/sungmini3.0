package com.example.hackathon

import android.os.Bundle
import android.view.Menu
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
                R.id.nav_mypage, R.id.nav_profile_edit, R.id.nav_withdraw
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // View Binding을 사용한다고 가정
        // binding.bottomNavView는 XML에 정의된 BottomNavigationView를 "호출" (참조)하는 것입니다.
        val bottomNavView: BottomNavigationView = binding.bottomNavView

        // NavController 가져오기
        val bottomNavController = findNavController(R.id.fab)

        // "호출"한 bottomNavView 객체를 사용하여 NavController와 연결
        bottomNavView.setupWithNavController(bottomNavController)

        // 이제 bottomNavView는 화면에 보여지고, 탭 클릭 시 navController를 통해 화면 전환을

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
}