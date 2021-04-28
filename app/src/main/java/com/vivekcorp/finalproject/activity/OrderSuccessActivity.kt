package com.vivekcorp.finalproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.vivekcorp.finalproject.R
import com.vivekcorp.finalproject.fragment.HomeFragment

class OrderSuccessActivity : AppCompatActivity() {

    lateinit var bundle: Bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_success)

        bundle = intent.getBundleExtra("details")!!
    }

    fun successOk(view: View) {
        val intent = Intent(this,DashboardActivity::class.java)
        intent.putExtra("details",bundle)
        startActivity(intent)
        this.finish()
    }
}