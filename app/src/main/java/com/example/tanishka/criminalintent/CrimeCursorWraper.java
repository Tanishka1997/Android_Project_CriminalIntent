package com.example.tanishka.criminalintent;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Tanishka on 18-06-2016.
 */
public class CrimeCursorWraper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWraper(Cursor cursor) {
        super(cursor);
    }
public Crime getCrime(){
 String uuidstring=getString(getColumnIndex(CrimedbSchema.CrimeTable.Cols.UUID));
    String title=getString(getColumnIndex(CrimedbSchema.CrimeTable.Cols.TITLE));
    long date=getLong(getColumnIndex(CrimedbSchema.CrimeTable.Cols.DATE));
    int solved=getInt(getColumnIndex(CrimedbSchema.CrimeTable.Cols.SOLVED));
    String suspect=getString(getColumnIndex(CrimedbSchema.CrimeTable.Cols.SUSPECT));
  Crime crime=new Crime(UUID.fromString(uuidstring));
    crime.setmTitle(title);
    crime.setmDate(new Date(date));
    crime.setSolved(solved!=0);
    crime.setmSuspect(suspect);
    return crime;
}
}
