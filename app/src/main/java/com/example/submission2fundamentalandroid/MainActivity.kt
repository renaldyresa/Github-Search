package com.example.submission2fundamentalandroid

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission2fundamentalandroid.entity.UserItems
import com.example.submission2fundamentalandroid.model.UsersModel
import com.example.submission2fundamentalandroid.adapter.UserAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var adapter: UserAdapter
    private lateinit var userModel: UsersModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter =
            UserAdapter()
        adapter.notifyDataSetChanged()
        showRecyclerList()
        text_search.setOnEditorActionListener { _, actionId, _ ->
            when (actionId)  {
                EditorInfo.IME_ACTION_SEND -> {
                    getDataList(this@MainActivity, text_search )
                    true
                }
                else -> false
            }
        }
        btn_search.setOnClickListener(this)

        userModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            UsersModel::class.java)
        userModel.getUsers().observe(this, Observer { userItems ->
            if (userModel.getTotalCount() != 0) {
                tv_status.visibility = View.GONE
                adapter.setData(userItems)
            }else{
                tv_status.visibility = View.VISIBLE
            }
            showLoading(false)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.setting_menu -> startActivity(Intent(this, SettingActivity::class.java))
            R.id.favorite_user_menu -> startActivity(Intent(this, UserFavoriteActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showRecyclerList(){
        rv_users.layoutManager = LinearLayoutManager(this)
        rv_users.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: UserItems) {
                showSelectedUser(data)
            }

        })
    }

    private fun showSelectedUser(data: UserItems) {
        val moveToUserDetailIntent = Intent(this, UserDetailActivity::class.java)
        moveToUserDetailIntent.putExtra(UserDetailActivity.EXTRA_USERNAME, data.username)
        startActivity(moveToUserDetailIntent)
    }

    private fun getDataList(context: Context, view: View){
        tv_status.visibility = View.GONE
        val text = text_search.text.toString().replace(" ","").trim()
        if (text.isNotEmpty()){
            userModel.setUserItem(text, this)
            showLoading(true)
            hideKeyboardFrom(context, view)
        }else showLoading(false)
    }

    private fun showLoading(state: Boolean) {
        progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.btn_search -> getDataList(this@MainActivity, text_search )
        }
    }
}
