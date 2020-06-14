package org.gang.makegroup.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import org.gang.makegroup.MainActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil

class MainViewModel : ViewModel() {
    private val members: ArrayList<Member> = arrayListOf()
    private var random: Random = Random()
    private val saveFileName: String = "save"

    fun getActiveMemberCount() : Int {
        return members.filter { it.active }.count()
    }

    fun getNameList() : ArrayList<String> {
        val retList = arrayListOf<String>()
        members.forEach {
            retList.add(it.name)
        }
        return retList
    }

    fun addMember(name: String, selected: Boolean = true) {
        members.add(Member(name, selected))
    }

    private fun getActiveMemberNames() : ArrayList<String> {
        val retList = ArrayList<String>()

        members.forEach {
            if(it.active) {
                retList.add(it.name)
            }
        }

        return retList
    }

    fun getActiveStatus() : ArrayList<Boolean> {
        val retList = ArrayList<Boolean>()

        members.forEach {
            retList.add(it.active)
        }

        return retList
    }

    fun toggleActive(index: Int) {
        if(index >= 0 && index < members.size) {
            members[index].toggleStatus()
        }
    }

    fun getRandomMemberName() : String? {

        val list = getActiveMemberNames()

        return if(list.size < 1) {
            null
        } else {
            list[random.nextInt(list.size)]
        }
    }

    fun getRandomTeams(numberOfTeams: Int) : ArrayList<ArrayList<String>>? {

        val list = getActiveMemberNames()

        if(list.size < numberOfTeams || list.size < 1) {
            return null
        }


        val teams = ArrayList<ArrayList<String>>()
        for(i in 0 until numberOfTeams) {
            teams.add(ArrayList())
        }

        val notUsedIndexList = arrayListOf<Int>()
        notUsedIndexList.addAll(0 until list.size)

        val maxMembersPerTeam : Int = ceil(list.size.toFloat() / (numberOfTeams.toFloat())).toInt()

        for(i in 0 until maxMembersPerTeam) {
            for(k in 0 until numberOfTeams) {
                if (notUsedIndexList.size == 0) {
                    break
                }

                val idx = random.nextInt(notUsedIndexList.size)

                teams[k].add(list[notUsedIndexList[idx]])
                notUsedIndexList.removeAt(idx)
            }
        }

        return teams
    }

    fun removeMember(position: Int) {
        members.removeAt(position)
    }

    fun save(mainActivity: MainActivity) {

        OutputStreamWriter(mainActivity.openFileOutput(saveFileName, Context.MODE_PRIVATE)).use {
            it.write(Gson().toJson(members))
        }
    }

    fun load(mainActivity: MainActivity) {

        if(!mainActivity.fileList().contains(saveFileName)) {
            return
        }

        var string = ""
        BufferedReader(InputStreamReader(mainActivity.openFileInput(saveFileName))).use {
            string = it.readText()
        }

        members.addAll(Gson().fromJson(string, Array<Member>::class.java))
    }

}
