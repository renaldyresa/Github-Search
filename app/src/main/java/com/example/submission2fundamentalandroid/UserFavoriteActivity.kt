package com.example.submission2fundamentalandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission2fundamentalandroid.db.UserHelper
import com.example.submission2fundamentalandroid.entity.UserItems
import com.example.submission2fundamentalandroid.helper.MappingHelper
import com.example.submission2fundamentalandroid.adapter.UserFavoriteAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite_user.*
import kotlinx.coroutines.*

class UserFavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: UserFavoriteAdapter
    private lateinit var userHelper: UserHelper

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_user)

        showRecyclerList()

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserItems>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavoriteUsers = list
            }
        }

    }

    private fun showRecyclerList(){
        rv_users.layoutManager = LinearLayoutManager(this)
        rv_users.setHasFixedSize(true)
        adapter = UserFavoriteAdapter()
        rv_users.adapter = adapter
        adapter.setOnButtonDeleteClickCallback(object : UserFavoriteAdapter.OnButtonDeleteClickCallback {
            override fun onButtonDeleteClicked(id: Int, position: Int) {
                deleteSelectedItem(id, position)
            }
        })
        adapter.setOnItemClickCallback(object : UserFavoriteAdapter.OnItemClickCallback{
            override fun onItemClicked(username: String) {
                showSelectedUser(username)
            }
        })
    }


    private fun showSelectedUser(username: String) {
        val moveToUserDetailIntent = Intent(this, UserDetailActivity::class.java)
        moveToUserDetailIntent.putExtra(UserDetailActivity.EXTRA_USERNAME, username)
        startActivity(moveToUserDetailIntent)
    }


    private fun deleteSelectedItem(id: Int, position: Int) {
        val result = userHelper.deleteById(id.toString()).toLong()
        if (result > 0){
            adapter.removeItem(position)
            showSnackBarMessage("Data user id $id dihapus")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavoriteUsers)
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = userHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBar.visibility = View.GONE
            val userItems = deferredNotes.await()
            if (userItems.size > 0) {
                adapter.listFavoriteUsers = userItems

            } else {
                adapter.listFavoriteUsers = ArrayList()
                showSnackBarMessage("Tidak ada data saat ini")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userHelper.close()
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(rv_users, message, Snackbar.LENGTH_SHORT).show()
    }

}
