package com.vivekcorp.finalproject.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.vivekcorp.finalproject.R
import com.vivekcorp.finalproject.fragment.*

class DashboardActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView

    var previousMenuItem: MenuItem? = null

    lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)
        setUpToolbar()

        openHome()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@DashboardActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }

            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            bundle = intent.getBundleExtra("details")!!

            when (it.itemId) {

                R.id.home -> {
                    openHome()
                    drawerLayout.closeDrawers()
                }

                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            ProfileFragment(
                                bundle
                            )
                        )
                        .commit()

                    supportActionBar?.title = "My Profile"

                    drawerLayout.closeDrawers()
                }
                R.id.favoriteRestaurants -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FavoriteFragment())
                        .commit()

                    supportActionBar?.title = "Favorite Restaurants"
                    drawerLayout.closeDrawers()
                }
                R.id.history -> {
                    bundle = intent.getBundleExtra("details")!!

                    val orderHistory = OrderHistory()
                    orderHistory.arguments = bundle

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, orderHistory)
                        .commit()

                    supportActionBar?.title = "My Previous Orders"
                    drawerLayout.closeDrawers()
                }
                R.id.FAQs -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FAQsFragment())
                        .commit()

                    supportActionBar?.title = "Frequently Asked Question"
                    drawerLayout.closeDrawers()
                }
                R.id.logout -> {
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Are you sure you want to log out?")
                    dialog.setPositiveButton("Yes") { text, listner ->
                        val intent = Intent(this, LoginActivity::class.java)
                        val preferences = getSharedPreferences(getString(R.string.preferences_file_name), MODE_PRIVATE)
                        val editor = preferences.edit()
                        editor.clear()
                        editor.apply()
                        startActivity(intent)
                        finish()
                    }
                    dialog.setNegativeButton("No") { text, listner ->
                        openHome()
                        drawerLayout.closeDrawers()
                    }
                    dialog.create()
                    dialog.show()
                }
            }

            return@setNavigationItemSelectedListener true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setUpToolbar() {

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun openHome() {

        if (intent != null) {
            bundle = intent.getBundleExtra("details")!!
        }
        val homefrag = HomeFragment()
        homefrag.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, homefrag)
            .commit()

        supportActionBar?.title = "All Restaurants"
        navigationView.setCheckedItem(R.id.home)
    }

    override fun onBackPressed() {

        val frag = supportFragmentManager.findFragmentById(R.id.frame)

        when (frag) {
            !is HomeFragment -> openHome()

            else -> super.onBackPressed()
        }
    }
}