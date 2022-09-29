package com.dev334.blood.ui.bank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.blood.R;
import com.dev334.blood.model.Blood;
import com.dev334.blood.model.BloodBank;

import java.util.List;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.mViewHolder>{

    private List<BloodBank> bloodBankList;
    private ClickInterface clickInterface;

    public BankAdapter(List<BloodBank> bloodBankList, ClickInterface clickInterface){
        this.bloodBankList=bloodBankList;
        this.clickInterface=clickInterface;
    }

    @NonNull
    @Override
    public BankAdapter.mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_bank_card, parent, false);
        return new BankAdapter.mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BankAdapter.mViewHolder holder, int position) {
        holder.setItems(bloodBankList.get(position).getBankName());
    }

    public interface ClickInterface {
        void recyclerviewOnClick(int position);
    }

    @Override
    public int getItemCount() {
        return bloodBankList.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder{

        TextView bankName;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            bankName=itemView.findViewById(R.id.card_bank_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickInterface.recyclerviewOnClick(getAdapterPosition());
                }
            });
        }

        public void setItems(String name) {
            bankName.setText(name);
        }
    }
}

