package com.dev334.blood.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dev334.blood.R;
import com.dev334.blood.databinding.ActivityHomeBinding;
import com.dev334.blood.model.Blood;
import com.dev334.blood.model.User;
import com.dev334.blood.ui.MapActivity;
import com.dev334.blood.ui.login.LoginActivity;
import com.dev334.blood.util.app.AppConfig;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private NotificationFragment notificationFragment;
    private ScheduleFragment scheduleFragment;
    private ProfileFragment profileFragment;
    private RequestFragment requestFragment;
    private SplashFragment splashFragment;
    private NoInternetFragment noInternetFragment;
    private FragmentManager fragmentManager;
    private ActivityHomeBinding binding;
    private AppConfig appConfig;
    private User user;
    private List<Blood> bloods;
    private Blood userBlood;

    private final String TAG="HomeActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavView.setBackground(null);
        binding.bottomNavView.getMenu().getItem(2).setEnabled(false);

        homeFragment = HomeFragment.newInstance();
        notificationFragment=NotificationFragment.newInstance();
        scheduleFragment=ScheduleFragment.newInstance();
        profileFragment=ProfileFragment.newInstance();
        requestFragment=RequestFragment.newInstance();
        splashFragment=new SplashFragment();
        noInternetFragment = NoInternetFragment.newInstance();

        appConfig= new AppConfig(this);
        bloods=new ArrayList<>();

        fragmentManager=getSupportFragmentManager();

        if(savedInstanceState==null){
            binding.bottomNavigationBar.setVisibility(View.GONE);
            binding.floatingActionButton.setVisibility(View.GONE);
            binding.bottomNavView.getMenu().getItem(0).isChecked();
            replaceFragment(splashFragment);
        }


        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRequestFragment();
            }
        });

        binding.bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                binding.bottomNavView.getMenu().setGroupCheckable(0,true,true);
                switch(item.getItemId()){
                    case R.id.nav_donate:
                        replaceFragment(scheduleFragment);
                        break;
                    case R.id.nav_notification:
                        replaceFragment(notificationFragment);
                        break;
                    case R.id.nav_profile:
                        replaceFragment(profileFragment);
                        break;
                    default:
                        replaceFragment(homeFragment);
                        break;
                }
                return true;
            }
        });

    }

    public void openMapActivity() {
        Intent i = new Intent(HomeActivity.this, MapActivity.class);
        startActivity(i);
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
            transaction.add(R.id.homeContainer, fragmentToShow);
        }

        transaction.commit();
    }

    public void openLoginActivity(int i) {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.putExtra("FRAGMENT", i);
        startActivity(intent);
        finish();
    }

    public void openHomeFragment() {
        binding.bottomNavigationBar.setVisibility(View.VISIBLE);
        binding.floatingActionButton.setVisibility(View.VISIBLE);
        replaceFragment(homeFragment);
    }

    public void setUser(User user){
        this.user=user;
    }

    public String getUserLocation(){
        return user.getLocation();
    }

    public String getUserBlood(){
        return user.getBloodGroup();
    }

    public void setBloodRequests(List<Blood> body) {
        bloods=body;
        Log.i(TAG, "setBloodRequests: "+bloods);
    }

    public List<Blood> getBloodRequests(){
        return bloods;
    }

    public String getUserId() {
        return user.getId();
    }

    public void openRequestFragment() {
        binding.bottomNavView.getMenu().setGroupCheckable(0,false, true);
        replaceFragment(requestFragment);
    }

    public void openScheduleFragment() {
        binding.bottomNavView.setSelectedItemId(R.id.nav_donate);
    }

    public void openNotificationFragment() {
        binding.bottomNavView.setSelectedItemId(R.id.nav_notification);
    }

    public void setUserRequest(Blood body) {
        Log.i(TAG, "setUserRequest: "+body);
        userBlood=body;
    }

    public void openNoInternetFragment() {
        replaceFragment(noInternetFragment);
    }
}