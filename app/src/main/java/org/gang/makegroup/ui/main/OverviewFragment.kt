package org.gang.makegroup.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import org.gang.makegroup.MainActivity
import org.gang.makegroup.R

class OverviewFragment(private val mainActivity: MainActivity) : Fragment() {

    companion object {
        fun newInstance(mainActivity: MainActivity) = OverviewFragment(mainActivity)
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.overview_fragment, container, false)

        view.findViewById<TextView>(R.id.tv_manage_group).setOnClickListener {
            mainActivity.showManageGroup()
        }

        view.findViewById<TextView>(R.id.tv_first).setOnClickListener {
            val alertDialog: AlertDialog? = this.let {
                val builder = AlertDialog.Builder(context)

                builder.apply {
                    setPositiveButton("Dismiss", null)

                    val randomMember = viewModel.getRandomMemberName()

                    setCancelable(true)
                    if(randomMember != null) {
                        setMessage("$randomMember goes first.")
                    } else {
                        setMessage("No active members in group found.")
                    }
                }

                builder.create()
            }

            alertDialog?.show()
        }

        view.findViewById<TextView>(R.id.tv_tow_teams).setOnClickListener {
            val alertDialog: AlertDialog? = this.let {
                val builder = AlertDialog.Builder(context)

                builder.apply {

                    setPositiveButton("Dismiss", null)

                    val numberOfTeams = 2
                    val teams = viewModel.getRandomTeams(numberOfTeams)

                    setCancelable(true)
                    if(teams != null) {
                        val sb = StringBuilder()
                        teams.forEachIndexed { i, t ->
                            sb.appendln("Team ${i + 1}: ")
                            t.forEachIndexed {k, name ->
                                sb.append(name)
                                if(k + 1 < t.count()) {
                                    sb.append(", ")
                                }
                            }
                            sb.appendln()
                            sb.appendln()
                        }

                        setMessage(sb.toString())
                    } else {
                        setMessage("No or to few active members in group found for $numberOfTeams teams.")
                    }
                }

                builder.create()
            }

            alertDialog?.show()
        }

        view.findViewById<TextView>(R.id.tv_n_teams).setOnClickListener {
            val chooseTeamAmountDialog: AlertDialog? = this.let {
                val chooseTeamAmountBuilder = AlertDialog.Builder(mainActivity)
                val dialogLayout = mainActivity.layoutInflater.inflate(R.layout.dialog_choose_number_of_teams, null)

                chooseTeamAmountBuilder.apply {

                    setView(dialogLayout)

                    setNegativeButton("Dismiss", null)
                    setPositiveButton("Create") {_, _ ->
                        val numberOfTeams: Int = dialogLayout.findViewById<EditText>(R.id.et_dialog_choose_number_of_teams_number).text.toString().toInt()

                        val alertDialog: AlertDialog? = this.let {
                            val builder = AlertDialog.Builder(context)

                            builder.apply {
                                setPositiveButton("Dismiss", null)
                                if(numberOfTeams < 1)
                                {
                                    setMessage("Sorry can't have 0 or less teams.")
                                }
                                else if(viewModel.getActiveMemberCount() >= numberOfTeams)
                                {
                                    val teams = viewModel.getRandomTeams(numberOfTeams)

                                    setCancelable(true)
                                    if(teams != null) {
                                        val sb = StringBuilder()
                                        teams.forEachIndexed { i, t ->
                                            sb.appendln("Team ${i + 1}: ")
                                            t.forEachIndexed { k, name ->
                                                sb.append(name)
                                                if (k + 1 < t.count()) {
                                                    sb.append(", ")
                                                }
                                            }
                                            sb.appendln()
                                            sb.appendln()
                                        }
                                        setMessage(sb.toString())
                                    }
                                    else
                                    {
                                        setMessage("This should not happen.")
                                    }
                                }
                                else
                                {
                                    setMessage("No or to few active members in group found for $numberOfTeams teams.")
                                }
                            }

                            builder.create()
                        }

                        alertDialog?.show()
                    }

                }

                chooseTeamAmountBuilder.create()
            }

            chooseTeamAmountDialog?.show()
        }

        return view
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(mainActivity).get(MainViewModel::class.java)
    }

}
