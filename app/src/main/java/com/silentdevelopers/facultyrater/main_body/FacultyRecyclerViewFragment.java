package com.silentdevelopers.facultyrater.main_body;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.silentdevelopers.facultyrater.R;
import com.silentdevelopers.facultyrater.StartActivity;

import java.util.ArrayList;

public class FacultyRecyclerViewFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentRVInteractionListener mListener;

    public FacultyRecyclerViewFragment() {

    }

    public static FacultyRecyclerViewFragment newInstance(String param1, String param2) {
        FacultyRecyclerViewFragment fragment = new FacultyRecyclerViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    static MainBodyActivity parentAct;
    public void setParent(MainBodyActivity parent){
        parentAct = parent;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_faculty_recycler_view, container, false);

        ArrayList<DataProviderFaculty> arrayList = new ArrayList<>();


        String name[] = {"A", "B"}, des[]= {"da", "db"}, time[] = {"1", "2"}, exp[] = {"e1", "e2"};
        int img_res[] = {R.drawable.gr_photo_18072014_103842pm, R.drawable.gr_photo_18072014_103842pm};
        int id[] = {1, 2};

        int i=0;
        for(String n: name){
            DataProviderFaculty dataProviderFaculty = new DataProviderFaculty(img_res[i], n, des[i], exp[i], time[i], id[i]);
            arrayList.add(dataProviderFaculty);
            i++;
        }

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_faculty_list);
        adapter = new FacultyRecyclerAdapter(arrayList, parentAct);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(View v) {
        if (mListener != null) {
            mListener.onFragmentInteractionRV(v);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentRVInteractionListener) {
            mListener = (OnFragmentRVInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentRVInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteractionRV(View v);
    }
}