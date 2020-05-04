package com.example.consumerapp

import android.database.ContentObserver
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.adapter.UserFavoriteAdapter
import com.example.consumerapp.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.consumerapp.entity.UserItems
import com.example.consumerapp.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserFavoriteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showRecyclerList()


        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadUsersAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        if (savedInstanceState == null) {
            loadUsersAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserItems>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavoriteUsers = list
            }
        }

    }

    override fun onResume() {
        super.onResume()
        loadUsersAsync()
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
    }

    private fun loadUsersAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val deferredUsers = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBar.visibility = View.GONE
            val userItems = deferredUsers.await()
            if (userItems.size > 0) {
                adapter.listFavoriteUsers = userItems

            } else {
                adapter.listFavoriteUsers = ArrayList()
                showSnackBarMessage(resources.getString(R.string.no_data))
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavoriteUsers)
    }

    private fun deleteSelectedItem(id: Int, position: Int) {
        val uriWithId = Uri.parse("$CONTENT_URI/$id")
        val result = contentResolver.delete(uriWithId, null,null)
        if (result > 0){
            adapter.removeItem(position)
            showSnackBarMessage(resources.getString(R.string.txt_delete_notif, id))
        }
    }


    private fun showSnackBarMessage(message: String) {
        Snackbar.make(rv_users, message, Snackbar.LENGTH_SHORT).show()
    }
}
