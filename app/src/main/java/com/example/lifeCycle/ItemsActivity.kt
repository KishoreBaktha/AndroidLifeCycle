package com.example.lifeCycle

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_items.drawer_layout
import kotlinx.android.synthetic.main.activity_items.nav_view
import kotlinx.android.synthetic.main.app_bar_items.fab
import kotlinx.android.synthetic.main.app_bar_items.toolbar
import kotlinx.android.synthetic.main.content_items.listItems

class ItemsActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    NoteRecyclerAdapter.OnNoteSelectedListener {

    private val noteLayoutManager by lazy {
        LinearLayoutManager(this)
    }
    private val noteRecyclerAdapter by lazy {
        val adapter = NoteRecyclerAdapter(this, DataManager.loadNotes())
        adapter.setOnSelectedListener(this)
        adapter
    }

    private val courseLayoutManager by lazy {
        GridLayoutManager(this, 2)
    }
    private val courseRecyclerAdapter by lazy {
        CourseRecyclerAdapter(this, DataManager.courses.values.toList())
    }

    private val recentlyViewedNoteRecyclerAdapter by lazy {
        val adapter = NoteRecyclerAdapter(this, viewModel.recentlyViewedNotes)
        adapter.setOnSelectedListener(this)
        adapter
    }

    private val viewModel by lazy { ViewModelProvider(this)[ItemsActivityViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            startActivity(Intent(this, NoteActivity::class.java))
        }

        if (viewModel.isNewlyCreated)
            viewModel.restoreState(savedInstanceState)
        viewModel.isNewlyCreated = false

        handleDisplaySelection(viewModel.navDrawerDisplaySelection)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        viewModel.saveState(outState)
    }

    private fun displayNotes() {
        listItems.layoutManager = noteLayoutManager
        listItems.adapter = noteRecyclerAdapter

        nav_view.menu.findItem(R.id.nav_notes).isChecked = true
    }

    private fun displayCourses() {
        listItems.layoutManager = courseLayoutManager
        listItems.adapter = courseRecyclerAdapter

        nav_view.menu.findItem(R.id.nav_courses).isChecked = true
    }

    private fun displayRecentlyViewedNotes() {
        listItems.layoutManager = noteLayoutManager
        listItems.adapter = recentlyViewedNoteRecyclerAdapter

        nav_view.menu.findItem(R.id.nav_recent_notes).isChecked = true
    }

    override fun onResume() {
        super.onResume()
        listItems.adapter?.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_notes, R.id.nav_courses, R.id.nav_recent_notes -> {
                handleDisplaySelection(item.itemId)
                viewModel.navDrawerDisplaySelection = item.itemId
            }
            R.id.nav_share -> {
                handleSelection("Don't you think you've shared enough")
            }
            R.id.nav_send -> {
                handleSelection("Send")
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun handleDisplaySelection(itemId: Int) {
        when (itemId) {
            R.id.nav_notes -> {
                displayNotes()
            }
            R.id.nav_courses -> {
                displayCourses()
            }
            R.id.nav_recent_notes -> {
                displayRecentlyViewedNotes()
            }
        }
    }

    override fun onNoteSelected(note: NoteInfo) {
        viewModel.addToRecentlyViewedNotes(note)
    }

    private fun handleSelection(message: String) {
        Snackbar.make(listItems, message, Snackbar.LENGTH_LONG).show()
    }
}
