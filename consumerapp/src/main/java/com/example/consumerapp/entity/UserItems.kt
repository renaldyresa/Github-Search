package com.example.consumerapp.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserItems (
    var id: Int? = null,
    var username: String? = null,
    var avatar: String? = null
): Parcelable