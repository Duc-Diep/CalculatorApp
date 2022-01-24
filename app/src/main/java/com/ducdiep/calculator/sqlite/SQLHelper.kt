package com.ducdiep.calculator.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ducdiep.calculator.model.Calculation
import java.util.ArrayList


const val DB_NAME = "Calculations.db"
const val DB_TABLE = "Calculation"
const val DB_COLUMN_INPUT = "input"
const val DB_COLUMN_OUTPUT = "output"
const val DB_VERSION = 1

class SQLHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    lateinit var sqlIteDatabase: SQLiteDatabase
    lateinit var contentValue: ContentValues
    override fun onCreate(db: SQLiteDatabase) {
        val queryCreateTable = "CREATE TABLE $DB_TABLE($DB_COLUMN_INPUT Text,$DB_COLUMN_OUTPUT INTEGER)"
        db.execSQL(queryCreateTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (newVersion!=oldVersion){
            db.execSQL("DROP TABLE IF EXISTS $DB_TABLE")
            onCreate(db)
        }
    }
    fun addCalculation(cal: Calculation) {
        sqlIteDatabase = writableDatabase
        contentValue = ContentValues()
        contentValue.put(DB_COLUMN_INPUT, cal.input)
        contentValue.put(DB_COLUMN_OUTPUT, cal.output)
        sqlIteDatabase.insert(DB_TABLE, null, contentValue)
    }

    @SuppressLint("Range")
    fun getAllCalculation(): List<Calculation> {
        val list: MutableList<Calculation> = ArrayList()
        sqlIteDatabase = readableDatabase
        val cursor: Cursor = sqlIteDatabase.query(
            false,
            DB_TABLE,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            val input = cursor.getString(cursor.getColumnIndex(DB_COLUMN_INPUT))
            val output = cursor.getString(cursor.getColumnIndex(DB_COLUMN_OUTPUT))
            list.add(Calculation(input, output))
        }
        return list
    }

}