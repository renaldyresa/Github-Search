package com.example.submission2fundamentalandroid.ui.following

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission2fundamentalandroid.*
import com.example.submission2fundamentalandroid.entity.UserItems
import com.example.submission2fundamentalandroid.adapter.SectionPagerAdapter
import com.example.submission2fundamentalandroid.adapter.UserAdapter
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {

    private lateinit var adapter: UserAdapter
    private lateinit var followingModel: FollowingModel
    private lateinit var username: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(SectionPagerAdapter.EXTRA_USERNAME)?.let {
            username = it
        }
        adapter =
            UserAdapter()
        adapter.notifyDataSetChanged()
        showRecyclerList()
        followingModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowingModel::class.java)
        getDataList(username)
        followingModel.getUsers().observe(viewLifecycleOwner, Observer { userItems ->
            if (followingModel.getTotalCount() != 0) {
                tv_status.visibility = View.GONE
            }else{
                tv_status.visibility = View.VISIBLE
            }
            adapter.setData(userItems)
            showLoading(false)
        })
    }

    private fun showRecyclerList(){
        rv_users.layoutManager = LinearLayoutManager(activity)
        rv_users.adapter = adapter
        adapter.setOnItemClickCallback(object :
            UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserItems) {
                showSelectedUser(data)
            }

        })
    }

    private fun showSelectedUser(data: UserItems) {
        val moveToUserDetailIntent = Intent(activity, UserDetailActivity::class.java)
        moveToUserDetailIntent.putExtra(UserDetailActivity.EXTRA_USERNAME, data.username)
        startActivity(moveToUserDetailIntent)
    }

    private fun getDataList(text: String){
        tv_status.visibility = View.GONE
        if (text.isNotEmpty()){
            activity?.let { followingModel.setFollowingItem(text, it) }
            showLoading(true)
        }else showLoading(false)
    }

    private fun showLoading(state: Boolean) {
        progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

}
