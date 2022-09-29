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
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.blood.R;
import com.dev334.blood.model.Blood;
import com.dev334.blood.util.app.AppConfig;
import com.dev334.blood.util.retrofit.ApiClient;
import com.dev334.blood.util.retrofit.ApiInterface;
import com.dev334.blood.util.retrofit.NoConnectivityException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivityRequest extends AppCompatActivity {

       private  String TAG="AdminActivityRequest";
       private BloodRequestAdminAdapter bloodRequestAdminAdapter;
       private List<Blood> pendingRequests;
       private AppConfig appConfig;
       private RecyclerView recyclerViewPendingRequests;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_request);

        pendingRequests=new ArrayList<>();
        recyclerViewPendingRequests=(RecyclerView) findViewById(R.id.blood_request_pending_recycler);
        reqPendingRequests();


    }

    private void reqPendingRequests() {

        appConfig=new AppConfig(this);
        Call<List<Blood>> call= ApiClient.getApiClient(getApplicationContext()).create(ApiInterface.class).getAdminRequests(appConfig.getUserLocation());
        call.enqueue(new Callback<List<Blood>>() {
            @Override
            public void onResponse(Call<List<Blood>> call, Response<List<Blood>> response) {
                if(!response.isSuccessful()){
                    Log.i(TAG, "onResponse: "+response.code());
                    Log.i(TAG, "onResponse: "+response.toString());
                    Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(response.code() == 200){
                    Log.i(TAG, "onResponse: Successful");
                    pendingRequests=response.body();
                    Log.i(TAG, "Array of Pending Requests: "+pendingRequests);
                    bloodRequestAdminAdapter= new BloodRequestAdminAdapter(pendingRequests,getApplicationContext());
                    recyclerViewPendingRequests.setAdapter(bloodRequestAdminAdapter);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerViewPendingRequests.setLayoutManager(mLayoutManager);
                    recyclerViewPendingRequests.setHasFixedSize(true);

                }
            }

            @Override
            public void onFailure(Call<List<Blood>> call, Throwable t) {
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
}