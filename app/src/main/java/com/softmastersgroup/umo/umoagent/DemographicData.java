package com.softmastersgroup.umo.umoagent;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.softmastersgroup.umo.umoagent.models.BioData;
import com.softmastersgroup.umo.umoagent.models.RegisterBundle;
import com.softmastersgroup.umo.umoagent.utils.AndroidUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import nouri.in.goodprefslib.GoodPrefs;

public class DemographicData extends AppCompatActivity {


    TextInputEditText etSurname,etFirstname,etOthernames,
            etPopularName,etAge,etDOB,
            etBirthPlace,etPhone,etEmail;
    Spinner spGender;
    Button btnNext,btnPrev;
    Context ctx;
    Calendar cal;
    AutoCompleteTextView etCountry;
    String[] countries;
    BioData bioData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ctx = DemographicData.this;

        setContentView(R.layout.activity_demographic_data);

        init();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setData();

                startActivity(new Intent(ctx,IDProofActivity.class));
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx,LaunchActivity.class));
                finish();
            }
        });
    }

    private void init(){

        cal = Calendar.getInstance();
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        etFirstname = findViewById(R.id.etFirstname);
        etSurname = findViewById(R.id.etSurname);
        etOthernames = findViewById(R.id.etOtherNames);
        etPopularName = findViewById(R.id.etPopularName);
        etDOB = findViewById(R.id.etBirthDate);
        etAge = findViewById(R.id.etAge);
        spGender = findViewById(R.id.etGender);
        etCountry = findViewById(R.id.etCountry);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etBirthPlace = findViewById(R.id.etBirthPlace);

        etPopularName.setVisibility(View.GONE);
        etAge.setVisibility(View.GONE);
        etBirthPlace.setVisibility(View.GONE);

        etDOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    etDOB.setText("");
                    showDatePicker();
                }
            }
        });

        etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDOB.setText("");
                showDatePicker();
            }
        });

        countries = getResources().getStringArray(R.array.countries);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx,android.R.layout.simple_list_item_1,countries);

        etCountry.setAdapter(adapter);
        etCountry.setThreshold(1);

        RegisterBundle bundle = GoodPrefs.getInstance().getObject("register_bundle",RegisterBundle.class);

        if (bundle != null){

            bioData = bundle.getBiodata();

            if (bioData == null) bioData = new BioData();

            bindData(bioData);

        }
    }

    public void showDatePicker()
    {

        DatePickerDialog datePickerDialog = new DatePickerDialog(ctx, date,
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "dd-MMM-yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

            etDOB.setText(sdf.format(cal.getTime()));

            try {
                etAge.setText("" + AndroidUtils.age(year, monthOfYear, dayOfMonth));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void bindData(BioData bioData){

        if (bioData == null) return;

        etFirstname.setText(bioData.getFirstname());
        etSurname.setText(bioData.getLastname());
        etOthernames.setText(bioData.getOthernames());
        etPopularName.setText(bioData.getPopularname());
        etAge.setText(String.valueOf(bioData.getAge()));
        etCountry.setText(bioData.getNationality());
        etDOB.setText(bioData.getDateofbirth());
        etBirthPlace.setText(bioData.getBirthplace());
        etPhone.setText(bioData.getTelephone());
        etEmail.setText(bioData.getEmail());

        if (bioData.getGender().equalsIgnoreCase("male")){
            spGender.setSelection(1);
        }else if (bioData.getGender().equalsIgnoreCase("female")){
            spGender.setSelection(2);
        }else if (bioData.getGender().equalsIgnoreCase("other")){
            spGender.setSelection(3);
        }else{
            spGender.setSelection(0);
        }

    }

    private void setData(){

        if(bioData == null) bioData = new BioData();

        bioData.setAge(Integer.parseInt(etAge.getText().toString()));
        bioData.setBirthplace(etBirthPlace.getText().toString());
        bioData.setFirstname(etFirstname.getText().toString());
        bioData.setLastname(etSurname.getText().toString());
        bioData.setOthernames(etOthernames.getText().toString());
        bioData.setPopularname(etPopularName.getText().toString());
        bioData.setNationality(etCountry.getText().toString());
        bioData.setDateofbirth(etDOB.getText().toString());
        bioData.setTelephone(etPhone.getText().toString());
        bioData.setEmail(etEmail.getText().toString());
        bioData.setGender(spGender.getSelectedItem().toString());
        bioData.setType("individual");

        RegisterBundle bundle = GoodPrefs.getInstance().getObject("register_bundle",RegisterBundle.class);

        if (bundle == null){
            bundle = new RegisterBundle();
        }

        bundle.setBiodata(bioData);

        GoodPrefs.getInstance().saveObject("register_bundle",bundle);

    }

}
