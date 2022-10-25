package com.dev334.blood.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dev334.blood.R;
import com.dev334.blood.databinding.FragmentHomeBinding;
import com.dev334.blood.ui.bank.BloodBankActivity;
import com.dev334.blood.util.app.AppConfig;
import com.google.android.material.snackbar.Snackbar;

public class HomeFragment extends Fragment {

    private static final int REQUEST_LOCATION_CODE = 101;
    public static HomeFragment fragment=null;
    private View view;
    FragmentHomeBinding binding;
    private AppConfig appConfig;
    ActivityResultLauncher<String> requestPermissionLauncher;
    ActivityResultLauncher<Intent> getActivityResultLauncher;

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

        getActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()== Activity.RESULT_OK){
                            startActivity(new Intent(getActivity(), BloodBankActivity.class));
                        }
                    }
                });

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if(result){
                            startActivity(new Intent(getActivity(), BloodBankActivity.class));
                        }
                        else{
                            if(!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)){
                                Snackbar s = Snackbar.make(getActivity().findViewById(android.R.id.content), "We need location Permission to proceed. " +
                                        "Please grant the permissions", Snackbar.LENGTH_SHORT)
                                        .setAction("SETTINGS", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                                intent.setData(uri);
                                                getActivityResultLauncher.launch(intent);
                                            }
                                        });

                                s.show();
                            }
                            else {
                                Toast.makeText(getActivity(), "Please grant required location permissions", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

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
                if(!checkPermissions()){
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                }
                else {
                    Intent i = new Intent(getActivity(), BloodBankActivity.class);
                    startActivity(i);
                }
            }
        });

        binding.donateBlood.setOnClickListener(v->{
            ((HomeActivity)getActivity()).openNotificationFragment();
        });

        return binding.getRoot();
    }

    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
}