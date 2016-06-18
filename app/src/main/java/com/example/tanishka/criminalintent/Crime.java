package com.example.tanishka.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Tanishka on 15-06-2016.
 */
public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean isSolved;
    private String mSuspect;


    public String getmSuspect() {
        return mSuspect;
    }

    public void setmSuspect(String mSuspect) {
        this.mSuspect = mSuspect;
    }


    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }


    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }

    public UUID getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }
    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Crime() {
        this(UUID.randomUUID());
    }
    public Crime(UUID uuid){
        this.mId = uuid;
        mDate=new Date();
    }

}
