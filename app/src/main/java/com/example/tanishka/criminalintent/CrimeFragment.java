package com.example.tanishka.criminalintent;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrimeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static final String crime_id="crime_id";
    private String mParam1;
    private String mParam2;
    private Crime mCrime;
    private EditText mTextField;
    private Button mButton;
    private CheckBox mSolvedCheckBox;
    private static final String DIALOG_DATE="Dialog_Date";
    private static final int REQUEST_CODE=0;
    private Button mSendReport;
    private Button mSuspectButton;
    public CrimeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment CrimeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrimeFragment newInstance(UUID uuid) {
        CrimeFragment fragment = new CrimeFragment();
        Bundle args = new Bundle();
        args.putSerializable(crime_id,uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
        UUID muuid=(UUID) getArguments().getSerializable(crime_id);
        mCrime=CrimeLab.get(getActivity()).getCrime(muuid);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.remove_from_list_delete:
                CrimeLab.get(getActivity()).delete(mCrime);
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.remove_from_list,menu);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_crime, container, false);
        mTextField=(EditText) view.findViewById(R.id.crime_title);
        mButton=(Button) view.findViewById(R.id.crime_date);
        mButton.setText(mCrime.getmDate().toString());
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm=getFragmentManager();
                DatePickerFragment datePickerFragment=DatePickerFragment.newInstance(mCrime.getmDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this,REQUEST_CODE);
                datePickerFragment.show(fm,DIALOG_DATE);
            }
        });
        mSendReport=(Button) view.findViewById(R.id.crime_send_report);
        mSendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT,"CriminalIntent Crime Report");
                intent=Intent.createChooser(intent,"Send report via");
                startActivity(intent);
            }
        });
        mSolvedCheckBox=(CheckBox) view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        mSuspectButton=(Button) view.findViewById(R.id.crime_suspect);
        final Intent i=new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(i,1);
            }
        });
        mTextField.setText(mCrime.getmTitle());
        mTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                  //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            mCrime.setmTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            //
            }
        });
     if (mCrime.getmSuspect()!=null)
         mSuspectButton.setText(mCrime.getmSuspect());
        return view;
    }

    private String getCrimeReport(){
        String solvedString=null;
        if (mCrime.isSolved())
            solvedString=getString(R.string.crime_report_solved);
        else
            solvedString=getString(R.string.crime_report_unsolved);
        String dateformat="EEE, MMM dd";
        String dateString= DateFormat.format(dateformat,mCrime.getmDate()).toString();
        String suspect=mCrime.getmSuspect();
        if (suspect==null)
            suspect=getString(R.string.crime_report_no_suspect);
        else
            suspect=getString(R.string.crime_report_suspect,suspect);
        String report;
        report=getString(R.string.crime_report,mCrime.getmTitle(),dateString,solvedString,suspect);
        return  report;
    }
    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).update(mCrime);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!= Activity.RESULT_OK)
            return;
        if (REQUEST_CODE==requestCode){
            mButton.setText(data.getSerializableExtra(DatePickerFragment.EXTRA_DATE).toString());
            mCrime.setmDate((Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE));
        }
       else if (requestCode==1&&data!=null){
            Uri uri=data.getData();
            String[] queryFields=new String[]{ContactsContract.Contacts.DISPLAY_NAME};
            Cursor c=getActivity().getContentResolver().query(uri,queryFields,null,null,null);
            try {
                if (c.getCount()==0)
                    return;
                c.moveToFirst();
                String suspect=c.getString(0);
                mCrime.setmSuspect(suspect);
                mSuspectButton.setText(suspect);
            }

        finally{
                assert c != null;
                c.close();
            }
        }
        PackageManager packageManager=getActivity().getPackageManager();
        if (packageManager.resolveActivity(data,PackageManager.MATCH_DEFAULT_ONLY)==null)
            mSuspectButton.setEnabled(false);
    }
}
