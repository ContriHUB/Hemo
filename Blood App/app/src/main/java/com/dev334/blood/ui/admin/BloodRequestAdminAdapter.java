package com.dev334.blood.ui.admin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.blood.R;
import com.dev334.blood.model.ApiResponse;
import com.dev334.blood.model.Blood;
import com.dev334.blood.util.retrofit.ApiClient;
import com.dev334.blood.util.retrofit.ApiInterface;
import com.dev334.blood.util.retrofit.NoConnectivityException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BloodRequestAdminAdapter extends RecyclerView.Adapter<BloodRequestAdminAdapter.mViewHolder> {
    private List<Blood> bloods;

    private Context context;
    private String TAG="BloodRequestAdminAdapter";
    public BloodRequestAdminAdapter(List<Blood> bloods, Context context) {
        this.bloods = bloods;
        this.context = context;

    }



    @NonNull
    @Override
    public BloodRequestAdminAdapter.mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_blood_admin, parent, false);
        return new BloodRequestAdminAdapter.mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodRequestAdminAdapter.mViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setItems(bloods.get(position).getName(),bloods.get(position).getQuantity(),bloods.get(position).getBlood());

           holder.callImgView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  String phone=bloods.get(position).getPhone();
                  if(phone.equals("")){
                      Toast.makeText(context.getApplicationContext(), "Phone number not provided", Toast.LENGTH_SHORT).show();
                  }else{
                      Intent intent =new Intent(Intent.ACTION_DIAL);
                      intent.setData(Uri.parse("tel:"+phone));
                      context.startActivity(intent);
                  }
              }
          });

           holder.documentImgView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   String filepath=bloods.get(position).getFile();

                   Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(filepath));
                   browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   context.startActivity(browserIntent);

               }
           });

           holder.locationImgView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   double longitude=bloods.get(position).getLongitude();
                   double latitude=bloods.get(position).getLatitude();
                   String strUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude+ " (" + "Hemo" + ")";
                   Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                   intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   context.startActivity(intent);
               }
           });

           holder.verifyRequest.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   markVerify(bloods.get(position).get_id(),"1");
                   int actualPosition=holder.getAdapterPosition();
                   bloods.remove(actualPosition);
                   notifyItemRemoved(actualPosition);
                   notifyItemRangeChanged(actualPosition,bloods.size());
                   Toast.makeText(context, "Request Verified", Toast.LENGTH_SHORT).show();

               }
           });

           holder.declineRequest.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   markVerify(bloods.get(position).get_id(),"0");
                   int actualPosition=holder.getAdapterPosition();
                   bloods.remove(actualPosition);
                   notifyItemRemoved(actualPosition);
                   notifyItemRangeChanged(actualPosition,bloods.size());
                   Toast.makeText(context, "Request Declined", Toast.LENGTH_SHORT).show();
               }
           });








    }



    @Override
    public int getItemCount() {
        return bloods.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxtView,bloodUnitTxtView,bloodGrpTxtView;
        ImageView locationImgView,documentImgView,callImgView;
        Button verifyRequest,declineRequest;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxtView=itemView.findViewById(R.id.request_name);
            bloodGrpTxtView=itemView.findViewById(R.id.blood_group);
            bloodUnitTxtView=itemView.findViewById(R.id.blood_quantity);
            callImgView=itemView.findViewById(R.id.contact_icon);
            locationImgView=itemView.findViewById(R.id.view_location_icon);
            documentImgView=itemView.findViewById(R.id.view_doc_icon);
            verifyRequest=itemView.findViewById(R.id.btn_verify_request);
            declineRequest=itemView.findViewById(R.id.btn_decline_request);


        }

        public void setItems(String user, Integer quantity, String blood) {
            nameTxtView.setText(user);
            bloodGrpTxtView.setText(blood);
            bloodUnitTxtView.setText(quantity.toString()+" units required");
        }
    }

    public void markVerify(String userId,String verified){
        Call<ApiResponse> call= ApiClient.getApiClient(context).create(ApiInterface.class).markRequest(userId,verified);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(!response.isSuccessful()){
                    Log.i(TAG, "onResponse: "+response.code());
                    Log.i(TAG, "onResponse: "+response.toString());
                    Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "onResponse: "+response.body());
                if(response.body().getStatus()==200){
                    Log.i(TAG, "onResponse: Successful");
                    Toast.makeText(context, "Operation Successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.getMessage());
                if(t instanceof NoConnectivityException){
                    showNoInternetDialog();

                    return;
                }
            }
        });
    }

    private void showNoInternetDialog() {
        final Dialog dialog=new Dialog(context.getApplicationContext());
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
