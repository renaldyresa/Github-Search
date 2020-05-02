package com.example.submission2fundamentalandroid

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submission2fundamentalandroid.db.DatabaseContract
import com.example.submission2fundamentalandroid.db.UserHelper
import com.example.submission2fundamentalandroid.entity.User
import com.example.submission2fundamentalandroid.entity.UserItems
import com.example.submission2fundamentalandroid.model.UserDetailModel
import com.example.submission2fundamentalandroid.adapter.SectionPagerAdapter
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.android.synthetic.main.layout_profile_user.*
import kotlinx.coroutines.InternalCoroutinesApi

class UserDetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        const val EXTRA_USERNAME = "extra_username"
    }
    private lateinit var userDetailModel: UserDetailModel
    private lateinit var userItems: UserItems
    private lateinit var userHelper: UserHelper

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        supportActionBar?.elevation = 2f

        userItems = UserItems()
        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        val username = intent.getStringExtra(EXTRA_USERNAME) as String
        userDetailModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            UserDetailModel::class.java)
        userDetailModel.setUserDetail(username, this)
        userDetailModel.getUserDetail().observe(this, Observer {
            setTextDetailUser(it)
            userItems.username = it.username
            userItems.id = it.id
            userItems.avatar = it.avatar
            progressBar_detail.visibility = View.GONE

        })

        val sectionsPagerAdapter =
            SectionPagerAdapter(this@UserDetailActivity, supportFragmentManager, username)
        view_pager.adapter = sectionsPagerAdapter
        tabs_detail.setupWithViewPager(view_pager)

        btn_add_favorite_user.setOnClickListener(this)
        btn_add_favorite_user.visibility = View.GONE
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

    private fun setTextDetailUser(user: User){
        Glide.with(this)
            .load(user.avatar)
            .apply(RequestOptions().override(100,100))
            .into(img_avatar2)
        tv_username.text = user.username
        tv_id.text = user.id.toString()
        tv_name.text = user.name
        tv_email.text = user.email
        tv_company.text = user.company
        tv_location.text = user.location
        tv_repo.text = manipulateText(R.plurals.repo, user.repository)
        tabs_detail.getTabAt(0)?.text = manipulateText(R.plurals.follower, user.follower)
        tabs_detail.getTabAt(1)?.text = manipulateText(R.plurals.following, user.following)
        if (userHelper.checkIdInDatabase(user.id.toString())){
            buttonFavoriteType(true)
        }
        btn_add_favorite_user.visibility = View.VISIBLE
    }

    private fun buttonFavoriteType(type: Boolean){
        if (type){
            btn_add_favorite_user.setBackgroundColor(resources.getColor(R.color.in_btn,theme))
            btn_add_favorite_user.text = resources.getText(R.string.delete_from_favorite_user)
            btn_add_favorite_user.icon = resources.getDrawable(R.drawable.ic_remove_white_24dp,theme)
        }else{
            btn_add_favorite_user.setBackgroundColor(resources.getColor(R.color.colorAccent,theme))
            btn_add_favorite_user.text = resources.getText(R.string.add_favorite_user)
            btn_add_favorite_user.icon = resources.getDrawable(R.drawable.ic_add_white_24dp,theme)
        }
    }

    private fun manipulateText(id: Int, num: Int) = resources.getQuantityString(id, num, num)

    override fun onClick(v: View) {
        if (v.id == R.id.btn_add_favorite_user){
            if (btn_add_favorite_user.text == resources.getString(R.string.add_favorite_user)){
                val values = ContentValues()
                values.put(DatabaseContract.UserColumns._ID, userItems.id)
                values.put(DatabaseContract.UserColumns.USERNAME, userItems.username)
                values.put(DatabaseContract.UserColumns.AVATAR, userItems.avatar)

                val result = userHelper.insert(values)
                if (result > 0 ){
                    buttonFavoriteType(true)
                }else
                    Toast.makeText(this, "gagal", Toast.LENGTH_SHORT).show()
            }else{
                val result = userHelper.deleteById(userItems.id.toString()).toLong()
                if (result > 0){
                    buttonFavoriteType(false)
                }else
                    Toast.makeText(this, "gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        userHelper.close()
    }


}
