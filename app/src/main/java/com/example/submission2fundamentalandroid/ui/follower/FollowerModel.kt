package com.example.submission2fundamentalandroid.ui.follower

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission2fundamentalandroid.entity.UserItems
import com.example.submission2fundamentalandroid.model.UsersModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowerModel: ViewModel() {

    private val listUsers = MutableLiveData<ArrayList<UserItems>>()
    private var totalCount = 0
    private val client = AsyncHttpClient()

    fun getTotalCount(): Int{
        return totalCount
    }

    fun setFollowerItem(username: String, context: Context){
        val listItems = ArrayList<UserItems>()
        val token = UsersModel.TOKEN
        val url = "https://api.github.com/users/$username/followers"
        client.cancelAllRequests(true)
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization",  token)

        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                try {
                    val result = responseBody?.let { String(it) } as String
                    val list: JSONArray
                    list = JSONArray(result)
                    totalCount = list.length()

                    for (i in 0 until list.length()){
                        val user = list.getJSONObject(i)
                        val userItems =
                            UserItems()
                        userItems.username = user.getString("login")
                        userItems.id = user.getInt("id")
                        userItems.avatar = user.getString("avatar_url")
                        listItems.add(userItems)
                    }
                    listUsers.postValue(listItems)
                }catch (e: Exception){
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Toast.makeText(context, error?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getUsers() : LiveData<ArrayList<UserItems>> {
        return listUsers
    }

}