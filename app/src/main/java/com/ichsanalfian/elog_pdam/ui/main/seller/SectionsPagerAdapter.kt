package com.ichsanalfian.elog_pdam.ui.main.seller

import ProdukFragment
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ichsanalfian.elog_pdam.ui.main.admin.ApprovedFragment
import com.ichsanalfian.elog_pdam.ui.main.admin.WaitingListFragment

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager, tab1 : String, tab2 : String) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val titles = arrayOf(tab1, tab2) // Ganti dengan judul tab sesuai kebutuhan Anda
    private val fragments = arrayOf(ProdukFragment(), RequestFragment()) // Tambahkan fragment sesuai dengan judul tab
    private val adminFragments = arrayOf(WaitingListFragment(), ApprovedFragment())

    override fun getItem(position: Int): Fragment {
        if (titles[0] == "Produk" && titles[1] == "Request"){
            return fragments[position]
        }else{
            return adminFragments[position]
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun getCount(): Int {
        return titles.size
    }
}
