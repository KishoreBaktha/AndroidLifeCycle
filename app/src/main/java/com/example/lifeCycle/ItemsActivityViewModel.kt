package com.example.lifeCycle

import android.os.Bundle
import androidx.lifecycle.ViewModel

const val MAX_RECENT_NOTES = 5

class ItemsActivityViewModel : ViewModel() {

    var isNewlyCreated  = true

    var navDrawerDisplaySelection = R.id.nav_notes
    val recentlyViewedNotes = ArrayList<NoteInfo>(MAX_RECENT_NOTES)
    var navDrawerDisplaySelectionName = "com.example.LifeCycle.ItemsActivityViewModel.navDrawerDisplaySelection"
    var recentlyViewedNoteIdsName = "com.example.LifeCycle.ItemsActivityViewModel.recentlyViewedNoteIds"

    fun addToRecentlyViewedNotes(note: NoteInfo) {
        // Check if selection is already in the list
        val existingIndex = recentlyViewedNotes.indexOf(note)
        if (existingIndex == -1) {
            // it isn't in the list...
            // Add new one to beginning of list and remove any beyond max we want to keep
            recentlyViewedNotes.add(0, note)
            for (index in recentlyViewedNotes.lastIndex downTo MAX_RECENT_NOTES)
                recentlyViewedNotes.removeAt(index)
        } else {
            // it is in the list...
            // Shift the ones above down the list and make it first member of the list
            for (index in (existingIndex - 1) downTo 0)
                recentlyViewedNotes[index + 1] = recentlyViewedNotes[index]
            recentlyViewedNotes[0] = note
        }
    }

    fun saveState(outState: Bundle?) {
        outState?.putInt(navDrawerDisplaySelectionName, navDrawerDisplaySelection)
        val noteIds = DataManager.noteIdsAsIntArray(recentlyViewedNotes)
        outState?.putIntArray(recentlyViewedNoteIdsName, noteIds)
    }

    fun restoreState(savedInstanceState: Bundle?) {
        navDrawerDisplaySelection = savedInstanceState?.getInt(navDrawerDisplaySelectionName) ?: R.id.nav_notes
        val noteIds = savedInstanceState?.getIntArray(recentlyViewedNoteIdsName)
        val noteList= noteIds?.let { DataManager.loadNotes(*it) }
        noteList?.let { recentlyViewedNotes.addAll(it) }
    }
}