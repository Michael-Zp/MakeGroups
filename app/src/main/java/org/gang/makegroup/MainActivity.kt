package org.gang.makegroup

//Icon made by srip from www.flaticon.com

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import org.gang.makegroup.ui.main.MainViewModel
import org.gang.makegroup.ui.main.ManageGroupFragment
import org.gang.makegroup.ui.main.OverviewFragment
import org.gang.makegroup.ui.main.replaceTransaction

class MainActivity : AppCompatActivity() {

    private lateinit var state: UIState


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)


        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.load(this)

        if (savedInstanceState == null) {
            showOverview()
        }
    }

    fun showManageGroup() {
        switchFragment(ManageGroupFragment.newInstance(this), UIState.ManageGroup, true)
    }

    private fun showOverview() {
        switchFragment(OverviewFragment.newInstance(this), UIState.Overview, false)
    }

    private fun switchFragment(fragment: Fragment, newState: UIState, backPressable: Boolean) {
        supportFragmentManager.replaceTransaction(R.id.container, fragment)
        supportActionBar?.setDisplayHomeAsUpEnabled(backPressable)
        state = newState
    }

    override fun onBackPressed() {
        when (state) {
            UIState.Overview -> finish()
            UIState.ManageGroup -> showOverview()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }

    override fun onStop() {

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.save(this)

        super.onStop()
    }

}
