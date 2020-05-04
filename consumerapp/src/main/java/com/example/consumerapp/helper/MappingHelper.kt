package com.example.consumerapp.helper

import android.database.Cursor
import com.example.consumerapp.db.DatabaseContract
import com.example.consumerapp.entity.UserItems


object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<UserItems> {
        val usersList = ArrayList<UserItems>()
        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))
                usersList.add(UserItems(id, username, avatar))
            }
        }
        return usersList
    }

}