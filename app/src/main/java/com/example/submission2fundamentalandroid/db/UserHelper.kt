package com.example.submission2fundamentalandroid.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.submission2fundamentalandroid.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.example.submission2fundamentalandroid.db.DatabaseContract.UserColumns.Companion._ID
import kotlinx.coroutines.InternalCoroutinesApi

import kotlinx.coroutines.internal.synchronized
import java.sql.SQLException

class UserHelper(context: Context)  {

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME

        private lateinit var dataBaseHelper: DatabaseHelper
        private var INSTANCE: UserHelper? = null
        private lateinit var database: SQLiteDatabase

        @InternalCoroutinesApi
        fun getInstance(context: Context): UserHelper =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: UserHelper(context)
            }
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }


    @Throws(SQLException::class)
    fun open(){
        database = dataBaseHelper.writableDatabase
    }

    fun close(){
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC")
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    fun checkIdInDatabase(id: String): Boolean {
        val cursor = queryById(id)
        val count = cursor.count
        return count > 0
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }
}