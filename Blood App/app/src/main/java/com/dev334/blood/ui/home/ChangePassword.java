package com.dev334.blood.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dev334.blood.databinding.ActivityChangePasswordBinding;
import com.dev334.blood.model.ApiResponse;
import com.dev334.blood.model.ChangePasswordModel;
import com.dev334.blood.ui.login.LoginActivity;
import com.dev334.blood.util.app.AppConfig;
import com.dev334.blood.util.retrofit.ApiClient;
import com.dev334.blood.util.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {
    private Button changePasswordBtn;
    private EditText passwordEditText, RePasswordEditText;
    ActivityChangePasswordBinding binding;
    private AppConfig appConfig;
    private String TAG="ChangePasswordLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityChangePasswordBinding.inflate(getLayoutInflater());
        appConfig=new AppConfig(this);

        binding.resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Pass= String.valueOf(binding.resetPasswordNew.getText().toString());
                String cPass= String.valueOf(binding.resetPasswordConfirm.getText().toString());

                if(Pass.isEmpty()){
                    binding.resetPasswordNew.setError("Your password can't be empty");
                }
                else if(Pass.equals(cPass)){
                    binding.resetPasswordConfirm.setError("Your new password can't be same as old one");
                }
                else{
                    changePassword(Pass, cPass);
                }
            }
        });
        setContentView(binding.getRoot());
    }

    private void changePassword(String oldPass, String newPass){
        ChangePasswordModel changePassword=new ChangePasswordModel(appConfig.getUserID(), oldPass, newPass);
        Call<ApiResponse> call= ApiClient.getApiClient(getApplicationContext()).create(ApiInterface.class)
                .changePassword(changePassword);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(!response.isSuccessful()){
                    Log.i(TAG, "onResponse: "+response.code());
                    Log.i(TAG, "onResponse: "+response.message());
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "onResponse: "+response.body());
                if(response.body().getStatus()==200){
                    Log.i(TAG, "onResponse: Successful");
                    Toast.makeText(getApplicationContext(), "Password Changed", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.getMessage());
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}