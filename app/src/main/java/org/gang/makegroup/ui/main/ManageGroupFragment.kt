package org.gang.makegroup.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.gang.makegroup.MainActivity
import org.gang.makegroup.R

class ManageGroupFragment(private val mainActivity: MainActivity) : Fragment() {

    companion object {
        fun newInstance(mainActivity: MainActivity) = ManageGroupFragment(mainActivity)
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.group_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(mainActivity).get(MainViewModel::class.java)

        val adapter = ArrayAdapter<String>(mainActivity,
            android.R.layout.simple_list_item_checked,
            viewModel.getNameList()
        )


        val listView = view?.findViewById<ListView>(R.id.lv_group_list)

        listView.apply{
            this?.adapter = adapter
            this?.choiceMode = ListView.CHOICE_MODE_MULTIPLE
            viewModel.getActiveStatus().forEachIndexed { i, status ->
                this?.setItemChecked(i, status)
            }
            this?.setOnItemClickListener {_, _, position, _ ->
                viewModel.toggleActive(position)
            }

            this?.setOnItemLongClickListener { _, _, position, _ ->
                val alertDialog: AlertDialog? = this.let {
                    val builder = AlertDialog.Builder(context)

                    builder.apply {

                        setPositiveButton("Delete") { _, _ ->
                            viewModel.removeMember(position)
                            adapter.remove(adapter.getItem(position))
                            adapter.notifyDataSetChanged()
                        }

                        setNegativeButton("Cancel", null)

                        setCancelable(true)
                        setMessage("Delete?")
                    }

                    builder.create()
                }

                alertDialog?.show()

                true
            }
        }

        view?.findViewById<FloatingActionButton>(R.id.fab_add_member)?.setOnClickListener {
            val alertDialog: AlertDialog? = this.let {
                val builder = AlertDialog.Builder(mainActivity)
                val dialogLayout = mainActivity.layoutInflater.inflate(R.layout.dialog_add_member, null)


                builder.apply {

                    setPositiveButton("Create") {_, _ ->
                        val name: String = dialogLayout.findViewById<EditText>(R.id.et_dialog_add_member_name).text.toString()
                        if(name.isNotBlank())
                        {
                            viewModel.addMember(name)
                            adapter.add(name)
                            adapter.notifyDataSetChanged()

                            listView?.setItemChecked(adapter.count - 1, true)
                        }
                    }

                    setNegativeButton("Cancel", null)

                    setCancelable(true)
                    setMessage("Name?")

                    setView(dialogLayout)
                }

                builder.create()
            }

            alertDialog?.show()
        }
    }

}
