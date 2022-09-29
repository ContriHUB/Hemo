package com.dev334.blood.ui.admin;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dev334.blood.R;
import com.dev334.blood.databinding.ActivityAdminBinding;
import com.dev334.blood.model.Schedule;
import com.dev334.blood.util.retrofit.ApiClient;
import com.dev334.blood.util.retrofit.ApiInterface;
import com.dev334.blood.util.retrofit.NoConnectivityException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {

    private String TAG="AdminActivity";
    private ScheduleRequestAdapter pendingAdapter, approvedAdapter;
    private List<Schedule> approvedSchedules,pendingSchedule, Schedules;
    ActivityAdminBinding binding;
    private boolean PENDING=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityAdminBinding.inflate(getLayoutInflater());
        approvedSchedules =new ArrayList<>();
        pendingSchedule = new ArrayList<>();
        reqPendingSchedule();
        reqApprovedSchedule();
        binding.buttonPending.setBackground(getResources().getDrawable(R.drawable.primary_color_filled));

        binding.buttonPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PENDING=true;
                clearButtonColor();
                binding.buttonPending.setBackground(getResources().getDrawable(R.drawable.primary_color_filled));
                binding.ScheduleRecyclerView.setAdapter(pendingAdapter);
                binding.ScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                binding.ScheduleRecyclerView.setHasFixedSize(true);
            }
        });

        binding.buttonApproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PENDING=false;
                clearButtonColor();
                reqApprovedSchedule();
                binding.buttonApproved.setBackground(getResources().getDrawable(R.drawable.primary_color_filled));
                binding.ScheduleRecyclerView.setAdapter(approvedAdapter);
                binding.ScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                binding.ScheduleRecyclerView.setHasFixedSize(true);
            }
        });

        setContentView(binding.getRoot());
    }

    private void clearButtonColor() {
        binding.buttonApproved.setBackground(getResources().getDrawable(R.drawable.primary_outline_textbox));
        binding.buttonPending.setBackground(getResources().getDrawable(R.drawable.primary_outline_textbox));
    }

    private void reqApprovedSchedule() {
        Call<List<Schedule>> call= ApiClient.getApiClient(getApplicationContext()).create(ApiInterface.class).getSchedule("719","0");
        call.enqueue(new Callback<List<Schedule>>() {
            @Override
            public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response) {
                if(!response.isSuccessful()){
                    Log.i(TAG, "onResponse: "+response.code());
                    Log.i(TAG, "onResponse: "+response.toString());
                    Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(response.code() == 200){
                    Log.i(TAG, "onResponse: Successful");
                    approvedSchedules=response.body();
                    approvedAdapter=new ScheduleRequestAdapter(approvedSchedules, PENDING, getApplicationContext());
                    Log.i(TAG, "Array of Schedules: "+approvedSchedules);

                }
            }

            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.getMessage());
                if(t instanceof NoConnectivityException){
                    showNoInternetDialog();


                    return;
                }
            }
        });
    }

    private void showNoInternetDialog() {
        final Dialog dialog=new Dialog(getApplicationContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_no_internet);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        Button goToHome=dialog.findViewById(R.id.go_to_home4);
        goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }

    private void reqPendingSchedule() {

        Call<List<Schedule>> call= ApiClient.getApiClient(getApplicationContext()).create(ApiInterface.class).getSchedule("719","1");
        call.enqueue(new Callback<List<Schedule>>() {
            @Override
            public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response) {
                if(!response.isSuccessful()){
                    Log.i(TAG, "onResponse: "+response.code());
                    Log.i(TAG, "onResponse: "+response.toString());
                    Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(response.code() == 200){
                    Log.i(TAG, "onResponse: Successful");
                    pendingSchedule=response.body();
                    Log.i(TAG, "Array of Schedules: "+pendingSchedule);
                    pendingAdapter= new ScheduleRequestAdapter(pendingSchedule,PENDING,getApplicationContext());
                    binding.ScheduleRecyclerView.setAdapter(pendingAdapter);
                    binding.ScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    binding.ScheduleRecyclerView.setHasFixedSize(true);
                }
            }

            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.getMessage());
                if(t instanceof NoConnectivityException){
                    showNoInternetDialog();

                    return;
                }
            }
        });
    }
}