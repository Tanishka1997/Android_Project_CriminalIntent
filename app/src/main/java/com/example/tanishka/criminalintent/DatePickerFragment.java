package com.example.tanishka.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Tanishka on 16-06-2016.
 */
public class DatePickerFragment extends DialogFragment {
private DatePicker mDatePicker;
private static final String ARG_DATE="DATE";
public static final String EXTRA_DATE="date";
    public static DatePickerFragment newInstance(Date date) {
         Bundle args = new Bundle();
        args.putSerializable(ARG_DATE,date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date=(Date) getArguments().getSerializable(ARG_DATE);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH);
        int year=calendar.get(Calendar.YEAR);
        View v= LayoutInflater.from(getActivity()).inflate(R.layout.date_picker,null);
        mDatePicker=(DatePicker) v.findViewById(R.id.dialog_date_date_picker) ;
        mDatePicker.init(year,month,day,null);
        return new AlertDialog.Builder(getActivity())
                .setTitle("Date of Crime")
                .setView(v)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    int year=mDatePicker.getYear();
                        int month=mDatePicker.getMonth();
                        int day=mDatePicker.getDayOfMonth();
                        Date date=new GregorianCalendar(year,month,day).getTime();
                        sendResult(Activity.RESULT_OK,date);
                    }
                })
                .create();
    }
 public void sendResult(int ResultCode,Date date){
     if (getTargetFragment()==null)
         return;
     Intent intent=new Intent();
     intent.putExtra(EXTRA_DATE,date);
     getTargetFragment().onActivityResult(getTargetRequestCode(),ResultCode,intent);
 }
}
