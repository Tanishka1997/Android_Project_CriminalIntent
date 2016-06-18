package com.example.tanishka.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    public static final String Id="Id_crime";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        mViewPager=(ViewPager) findViewById(R.id.activity_crime_pager_view_pager);
        mCrimes=CrimeLab.get(this).getmCrimes();
        UUID crime_id= (UUID) getIntent().getSerializableExtra(Id);
        FragmentManager manager=getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(manager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime=mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getmId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        for(int i=0;i<mCrimes.size();i++){
           if (mCrimes.get(i).getmId().equals(crime_id)){
               mViewPager.setCurrentItem(i);
               break;
           }
        }
    }
    public static Intent newIntent(Context context, UUID uuid){
        Intent intent=new Intent(context,CrimePagerActivity.class);
        intent.putExtra(Id,uuid);
        return intent;
    }
}
