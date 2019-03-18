package com.example.mvvmmeeting.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvvmmeeting.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_main_favorites extends Fragment {


    public fragment_main_favorites() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_main_favorites, container, false);
    }

}
