package com.dev334.blood.ui.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dev334.blood.R;
import com.dev334.blood.databinding.FragmentNotificationBinding;
import com.dev334.blood.model.Blood;
import com.dev334.blood.util.app.DownloadPDF;
import com.dev334.blood.util.app.PdfViewerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment implements BloodRequestAdapter.ClickInterface{

    public static NotificationFragment fragment=null;
    private View view;
    FragmentNotificationBinding binding;
    private BloodRequestAdapter bloodRequestAdapter;
    private List<Blood> bloods;
    private String TAG="NotificationFragLog";

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance() {
        if(fragment==null) {
            fragment = new NotificationFragment();
        }
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
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        bloods=new ArrayList<>();
        bloods=((HomeActivity)getActivity()).getBloodRequests();
        Log.i(TAG, "onCreateView: "+bloods);
        bloodRequestAdapter= new BloodRequestAdapter(bloods,this,getContext());
        binding.recyclerNotification.setAdapter(bloodRequestAdapter);
        binding.recyclerNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerNotification.setHasFixedSize(true);
        return binding.getRoot();
    }

    @Override
    public void recyclerviewOnClick(int position) {

        showDialogBox(position);

    }

    private void showDialogBox(int position) {
        AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
        View view=getLayoutInflater().inflate(R.layout.card_request_contact,null);
        alert.setView(view);
        AlertDialog show=alert.show();


        Button contact,viewpdf;
        ImageView close;
        TextView info;


        contact=view.findViewById(R.id.card_request_contact_btn);
        close=view.findViewById(R.id.card_request_close_btn);
        viewpdf=view.findViewById(R.id.card_request_document_btn);
        info=view.findViewById(R.id.card_request_info);

        if(bloods.get(position).getInfo().isEmpty()){
            info.setText("No additional information provided by user");
        }else{
            info.setText(bloods.get(position).getInfo());
        }

          contact.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  String phone=bloods.get(position).getPhone();
                  if(phone.equals("")){
                      Toast.makeText(getActivity(), "Phone number not provided", Toast.LENGTH_SHORT).show();
                  }else{
                      Intent intent =new Intent(Intent.ACTION_DIAL);
                      intent.setData(Uri.parse("tel:"+phone));
                      startActivity(intent);
                  }
              }
          });

        close.setOnClickListener(v->{
            show.dismiss();
        });

        String filepath=bloods.get(position).getFile();

        viewpdf.setOnClickListener(v->{

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(filepath));
            startActivity(browserIntent);


        });



        alert.setCancelable(true);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private void ViewPDF() {

        File pdfFile=new File(Environment.getExternalStorageDirectory()+"/Hemo Requests/"+"bloodRequest.pdf");
        Log.i(TAG, "ViewPDF: "+pdfFile);


        Intent intent=new Intent(getActivity(), PdfViewerActivity.class);
        intent.putExtra("pdfFile",pdfFile);
        startActivity(intent);




    }

    private void DownloadPDFfromURL(String filepath) {

        new DownloadPDF().execute(filepath,"bloodRequest");

    }
}