package com.example.submission2fundamentalandroid.model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission2fundamentalandroid.entity.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler

import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class UserDetailModel: ViewModel() {


    private val userDetail = MutableLiveData<User>()

    fun setUserDetail(username: String, context: Context) {
        val client = AsyncHttpClient()
        val user = User()
        val url = "https://api.github.com/users/$username"
        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization",  UsersModel.TOKEN)
        client.get(url, object: AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                try {
                    val result = responseBody?.let { String(it) } as String

                    val jsonObject = JSONObject(result)
                    user.username = jsonObject.getString("login")
                    user.id = jsonObject.getInt("id")
                    user.avatar = jsonObject.getString("avatar_url")
                    user.name = checkNullText(jsonObject.getString("name"))
                    user.email = checkNullText(jsonObject.getString("email"))
                    user.company = checkNullText(jsonObject.getString("company"))
                    user.location = checkNullText(jsonObject.getString("location"))
                    user.repository = jsonObject.getInt("public_repos")
                    user.follower = jsonObject.getInt("followers")
                    user.following  = jsonObject.getInt("following")
                    userDetail.postValue(user)
                }catch (e: Exception){
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Toast.makeText(context, error?.message.toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun checkNullText(text: String): String {
        return if (text == "null") "." else text
    }

    fun getUserDetail(): LiveData<User>{
        return userDetail
    }
}