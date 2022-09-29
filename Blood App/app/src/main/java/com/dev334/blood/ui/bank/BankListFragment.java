package com.dev334.blood.ui.bank;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev334.blood.R;
import com.dev334.blood.databinding.FragmentBankListBinding;
import com.dev334.blood.databinding.FragmentBankMapBinding;
import com.dev334.blood.model.BloodBank;
import com.dev334.blood.ui.home.HomeActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BankListFragment extends Fragment implements BankAdapter.ClickInterface {

    public static BankListFragment fragment;
    private FragmentBankListBinding binding;
    private BankAdapter bankAdapter;
    private List<BloodBank> bloodBankList;

    public BankListFragment() {
        // Required empty public constructor
    }

    public static BankListFragment newInstance() {
        fragment = new BankListFragment();
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
        binding = FragmentBankListBinding.inflate(getLayoutInflater());

        bloodBankList=new ArrayList<>();
        bloodBankList=((BloodBankActivity)getActivity()).getBloodBankList();

        bankAdapter=new BankAdapter(bloodBankList, this);
        binding.bankRecyclerView.setAdapter(bankAdapter);
        binding.bankRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.bankRecyclerView.setHasFixedSize(true);

        binding.mapTextviewBtn.setOnClickListener(v->{
            ((BloodBankActivity)getActivity()).openBankMapFragment();
        });

        return binding.getRoot();
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

    @Override
    public void recyclerviewOnClick(int position) {
        showDialogBox(position);
    }
}