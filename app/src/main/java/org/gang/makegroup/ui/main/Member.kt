package org.gang.makegroup.ui.main

data class Member(val name: String, var active: Boolean) {
    fun toggleStatus() {
        active = !active
    }
}