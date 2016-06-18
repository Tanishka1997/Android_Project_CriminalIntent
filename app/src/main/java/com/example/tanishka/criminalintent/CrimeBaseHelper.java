package com.example.tanishka.criminalintent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tanishka on 18-06-2016.
 */
public class CrimeBaseHelper extends SQLiteOpenHelper {
public static final int version=1;
public static final String DATABASE_NAME="crime.db";
    public CrimeBaseHelper(Context context) {
        super(context,DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
     db.execSQL("create table "+CrimedbSchema.CrimeTable.NAME+"(" + "_id integer primary key autoincrement,"
                   +CrimedbSchema.CrimeTable.Cols.UUID +","+
                     CrimedbSchema.CrimeTable.Cols.TITLE+","+
                     CrimedbSchema.CrimeTable.Cols.DATE+","
                      +CrimedbSchema.CrimeTable.Cols.SOLVED+","+CrimedbSchema.CrimeTable.Cols.SUSPECT +")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CrimedbSchema.CrimeTable.NAME);
        onCreate(db);
    }
}
