package com.example.tanishka.criminalintent;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrimeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrimeListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CrimeAdapter adapter;
    private boolean mSubtitle;
    private static final String SUB_TITLE="sub_title";
    private List<Crime> crimes_list;
    private TextView textView;

    public CrimeListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CrimeListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrimeListFragment newInstance(String param1, String param2) {
        CrimeListFragment fragment = new CrimeListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
    if (savedInstanceState!=null)
        mSubtitle=savedInstanceState.getBoolean(SUB_TITLE);
        else
        mSubtitle=false;
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SUB_TITLE,mSubtitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime=new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent=CrimePagerActivity.newIntent(getActivity(),crime.getmId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitle=!mSubtitle;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void updateSubtitle(){
        CrimeLab crimeLab=CrimeLab.get(getActivity());
        int size=crimeLab.getmCrimes().size();
        AppCompatActivity activity=(AppCompatActivity) getActivity();
        ActionBar actionBar= activity.getSupportActionBar();
        assert actionBar != null;
        if (!mSubtitle)
            actionBar.setSubtitle(null);
        else
        actionBar.setSubtitle(size+" crimes");
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu( menu, inflater) ;
        inflater.inflate(R.menu.fragment_crime_list,menu);
        MenuItem item =menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitle)
            item.setTitle("HIDE SUBTITLE");
        else
            item.setTitle("SHOW SUBTITLE");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_crime_list,container,false);
        recyclerView=(RecyclerView) view.findViewById(R.id.crime_recycler_view);
        textView=(TextView) view.findViewById(R.id.empty_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
       update();
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        update();

    }

    private void update(){

        CrimeLab crimeLab=CrimeLab.get(getActivity());
       crimes_list=crimeLab.getmCrimes();
       if (crimes_list.size()==0)
       {
           recyclerView.setVisibility(View.GONE);
           textView.setVisibility(View.VISIBLE);
       }
        else{
           recyclerView.setVisibility(View.VISIBLE);
           textView.setVisibility(View.GONE);
       }
      if (adapter==null){
        adapter=new CrimeAdapter(crimes_list);
          recyclerView.setAdapter(adapter);}
       else
        {adapter.setCrimes(crimes_list);
            adapter.notifyDataSetChanged();}
        updateSubtitle();
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
      private TextView mTitleTextView;
      private TextView mDateTextView;
      private CheckBox mSolvedCheckBox;
      private Crime mCrime;
        public CrimeHolder(View itemView) {
          super(itemView);
          mTitleTextView=(TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
          mDateTextView=(TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
          mSolvedCheckBox=(CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);

          itemView.setOnClickListener(this);
        }
      public void bind(Crime crime){
          mCrime=crime;
          mTitleTextView.setText(crime.getmTitle());
          mDateTextView.setText(crime.getmDate().toString());
          mSolvedCheckBox.setChecked(crime.isSolved());
      }

        @Override
        public void onClick(View v) {
            Intent intent=CrimePagerActivity.newIntent(getActivity(),mCrime.getmId());
            startActivity(intent);
        }
    }
 public class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
   private List<Crime> CrimeList;
     public CrimeAdapter(List<Crime> CrimeList) {
     this.CrimeList=CrimeList;
     }

     @Override
     public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
         View v=layoutInflater.inflate(R.layout.list_item_crime,parent,false);
         return new CrimeHolder(v);
     }

     public void setCrimes(List<Crime> list){
         CrimeList=list;
     }
     @Override
     public void onBindViewHolder(CrimeHolder holder, int position) {
          Crime crime=CrimeList.get(position);
          holder.bind(crime);
     }

     @Override
     public int getItemCount() {
         return CrimeList.size();
     }
 }
}
