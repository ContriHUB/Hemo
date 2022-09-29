package com.dev334.blood.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev334.blood.R;

public class NoInternetFragment extends Fragment {

    public NoInternetFragment() {
        // Required empty public constructor
    }

    public static NoInternetFragment fragment;

    public static NoInternetFragment newInstance() {
        if(fragment==null){
            fragment = new NoInternetFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_internet, container, false);
    }
}