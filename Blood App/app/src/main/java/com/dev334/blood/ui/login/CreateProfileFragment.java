package com.dev334.blood.ui.login;

import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dev334.blood.R;
import com.dev334.blood.model.ApiResponse;
import com.dev334.blood.model.User;
import com.dev334.blood.util.retrofit.ApiClient;
import com.dev334.blood.util.retrofit.ApiInterface;
import com.dev334.blood.util.retrofit.NoConnectivityException;

import java.util.Calendar;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProfileFragment extends Fragment {

    private View view;
    private String selectedState,selectedDistrict,selectedBloodGroup;
    private Spinner stateSpinner,districtSpinner,bloodGroupSpinner;
    private ArrayAdapter<CharSequence> stateAdapter,districtAdapter,bloodGroupAdapter;
    private EditText weight,gender,dob,phone;
    private String weightString,genderString,dobString, phoneString;
    private Button nextButton;
    DatePickerDialog.OnDateSetListener setListener;
    private String TAG="CreateProfile";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_profile, container, false);

        stateSpinner=view.findViewById(R.id.spinner_indian_states);

        stateAdapter=ArrayAdapter.createFromResource(getContext(),R.array.array_indian_states,R.layout.spinner_layout);

         stateAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

         stateSpinner.setAdapter(stateAdapter);

         bloodGroupSpinner=view.findViewById(R.id.EditBloodGroup);
         bloodGroupAdapter=ArrayAdapter.createFromResource(getContext(),R.array.array_blood_group,R.layout.spinner_layout);
         bloodGroupAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
         bloodGroupSpinner.setAdapter(bloodGroupAdapter);

         gender=view.findViewById(R.id.EditGender);
         weight=view.findViewById(R.id.EditWeight);
         nextButton = view.findViewById(R.id.btnCreate);
         phone=view.findViewById(R.id.EditContactRecord);

         stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View v, int i, long l) {

                 districtSpinner=view.findViewById(R.id.spinner_indian_district);

                 selectedState=stateSpinner.getSelectedItem().toString();

                 int parentID=parent.getId();
                 if(parentID==R.id.spinner_indian_states)
                 {
                     switch (selectedState){
                         case "Select Your State": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_default_districts, R.layout.spinner_layout);
                             break;
                         case "Andhra Pradesh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_andhra_pradesh_districts, R.layout.spinner_layout);
                             break;
                         case "Arunachal Pradesh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_arunachal_pradesh_districts, R.layout.spinner_layout);
                             break;
                         case "Assam": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_assam_districts, R.layout.spinner_layout);
                             break;
                         case "Bihar": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_bihar_districts, R.layout.spinner_layout);
                             break;
                         case "Chhattisgarh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_chhattisgarh_districts, R.layout.spinner_layout);
                             break;
                         case "Goa": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_goa_districts, R.layout.spinner_layout);
                             break;
                         case "Gujarat": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_gujarat_districts, R.layout.spinner_layout);
                             break;
                         case "Haryana": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_haryana_districts, R.layout.spinner_layout);
                             break;
                         case "Himachal Pradesh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_himachal_pradesh_districts, R.layout.spinner_layout);
                             break;
                         case "Jharkhand": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_jharkhand_districts, R.layout.spinner_layout);
                             break;
                         case "Karnataka": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_karnataka_districts, R.layout.spinner_layout);
                             break;
                         case "Kerala": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_kerala_districts, R.layout.spinner_layout);
                             break;
                         case "Madhya Pradesh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_madhya_pradesh_districts, R.layout.spinner_layout);
                             break;
                         case "Maharashtra": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_maharashtra_districts, R.layout.spinner_layout);
                             break;
                         case "Manipur": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_manipur_districts, R.layout.spinner_layout);
                             break;
                         case "Meghalaya": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_meghalaya_districts, R.layout.spinner_layout);
                             break;
                         case "Mizoram": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_mizoram_districts, R.layout.spinner_layout);
                             break;
                         case "Nagaland": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_nagaland_districts, R.layout.spinner_layout);
                             break;
                         case "Odisha": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_odisha_districts, R.layout.spinner_layout);
                             break;
                         case "Punjab": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_punjab_districts, R.layout.spinner_layout);
                             break;
                         case "Rajasthan": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_rajasthan_districts, R.layout.spinner_layout);
                             break;
                         case "Sikkim": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_sikkim_districts, R.layout.spinner_layout);
                             break;
                         case "Tamil Nadu": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_tamil_nadu_districts, R.layout.spinner_layout);
                             break;
                         case "Telangana": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_telangana_districts, R.layout.spinner_layout);
                             break;
                         case "Tripura": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_tripura_districts, R.layout.spinner_layout);
                             break;
                         case "Uttar Pradesh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_uttar_pradesh_districts, R.layout.spinner_layout);
                             break;
                         case "Uttarakhand": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_uttarakhand_districts, R.layout.spinner_layout);
                             break;
                         case "West Bengal": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_west_bengal_districts, R.layout.spinner_layout);
                             break;
                         case "Andaman and Nicobar Islands": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_andaman_nicobar_districts, R.layout.spinner_layout);
                             break;
                         case "Chandigarh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_chandigarh_districts, R.layout.spinner_layout);
                             break;
                         case "Dadra and Nagar Haveli": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_dadra_nagar_haveli_districts, R.layout.spinner_layout);
                             break;
                         case "Daman and Diu": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_daman_diu_districts, R.layout.spinner_layout);
                             break;
                         case "Delhi": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_delhi_districts, R.layout.spinner_layout);
                             break;
                         case "Jammu and Kashmir": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_jammu_kashmir_districts, R.layout.spinner_layout);
                             break;
                         case "Lakshadweep": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_lakshadweep_districts, R.layout.spinner_layout);
                             break;
                         case "Ladakh": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_ladakh_districts, R.layout.spinner_layout);
                             break;
                         case "Puducherry": districtAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_puducherry_districts, R.layout.spinner_layout);
                             break;
                         default:  break;
                     }
                     districtAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                     districtSpinner.setAdapter(districtAdapter);
                     districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                         @Override
                         public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                             selectedDistrict=districtSpinner.getSelectedItem().toString();
                         }

                         @Override
                         public void onNothingSelected(AdapterView<?> adapterView) {

                         }
                     });

                 }

             }

             @Override
             public void onNothingSelected(AdapterView<?> adapterView) {

             }
         });


         bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 selectedBloodGroup=bloodGroupSpinner.getSelectedItem().toString();
             }

             @Override
             public void onNothingSelected(AdapterView<?> adapterView) {

             }
         });




        dob=view.findViewById(R.id.EditDOB);
        Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog datePicker = new DatePickerDialog(getContext(), datePickerListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle("Select the date");
                datePicker.show();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderString=gender.getText().toString();
                weightString=weight.getText().toString();
                phoneString=phone.getText().toString();
                if(check()){
                    createUser();
                }
            }


        });


        return view;
    }

    private void createUser() {
        User user = new User(((LoginActivity)getActivity()).getUserEmail(),Integer.parseInt(weightString),genderString,dobString,selectedBloodGroup,selectedDistrict,phoneString);
        Call<ApiResponse> call = ApiClient.getApiClient(getContext()).create(ApiInterface.class).createUser(user);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(!response.isSuccessful()){
                    Log.i(TAG, "onResponse: "+response.code());
                    Log.i(TAG, "onResponse: "+response.toString());
                    Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "onResponse: "+response.body());
                if(response.body().getStatus()==200){
                    Log.i(TAG, "onResponse: Successful");
                    ((LoginActivity)getActivity()).openHomeActivity();
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

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            selectedMonth=selectedMonth+1;
            String date=selectedYear+"-"+selectedMonth+"-"+selectedDay;

            dob.setText(date);
            dobString=date;
        }
    };



    private boolean check() {
        if(dobString.isEmpty()){
            dob.setError("Enter your DOB");
            return false;
        }
        if(weightString.isEmpty()){
            dob.setError("Enter your weight");
            return false;
        }
        if(genderString.isEmpty()){
            dob.setError("Enter your Gender(M/F)");
            return false;
        }
        if(selectedDistrict.equals("Select Your District")){
            Toast.makeText(getContext(),"Enter your location",Toast.LENGTH_LONG).show();
            return false;
        }
        if(selectedBloodGroup.equals("Select Your Blood Group")){
            Toast.makeText(getContext(),"Select your blood group",Toast.LENGTH_LONG).show();
            return false;
        }

        if(phoneString.isEmpty()){
            phone.setError("Enter valid contact no.");
            return false;
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    
}