package com.dev334.blood.ui.admin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.blood.R;
import com.dev334.blood.model.ApiResponse;
import com.dev334.blood.model.Schedule;
import com.dev334.blood.util.retrofit.ApiClient;
import com.dev334.blood.util.retrofit.ApiInterface;
import com.dev334.blood.util.retrofit.NoConnectivityException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleRequestAdapter extends RecyclerView.Adapter<ScheduleRequestAdapter.mViewHolder>{

    private List<Schedule> schedules;
    private boolean PENDING;
    private Context context;
    private String TAG="ScheduleRequestAdapter";
    public ScheduleRequestAdapter(List<Schedule> schedules,boolean pending,Context context){
        this.schedules=schedules;
        this.PENDING=pending;
        this.context=context;
    }

    @NonNull
    @Override
    public ScheduleRequestAdapter.mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_card, parent, false);
        return new ScheduleRequestAdapter.mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleRequestAdapter.mViewHolder holder, @SuppressLint("RecyclerView") int pos) {
        int position= holder.getAdapterPosition();
       holder.setItems(schedules.get(position).getName(),schedules.get(position).getBank(),schedules.get(position).getDate(),schedules.get(position).getTime());
        holder.approvedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PENDING){
                    Approval(schedules.get(position).get_id(),"1");
                    int actualPosition=holder.getAdapterPosition();
                    schedules.remove(actualPosition);
                    notifyItemRemoved(actualPosition);
                    notifyItemRangeChanged(actualPosition,schedules.size());
                    Toast.makeText(view.getContext(), "Marked Approved",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(view.getContext(), "Success",Toast.LENGTH_SHORT).show();
                    Success(schedules.get(position).get_id(),"1");
                }

            }
        });

        holder.declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(PENDING){
                    Approval(schedules.get(position).get_id(),"0");
                    int actualPosition=holder.getAdapterPosition();
                    schedules.remove(actualPosition);
                    notifyItemRemoved(actualPosition);
                    notifyItemRangeChanged(actualPosition,schedules.size());
                    Toast.makeText(view.getContext(), "Declined",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(view.getContext(), "Success",Toast.LENGTH_SHORT).show();
                    Success(schedules.get(position).get_id(),"0");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder{

        TextView userNameTxt,bankTxt,dateTxt,timeTxt;
        Button approvedBtn,declineBtn;
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTxt=itemView.findViewById(R.id.textViewUserName);
            bankTxt=itemView.findViewById(R.id.textViewLocation);
            dateTxt=itemView.findViewById(R.id.textViewDate);
            timeTxt=itemView.findViewById(R.id.textViewTiming);
            approvedBtn=itemView.findViewById(R.id.buttonMarkApp);
            declineBtn=itemView.findViewById(R.id.buttonDecline);
            if(!PENDING){
                approvedBtn.setText("Mark Success");
            }

        }

        public void setItems(String user, String bank, String date, String time) {
            userNameTxt.setText(user);
            bankTxt.setText(bank);
            dateTxt.setText(date);
            timeTxt.setText(time);
        }
    }

    public void Approval(String userId,String approved){
        Call<ApiResponse> call= ApiClient.getApiClient(context).create(ApiInterface.class).setApproval(userId,approved);
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
                    Toast.makeText(context, "Request Approved", Toast.LENGTH_SHORT).show();
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

    public void Success(String userId,String approved){
        Call<ApiResponse> call= ApiClient.getApiClient(context).create(ApiInterface.class).setSuccess(userId,approved);
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
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
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
}
