package com.esewa.esewatask

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.*
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.esewa.esewatask.freeUsers.FreeUserFragment
import com.esewa.esewatask.premiumUsers.PremiumUserFragment
import com.esewa.esewatask.shared.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {
    private val navLabels = intArrayOf(
        R.string.txvFree,
        R.string.txvPremium,
    )
    lateinit var  viewpager: ViewPager
    lateinit var tabs: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewpager = findViewById(R.id.viewpager)
        tabs = findViewById(R.id.tabs)

        initTabs()
    }

    private fun initTabs() {
        setupViewPager(viewpager)
        tabs?.setupWithViewPager(viewpager)
        tabs?.tabGravity = TabLayout.GRAVITY_FILL
        viewpager?.measure(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        for (i in 0 until tabs?.tabCount!!) {
            val tab = LayoutInflater.from(applicationContext).inflate(R.layout.nav_tab, null) as LinearLayout
            val tabLabel = tab.findViewById<View>(R.id.navLabel) as TextView
            tabLabel.text = resources.getString(navLabels[i])
            tabs?.getTabAt(i)?.customView = tab
        }
    }

    fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(applicationContext, supportFragmentManager)
        adapter.addFrag(FreeUserFragment(), "FreeUsers")
        adapter.addFrag(PremiumUserFragment(), "PremiumUsers")
        viewPager?.adapter = adapter
    }
}