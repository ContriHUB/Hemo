package com.dev334.blood.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dev334.blood.R;
import com.dev334.blood.model.Blood;
import com.dev334.blood.model.User;
import com.dev334.blood.util.app.AppConfig;
import com.dev334.blood.util.retrofit.ApiClient;
import com.dev334.blood.util.retrofit.ApiInterface;
import com.dev334.blood.util.retrofit.NoConnectivityException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashFragment extends Fragment {

    private View view;
    private AppConfig appConfig;
    private final String TAG= "SplashFragmentLog";
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_splash, container, false);
        appConfig=new AppConfig(getContext());
        if(!appConfig.isUserLogin()){

            ((HomeActivity)getActivity()).openLoginActivity(0);
        }else if(!appConfig.isProfileCreated()){
            ((HomeActivity)getActivity()).openLoginActivity(1);
        }else{
            user=appConfig.getUserInfo();

            ((HomeActivity)getActivity()).setUser(user);
            Log.i(TAG, "onCreateView: "+appConfig.getUserLocation()+" "+((HomeActivity)getActivity()).getUserBlood());
            Call<List<Blood>> call = ApiClient.getApiClient(getContext()).create(ApiInterface.class)
                    .getBloodReq(appConfig.getUserLocation(), ((HomeActivity)getActivity()).getUserBlood());
            call.enqueue(new Callback<List<Blood>>() {
                @Override
                public void onResponse(Call<List<Blood>> call, Response<List<Blood>> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: "+response.code());
                        Log.i(TAG, "onResponse: "+response.errorBody());
                        return;
                    }

                    if(response.code()==200){
                        Log.i(TAG, "onResponse: "+response.body());
                        ((HomeActivity)getActivity()).setBloodRequests(response.body());
                        ((HomeActivity)getActivity()).openHomeFragment();
                    }

                }

                @Override
                public void onFailure(Call<List<Blood>> call, Throwable t) {
                    Log.i(TAG, "onFailure: "+t.getMessage());

                    if(t instanceof NoConnectivityException){
                        ((HomeActivity)getActivity()).openNoInternetFragment();
                    }

                }
            });
        }

        return view;
    }

}