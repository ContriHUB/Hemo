package com.dev334.blood.ui.bank;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev334.blood.R;
import com.dev334.blood.databinding.FragmentBankMapBinding;
import com.dev334.blood.model.BloodBank;
import com.dev334.blood.util.app.AppConfig;
import com.google.android.gms.location.LocationRequest;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.core.Point2D;
import com.here.sdk.gestures.Gestures;
import com.here.sdk.gestures.TapListener;
import com.here.sdk.mapview.MapError;
import com.here.sdk.mapview.MapImage;
import com.here.sdk.mapview.MapImageFactory;
import com.here.sdk.mapview.MapMarker;
import com.here.sdk.mapview.MapScene;
import com.here.sdk.mapview.MapScheme;
import com.here.sdk.mapview.MapView;
import com.here.sdk.mapview.MapViewBase;
import com.here.sdk.mapview.PickMapItemsResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BankMapFragment extends Fragment {

    public static BankMapFragment fragment;
    private FragmentBankMapBinding binding;
    private AppConfig appConfig;
    private final String TAG="BloodBankLog";
    private List<BloodBank> bloodBankList;
    private List<MapMarker> bankMarkers;
    private Double longitude=77.199621, latitude=28.61436;

    public BankMapFragment() {
        // Required empty public constructor
    }
    public static BankMapFragment newInstance() {
        fragment = new BankMapFragment();
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
        binding=FragmentBankMapBinding.inflate(getLayoutInflater());

        binding.mapView2.onCreate(savedInstanceState);

        appConfig=new AppConfig(getContext());
        bloodBankList=new ArrayList<>();

        bloodBankList = ((BloodBankActivity)getActivity()).getBloodBankList();

        binding.mapView2.setOnReadyListener(new MapView.OnReadyListener() {
            @Override
            public void onMapViewReady() {
                Log.i(TAG, "onMapViewReady: Ready to use map");
            }
        });
        loadMapScene();

        binding.mapView2.getGestures().setTapListener(new TapListener() {
            @Override
            public void onTap(@NonNull Point2D touchPoint) {
                pickMapMarker(touchPoint);
            }
        });

        return binding.getRoot();
    }

    private void convertToGeoCoordinates(){
        MapImage mapImage = MapImageFactory.fromResource(getResources(), R.drawable.place_png);
        bankMarkers=new ArrayList<>();
        for(BloodBank bank: bloodBankList){
            longitude=bank.getLongitude();
            latitude=bank.getLatitude();
            GeoCoordinates geo=new GeoCoordinates(bank.getLatitude(), bank.getLongitude());
            bankMarkers.add(new MapMarker(geo, mapImage));
        }

        Log.i(TAG, "convertToGeoCoordinates: "+bankMarkers.size());

        setMarker();
    }

    private void loadMapScene() {
        // Load a scene from the HERE SDK to render the map with a map scheme.
        binding.mapView2.getMapScene().loadScene(MapScheme.NORMAL_NIGHT, new MapScene.LoadSceneCallback() {
            @Override
            public void onLoadScene(@Nullable MapError mapError) {
                if (mapError == null) {
                    convertToGeoCoordinates();
                } else {
                    Log.d(TAG, "Loading map failed: mapError: " + mapError.name());
                }
            }
        });
    }

    private void setMapLocation(){
        double distanceInMeters = 1000 * 10;
        GeoCoordinates geo;
        if(bloodBankList.get(0).getLatitude()==0) {
            geo = new GeoCoordinates(bloodBankList.get(1).getLatitude(), bloodBankList.get(1).getLongitude());
        }else if(bloodBankList.size()>1){
            geo = new GeoCoordinates(bloodBankList.get(0).getLatitude(), bloodBankList.get(0).getLongitude());
        }else{
            Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.mapView2.getCamera().lookAt(geo, distanceInMeters);
    }

    private void setMarker() {
        binding.mapView2.getMapScene().addMapMarkers(bankMarkers);
        setMapLocation();
    }

    private void pickMapMarker(final Point2D touchPoint) {
        float radiusInPixel = 2;
        binding.mapView2.pickMapItems(touchPoint, radiusInPixel, new MapViewBase.PickMapItemsCallback() {
            @Override
            public void onPickMapItems(@Nullable PickMapItemsResult pickMapItemsResult) {
                if (pickMapItemsResult == null) {
                    // An error occurred while performing the pick operation.
                    return;
                }
                List<MapMarker> mapMarkerList = pickMapItemsResult.getMarkers();
                if (mapMarkerList.size() == 0) {
                    return;
                }
                MapMarker topmostMapMarker = mapMarkerList.get(0);

                for(int i=0;i<bankMarkers.size();i++){
                    if(bankMarkers.get(i).equals(topmostMapMarker)) {
                        Log.i(TAG, "onPickMapItems: "+bloodBankList.get(i).getBankName());
                        showDialogBox(i);
                        return;
                    }
                }

            }
        });
    }

    private void showDialogBox(int i){
        AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
        View view=getLayoutInflater().inflate(R.layout.card_blood_bank,null);
        alert.setView(view);
        AlertDialog show=alert.show();

        TextView name,address;
        Button contact,done;
        ImageView close;

        name=view.findViewById(R.id.card_blood_bank_name);
        address=view.findViewById(R.id.card_bank_address);
        contact=view.findViewById(R.id.card_bank_contact);
        close=view.findViewById(R.id.card_bank_close);
        done=view.findViewById(R.id.card_bank_done);

        name.setText(bloodBankList.get(i).getBankName());
        address.setText(bloodBankList.get(i).getAddress());

        close.setOnClickListener(v->{
            show.dismiss();
        });

        done.setOnClickListener(v->{
            ((BloodBankActivity)getActivity()).openHomeActivity(bloodBankList.get(i));
        });

        List<String> phone = Arrays.asList(bloodBankList.get(i).getContact().split("\\s*,\\s*"));

        contact.setOnClickListener(v->{
            if(phone.isEmpty()){
                Toast.makeText(getActivity(), "Phone number not provided", Toast.LENGTH_SHORT).show();
            }else{
                Intent intent =new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phone.get(0)));
                startActivity(intent);
            }
        });

        alert.setCancelable(true);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


}