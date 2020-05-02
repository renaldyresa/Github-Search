package com.example.submission2fundamentalandroid.entity


data class User(
    var name: String = "",
    var email: String = "",
    var company: String ="",
    var location: String = "",
    var repository: Int = 0,
    var username: String = "",
    var id: Int = 0,
    var avatar: String = "",
    var follower: Int = 0,
    var following: Int = 0
)