package com.example.pizzaapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DatabaseHelper(var context:Context): SQLiteOpenHelper(
    context,DATABASE_NAME, null,DATABASE_VERSION
) {
    companion object{
        private val DATABASE_NAME = "pizza"
        private val DATABASE_VERSION = 1
        //table name
        private val TABLE_ACCOUNT = "account"
        //column account table
        private val COLUMN_EMAIL = "email"
        private val COLUMN_NAME = "name"
        private val COLUMN_LEVEL = "level"
        private val COLUMN_PASSWORD = "password"
    }

    //creat table account sql query
    private val CREATE_ACCOUNT_TABLE = ("CREATE TABLE " + TABLE_ACCOUNT + "("
            + COLUMN_EMAIL + " TEXT PRIMARY KEY, "+ COLUMN_NAME +" TEXT, "
            + COLUMN_LEVEL + " TEXT, "+ COLUMN_PASSWORD +" TEXT)")

    //drop table account sql query
    private val DROP_ACCOUNT_TABLE = "DROP TABLE IF EXISTS $TABLE_ACCOUNT"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_ACCOUNT_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(CREATE_ACCOUNT_TABLE)
        onCreate(p0)
    }

    //login check
    fun checkLogin(email:String, password:String):Boolean{
        val colums = arrayOf(COLUMN_NAME)
        val db = this.readableDatabase
        //selection criteria
        val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        //selection arguments
        val selectionArgs = arrayOf(email,password)

        val cursor = db.query(TABLE_ACCOUNT, //table to query
            colums, //colums to return
            selection, //colums for WHERE clause
            selectionArgs, //the values for the WHERE clause
            null, //group the rows
            null, //filter by row groups
            null) //the sort order

        val cursorCount = cursor.count
        cursor.close()
        db.close()

        //check data available or not
        if(cursorCount > 0)
            return true
        else
            return false
    }

    //add user
    fun addAccount(email: String, name:String, level:String, password: String){
        val db = this.readableDatabase

        val values = ContentValues()
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_LEVEL, level)
        values.put(COLUMN_PASSWORD, password)

        val result = db.insert(TABLE_ACCOUNT, null, values)
        //show message
        if (result==(0).toLong()){
            Toast.makeText(context, "Register Failed", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context, "Register Success, " + "Please login using new account", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }
    //check data
    fun checkData(email:String):String {
        val colums = arrayOf(COLUMN_NAME)
        val db = this.readableDatabase
        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)
        var name: String = ""

        val cursor = db.query(
            TABLE_ACCOUNT, //table to query
            colums, //colums to return
            selection, //colums for WHERE clause
            selectionArgs, //the values for the WHERE clause
            null, //group the rows
            null, //filter by row groups
            null
        ) //the sort order

        if(cursor.moveToFirst()){
            name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
        }
        cursor.close()
        db.close()
        return name
    }
}