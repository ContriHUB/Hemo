package com.dev334.blood.ui.bank;

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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dev334.blood.R;
import com.dev334.blood.database.TinyDB;
import com.dev334.blood.databinding.ActivityBloodBankBinding;
import com.dev334.blood.model.BloodBank;
import com.dev334.blood.model.GovApiResponse;
import com.dev334.blood.util.app.AppConfig;
import com.dev334.blood.util.retrofit.ApiInterface;
import com.dev334.blood.util.retrofit.GovApiClient;
import com.dev334.blood.util.retrofit.NoConnectivityException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BloodBankActivity extends AppCompatActivity {

    private final String TAG="BloodBankActivityLog";
    private List<BloodBank> bloodBankList;
    private AppConfig appConfig;
    private ActivityBloodBankBinding binding;
    private BankListFragment bankListFragment;
    private BankMapFragment bankMapFragment;
    private FragmentManager fragmentManager;
    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityBloodBankBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        appConfig=new AppConfig(this);

        bloodBankList=new ArrayList<>();

        bankListFragment=BankListFragment.newInstance();
        bankMapFragment=BankMapFragment.newInstance();

        fragmentManager=getSupportFragmentManager();
        tinyDB=new TinyDB(this);

        if(savedInstanceState==null){
            getBloodBanks();
        }else{
            replaceFragment(bankListFragment);
        }

    }

    private void getBloodBanks() {
        Call<GovApiResponse> call= GovApiClient.getApiClient(getApplicationContext()).create(ApiInterface.class).getBloodBank(appConfig.getUserLocation());
        call.enqueue(new Callback<GovApiResponse>() {
            @Override
            public void onResponse(Call<GovApiResponse> call, Response<GovApiResponse> response) {

                if(!response.isSuccessful()){
                    Log.i(TAG, "onResponse: "+response.code());
                    Log.i(TAG, "onResponse: "+response.toString());
                    Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(response.body().getTotal()!=0){
                    Log.i(TAG, "onResponse: "+response.body().getTotal());
                    bloodBankList=response.body().getResponse();
                    replaceFragment(bankListFragment);
                    binding.bankLoading.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(Call<GovApiResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.getMessage());
                if(t instanceof NoConnectivityException){
                    showNoInternetDialog();

                    return;
                }
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
        });
    }

    private void replaceFragment(Fragment fragmentToShow) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        // Hide all of the fragments
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            transaction.hide(fragment);
        }

        if (fragmentToShow.isAdded()) {
            // When fragment was previously added - show it
            transaction.show(fragmentToShow);
        } else {
            // When fragment is adding first time - add it
            transaction.add(R.id.bankContainer, fragmentToShow);
        }

        transaction.commit();
    }

    public List<BloodBank> getBloodBankList(){
        return bloodBankList;
    }

    public void openBankMapFragment() {
        replaceFragment(bankMapFragment);
    }

    public void openBankListFragment() {
        replaceFragment(bankListFragment);
    }

    public void openHomeActivity(BloodBank bloodBank) {
        tinyDB.putObject("BloodBank", bloodBank);
        finish();
    }
}