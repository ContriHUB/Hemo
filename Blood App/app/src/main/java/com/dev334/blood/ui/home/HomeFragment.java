package com.dev334.blood.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev334.blood.R;
import com.dev334.blood.databinding.FragmentHomeBinding;
import com.dev334.blood.ui.bank.BloodBankActivity;
import com.dev334.blood.util.app.AppConfig;

public class HomeFragment extends Fragment {

    public static HomeFragment fragment=null;
    private View view;
    FragmentHomeBinding binding;
    private AppConfig appConfig;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        if(fragment==null){
            fragment = new HomeFragment();
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
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        appConfig=new AppConfig(getContext());

        binding.textView24.setText(appConfig.getUsername());

        binding.HomemakeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).openRequestFragment();
            }
        });

        binding.NewAppontment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).openScheduleFragment();
            }
        });

        binding.BloodBankFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), BloodBankActivity.class);
                startActivity(i);
            }
        });

        binding.donateBlood.setOnClickListener(v->{
            ((HomeActivity)getActivity()).openNotificationFragment();
        });

        return binding.getRoot();
    }
}