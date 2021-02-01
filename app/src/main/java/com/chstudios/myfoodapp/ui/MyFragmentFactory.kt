package com.chstudios.myfoodapp.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.chstudios.myfoodapp.util.adapters.ListNameAdapter
import javax.inject.Inject

class MyFragmentFactory @Inject constructor(
    private val listAdapter: ListNameAdapter,
): FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className) {
            MainFragment::class.java.name -> MainFragment()
            InventoryFragment::class.java.name -> InventoryFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}