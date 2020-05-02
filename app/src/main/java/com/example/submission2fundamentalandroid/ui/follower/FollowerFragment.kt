package com.example.submission2fundamentalandroid.ui.follower

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
import kotlinx.android.synthetic.main.fragment_follower.*

class FollowerFragment : Fragment() {

    private lateinit var adapter: UserAdapter
    private lateinit var followerModel: FollowerModel
    private lateinit var username: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_follower, container, false)
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
        followerModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowerModel::class.java)
        getDataList(username)
        followerModel.getUsers().observe(viewLifecycleOwner, Observer { userItems ->
            if (followerModel.getTotalCount() != 0) {
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
            activity?.let { followerModel.setFollowerItem(text, it) }
            showLoading(true)
        }else showLoading(false)
    }

    private fun showLoading(state: Boolean) {
        progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

}
