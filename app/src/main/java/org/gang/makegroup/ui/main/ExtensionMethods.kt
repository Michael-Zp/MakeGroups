package org.gang.makegroup.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun FragmentManager.replaceTransaction(idToReplace: Int, newFragment: Fragment) {
    beginTransaction().replace(idToReplace, newFragment).commit()
}