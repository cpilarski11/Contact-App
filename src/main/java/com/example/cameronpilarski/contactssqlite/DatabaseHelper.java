package com.example.cameronpilarski.contactssqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cameronpilarski on 12/2/17.
 *
 * this activity creates database and handles queries
 *
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // database
    private static final String DATABASE_NAME = "ContactsTest.db";

    // table 1
    private static final String TABLE_NAME = "contact_table";

    // table 1 columns and attributes
    private static final String COL_1 = "ID";
    private static final String COL_2 = "FIRST";
    private static final String COL_3 = "LAST";
    private static final String COL_4 = "PHONE";
    private static final String COL_5 = "EMAIL";
    private static final String COL_6 = "ADDRESS1";
    private static final String COL_7 = "ADDRESS2";
    private static final String COL_8 = "PICTURE";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create the tables
        db.execSQL("create table " + TABLE_NAME + " (ID TEXT PRIMARY KEY, FIRST TEXT, LAST TEXT, PHONE TEXT, EMAIL TEXT, ADDRESS1 TEXT, ADDRESS2 TEXT, PICTURE TEXT)");
        //db.execSQL("create table " + TABLE_NAME2 + " (ID TEXT PRIMARY KEY, FOREIGN KEY (ID) REFERENCES contact_table(ID))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // need to drop singleton first because of FK contsraint
        //db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }


    // inserting data into contact table
    boolean insertContactData(String ID, String first, String last, String phone, String email, String add1, String add2, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,ID);
        contentValues.put(COL_2,first);
        contentValues.put(COL_3,last);
        contentValues.put(COL_4,phone);
        contentValues.put(COL_5,email);
        contentValues.put(COL_6,add1);
        contentValues.put(COL_7,add2);
        contentValues.put(COL_8, image);
        long result = db.insert(TABLE_NAME,null ,contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }


    // query to get all contact info
    Cursor getAllContactData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    // query to get first and last name for every contact
    // use for listView
    Cursor getContactFirstLast() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select FIRST, LAST from " + TABLE_NAME,null);
        return res;
    }

    // query to get contact info for a particular contact
    public Cursor getContactData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where 'ID = ?' ", null);
        return res;
    }

    public boolean updateContactData(String ID, String first,String last, String phone,String email, String add1, String add2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,ID);
        contentValues.put(COL_2,first);
        contentValues.put(COL_3,last);
        contentValues.put(COL_4,phone);
        contentValues.put(COL_5,email);
        contentValues.put(COL_6,add1);
        contentValues.put(COL_7,add2);
        //contentValues.put(COL_8,picture);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { ID });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
}
