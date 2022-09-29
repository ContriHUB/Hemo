package com.dev334.blood.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.blood.R;
import com.dev334.blood.model.Blood;

import java.util.List;

public class BloodRequestAdapter extends RecyclerView.Adapter<BloodRequestAdapter.mViewHolder>{

    private List<Blood> bloods;
    private ClickInterface clickInterface;
    private Context context;
    private String TAG="BloodRequestAdapter";
    private String name,bloodgrp,phone;
    private int bloodquantity;
    public BloodRequestAdapter(List<Blood> bloods, ClickInterface clickInterface,Context context){
        this.bloods=bloods;
        this.clickInterface= clickInterface;
        this.context=context;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_request_card, parent, false);
        return new BloodRequestAdapter.mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        holder.setItems(bloods.get(position).getName(),bloods.get(position).getQuantity(),bloods.get(position).getBlood(),bloods.get(position).getPhone());
    }

    public interface ClickInterface {
        void recyclerviewOnClick(int position);
    }


    @Override
    public int getItemCount() {
        return bloods.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder{

        TextView nameTxtView,bloodUnitTxtView,bloodGrpTxtView;
        ImageView locationTxtView,shareImgView;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {

                clickInterface.recyclerviewOnClick(getAdapterPosition());

            });
            nameTxtView=itemView.findViewById(R.id.request_card_name);
            bloodGrpTxtView=itemView.findViewById(R.id.request_card_blood_group);
            bloodUnitTxtView=itemView.findViewById(R.id.request_card_blood_quantity);
            locationTxtView=itemView.findViewById(R.id.request_card_location);
            shareImgView=itemView.findViewById(R.id.shareRequest);

            locationTxtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Blood currentRequest=bloods.get(getAdapterPosition());

                    Double lat=currentRequest.getLatitude();
                    Double lng=currentRequest.getLongitude();

                    String strUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng+ " (" + "Label which you want" + ")";
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                    context.startActivity(intent);




                }
            });


            shareImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Blood Request");
                    i.putExtra(Intent.EXTRA_TEXT, "Hey I found out "+name+" is requesting "+bloodquantity+"units of "+bloodgrp+" blood on Hemo app \n Contact:- "+phone+" if you have any leads \nVisit hemo app for more info.");

                    context.startActivity(Intent.createChooser(i, "Share app"));


                }
            });




        }

        public void setItems(String user, Integer quantity, String blood,String phn) {
            nameTxtView.setText(user);
            bloodGrpTxtView.setText(blood);
            bloodUnitTxtView.setText(quantity.toString());
            name=user;
            bloodgrp=blood;
            bloodquantity=quantity;
            phone=phn;
        }
    }
}
