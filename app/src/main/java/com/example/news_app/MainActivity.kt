package com.example.news_app

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.example.news_app.databinding.ActivityMainBinding
import com.example.news_app.model.repository.NewsRepo
import com.example.news_app.network.MyConnectivityManager
import com.example.news_app.network.NewsClient
import com.example.news_app.ui.adapter.PagerAdapter
import com.example.news_app.ui.settings.view.SettingsActivity
import com.example.news_app.ui.viewmodel.NewsViewModel
import com.example.news_app.ui.viewmodel.NewsViewModelFactory
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var pagerAdapter : PagerAdapter
    private lateinit var tabLayout: TabLayout
    companion object{
        var isSearch = false;
    }


    private val pagerCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.contentMain.tabLayout.getTabAt(position)!!.select()
            }
        } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.contentMain.toolBar)

        tabLayout = findViewById(R.id.tabLayout)

        toggle = ActionBarDrawerToggle(this,binding.drawerLayout,binding.contentMain.toolBar,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled
        toggle.drawerArrowDrawable.color = (Color.parseColor("#CA2D21"))
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)

       pagerAdapter = PagerAdapter(this.supportFragmentManager, this.lifecycle)
        binding.contentMain.viewPager.adapter = pagerAdapter
        binding.contentMain.viewPager.registerOnPageChangeCallback(pagerCallback)

        binding.contentMain.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.contentMain.viewPager.currentItem = tab?.position ?: 0
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }


        })

      binding.contentMain.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
      {
          override fun onQueryTextSubmit(query: String?): Boolean {
              return false
          }

          override fun onQueryTextChange(newText: String?): Boolean {
                  if (newText!!.isNotEmpty()){
                      isSearch = true
                      NewsViewModel.textSearch = newText
                      binding.contentMain.viewPager.adapter = pagerAdapter
                      binding.contentMain.viewPager.currentItem = binding.contentMain.tabLayout.selectedTabPosition

                  }
                  else{
                      isSearch = false
                      NewsViewModel.textSearch = ""
                      binding.contentMain.viewPager.adapter = pagerAdapter
                      binding.contentMain.viewPager.currentItem = binding.contentMain.tabLayout.selectedTabPosition
                  }
              return false
          }

      })

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home->{
                binding.contentMain.viewPager.currentItem = 0
            }
            R.id.nav_business ->{
                binding.contentMain.viewPager.currentItem = 1
            }

            R.id.nav_science -> {
                binding.contentMain.viewPager.currentItem = 2
            }
            R.id.nav_sports ->{
                binding.contentMain.viewPager.currentItem = 3
            }
            R.id.nav_entertainment ->{
                binding.contentMain.viewPager.currentItem = 4
            }
            R.id.nav_health ->{
                binding.contentMain.viewPager.currentItem = 5

            }
        }
        if(item.itemId == R.id.nav_settings){
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return  true
    }

}