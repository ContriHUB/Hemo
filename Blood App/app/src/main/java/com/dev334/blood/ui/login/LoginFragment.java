package com.dev334.blood.ui.login;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.dev334.blood.R;
import com.dev334.blood.database.TinyDB;
import com.dev334.blood.model.User;
import com.dev334.blood.util.app.AppConfig;
import com.dev334.blood.util.retrofit.ApiClient;
import com.dev334.blood.util.retrofit.ApiInterface;
import com.dev334.blood.util.retrofit.NoConnectivityException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {

    private View view;
    private Button Login;
    private TextView EditEmail, EditPassword;
    private TextView NewUser;
    private String Email,Password;
    private int RC_SIGN_IN=101;
    private ProgressBar loading;
    private ConstraintLayout parentLayout;
    private TinyDB tinyDB;
    private Map<String, Object> map;
    ArrayList<String> interest;
    private static String TAG="LoginFragmentLog";
    private AppConfig appConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        tinyDB=new TinyDB(getContext());
        map=new HashMap<>();

        NewUser=view.findViewById(R.id.LoginTextSignup);
        EditEmail= view.findViewById(R.id.editEmailLogin);
        EditPassword=view.findViewById(R.id.editPasswordLogin);
        loading=view.findViewById(R.id.loginLoading);
        interest=new ArrayList<>();
        appConfig=new AppConfig(getContext());

        loading.setVisibility(View.INVISIBLE);

        Login=view.findViewById(R.id.btnLogin);

        parentLayout=view.findViewById(R.id.LoginFragmentLayout);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                Email=EditEmail.getText().toString();
                Password=EditPassword.getText().toString();
                if(check(Email,Password)) {
                    getNotificationToken();
                }else {
                    loading.setVisibility(View.INVISIBLE);
                }
            }
        });

        NewUser.setOnClickListener(v->{
            ((LoginActivity)getActivity()).openSignup();
        });

        return view;
    }

    public static void setSnackBar(View root, String snackTitle) {
        Snackbar snackbar = Snackbar.make(root, snackTitle, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void SignInUser(String token) {

        User user = new User(Email,Password,token, true);
        Call<User> call = ApiClient.getApiClient(getContext()).create(ApiInterface.class).loginUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){

                    if(response.code()==401){
                        Toast.makeText(getContext(), "Email not verified", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.INVISIBLE);
                        return;
                    }
                    Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onResponse: "+response.code());
                    loading.setVisibility(View.INVISIBLE);
                    return;
                }

                Log.i(TAG, "onResponse: "+response.message());
                Log.i(TAG, "onResponse: "+response.headers().get("auth_token"));
                ((LoginActivity)getActivity()).setUserID(response.body().getId());
                appConfig.setAuthToken(response.headers().get("auth_token"));

                if(response.body().getGender().isEmpty()){
                    appConfig.setLoginStatus(true);
                    ((LoginActivity)getActivity()).openCreateProfile();
                }
                else {
                    appConfig.setLoginStatus(true);
                    ((LoginActivity) getActivity()).openHomeActivity();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.getMessage());
                if(t instanceof NoConnectivityException){
                    showNoInternetDialog();

                    return;
                }
                loading.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void showNoInternetDialog() {
        final Dialog dialog=new Dialog(getContext());
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

    private boolean check(String email, String password) {
        if(email.isEmpty()){
            EditEmail.setError("Email is empty");
            return false;
        }else if(password.isEmpty()){
            EditPassword.setError("Password is empty");
            return  false;
        }else {
            if (password.length() < 6) {
                EditPassword.setError("Password is too short");
                return false;
            } else {
                return true;
            }
        }
    }

    private void getNotificationToken(){
        FirebaseApp.initializeApp(getActivity());
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful())
                {
                    Log.i(TAG, "onComplete: Could not get the token");
                    return;
                }
                if(task.getResult()!=null)
                {
                    String firebaseMessagingToken =task.getResult();
                    SignInUser(firebaseMessagingToken);
                }

            }
        });
    }

}