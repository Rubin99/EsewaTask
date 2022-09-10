package com.esewa.esewatask.shared

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.ArrayList

/**
 * Created by Rubin on 9/10/2022
 */
class ViewPagerAdapter(private var context: Context?, manager: FragmentManager?) :
    FragmentPagerAdapter(manager!!) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()


    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFrag(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitleList[position]
//        val drawable = ContextCompat.getDrawable(
//           context!!, imageList[position]
//        )
//        drawable?.setBounds(
//            0, 0, drawable.intrinsicWidth,
//            drawable.intrinsicHeight
//        )
//        val spannableString = SpannableString("" + mFragmentTitleList.get(position))
//        val imageSpan = ImageSpan(drawable!!, ImageSpan.ALIGN_BOTTOM)
//
//        spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        return spannableString
    }
}