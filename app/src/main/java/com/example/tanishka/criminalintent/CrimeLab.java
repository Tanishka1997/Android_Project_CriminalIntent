package com.example.tanishka.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Tanishka on 15-06-2016.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private final Context mContext;

    private SQLiteDatabase db;

    public List<Crime> getmCrimes() {
        List<Crime> crimes=new ArrayList<>();
        CrimeCursorWraper cursorWraper= query(null,null);
        try {
            cursorWraper.moveToFirst();
            while (!cursorWraper.isAfterLast()){
                crimes.add(cursorWraper.getCrime());
                cursorWraper.moveToNext();
            }
        }
        finally {
            cursorWraper.close();
        }
    return crimes;
    }

    public void addCrime(Crime c){
      ContentValues values=getValues(c);
        db.insert(CrimedbSchema.CrimeTable.NAME,null,values);
    }
    public static CrimeLab get(Context context){
        if (sCrimeLab==null){
            sCrimeLab=new CrimeLab(context);
        }
       return sCrimeLab;
    }
  private CrimeLab(Context context){

      mContext=context.getApplicationContext();
      db=new CrimeBaseHelper(mContext).getWritableDatabase();
  }
   public void delete(Crime crime){
       db.delete(CrimedbSchema.CrimeTable.NAME,CrimedbSchema.CrimeTable.Cols.UUID+"=?",new String[]{crime.getmId().toString()});
   }
    public Crime getCrime(UUID uuid){
      CrimeCursorWraper cursorWraper=query(CrimedbSchema.CrimeTable.Cols.UUID+"=?",new String[]{uuid.toString()});
      try {
          if (cursorWraper.getCount()==0)
              return  null;
          cursorWraper.moveToFirst();
          return cursorWraper.getCrime();
      }
    finally {
          cursorWraper.close();
      }
  }
public void update(Crime crime){
    String id=crime.getmId().toString();
    ContentValues values=getValues(crime);
    db.update(CrimedbSchema.CrimeTable.NAME,values,CrimedbSchema.CrimeTable.Cols.UUID+"=?",new String[]{id});
}

    public static ContentValues getValues(Crime crime){
    ContentValues values=new ContentValues();
    values.put(CrimedbSchema.CrimeTable.Cols.UUID,crime.getmId().toString());
    values.put(CrimedbSchema.CrimeTable.Cols.TITLE,crime.getmTitle());
    values.put(CrimedbSchema.CrimeTable.Cols.DATE,crime.getmDate().getTime());
    values.put(CrimedbSchema.CrimeTable.Cols.SOLVED,crime.isSolved()?1:0);
    values.put(CrimedbSchema.CrimeTable.Cols.SUSPECT,crime.getmSuspect());
    return values;
 }
public CrimeCursorWraper query(String where,String[] whereClause){
    Cursor cursor=db.query(CrimedbSchema.CrimeTable.NAME,null,where,whereClause,null,null,null,null);
    return new CrimeCursorWraper(cursor);
}
}
