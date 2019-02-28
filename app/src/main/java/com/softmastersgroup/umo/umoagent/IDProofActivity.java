package com.softmastersgroup.umo.umoagent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.softmastersgroup.umo.umoagent.models.IDCardModel;
import com.softmastersgroup.umo.umoagent.models.ImageModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import nouri.in.goodprefslib.GoodPrefs;

public class IDProofActivity extends AppCompatActivity {

    Button btnNext, btnPrev;
    CardView cvCards, cvPhoto;
    public static ImageView ivTakePhoto, ivIDCard;
    CheckBox cbPhoto, cbIDCard;
    Context ctx;
    ImageModel userPhoto;
    IDCardModel idCard;
    TextInputEditText etID;
    Spinner spType;
    ImageView ivID;
    int SET_PHOTO_REQUEST = 121;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ctx = IDProofActivity.this;

        userPhoto = GoodPrefs.getInstance().getObject("photo", ImageModel.class);
        idCard = GoodPrefs.getInstance().getObject("id_card", IDCardModel.class);

        if (userPhoto == null) {
            userPhoto = new ImageModel();
        }

        if (idCard == null) {
            idCard = new IDCardModel();
        }

        setContentView(R.layout.activity_idproof);

        init();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, AddressActivity.class));
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, DemographicData.class));
                finish();
            }
        });

        cvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        cvCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIDCardDialog();
            }
        });
    }

    private void init() {
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        cvCards = findViewById(R.id.cvCards);
        cvPhoto = findViewById(R.id.cvPhoto);
        ivTakePhoto = findViewById(R.id.ivTakePhoto);
        ivIDCard = findViewById(R.id.ivIDCard);
        cbPhoto = findViewById(R.id.cbPhoto);
        cbIDCard = findViewById(R.id.cbIDCard);

        bindPhoto();

    }

    private void bindPhoto() {


        if (userPhoto != null && userPhoto.isTaken()) {

            Glide.with(this).load(userPhoto.getUrl()).into(ivTakePhoto);

            cbPhoto.setChecked(true);

        } else {
            Glide.with(this).load(R.drawable.ic_photo).into(ivTakePhoto);

            cbPhoto.setChecked(false);
        }

        if (idCard != null && idCard.isTaken()) {

            Glide.with(this).load(idCard.getImage()).into(ivIDCard);

            cbPhoto.setChecked(true);


        } else {
            Glide.with(this).load(R.drawable.ic_id_card).into(ivIDCard);

            cbPhoto.setChecked(false);
        }


    }

    public void openCamera() {

        GoodPrefs.getInstance().saveInt("img_type",1);

        Intent intent;
        if (userPhoto != null && userPhoto.isTaken()) {
            intent = new Intent(ctx, PreviewActivity.class);
            intent.putExtra("type", 2);
            intent.putExtra("session", 1);
        } else {
            intent = new Intent(ctx, CameraActivity.class);
            startActivity(intent);
        }
        intent.putExtra("id", 1);
        startActivity(intent);
        
    }

    public void captureID() {

        GoodPrefs.getInstance().saveInt("img_type",2);

        Intent intent;
        if (idCard != null && idCard.isTaken()) {
            intent = new Intent(ctx, PreviewActivity.class);
            intent.putExtra("type", 2);
            intent.putExtra("session", 1);
        } else {

            intent = new Intent(ctx, CameraActivity.class);
        }
        intent.putExtra("id", 2);
        startActivity(intent);

    }

    private void showIDCardDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(IDProofActivity.this);

        View view = LayoutInflater.from(ctx).inflate(R.layout.id_card_dialog, null);

        builder.setView(view);

        final TextInputEditText etID;
        final Spinner spType;
        ImageView ivID;
        CardView cvTakeCard ;

        etID = view.findViewById(R.id.etIDNumber);
        spType = view.findViewById(R.id.spIDType);
        ivID = view.findViewById(R.id.ivID);
        cvTakeCard = view.findViewById(R.id.cvIDCard);


        String[] types = getResources().getStringArray(R.array.id_types);


        if (idCard !=null){
            etID.setText(idCard.getId_number());
            Glide.with(ctx).load(idCard.getImage()).into(ivID);

            int p = getPos(types,idCard.getId_type());
            if (p > -1){
                spType.setSelection(p);
            }
        }

        cvTakeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etID.getText().toString().isEmpty()){
                    etID.setError("Specified ID Card's Number required");
                    return;
                }

                if(idCard == null){
                    idCard=new IDCardModel();

                }

                idCard.setId_number(etID.getText().toString());
                idCard.setId_type(spType.getSelectedItem().toString());

                GoodPrefs.getInstance().saveObject("id_card",idCard);

                captureID();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();

    }

    private int getPos(String[] arr,String t){

        int pos = -1;

        if (t == null) return pos;

        for (int i = 0; i < arr.length; ++i){
            if (t.equalsIgnoreCase(arr[i])){
                pos=i;
                return pos;
            }
        }

        return pos;

    }

    @Override
    protected void onResume() {
        super.onResume();

        userPhoto = GoodPrefs.getInstance().getObject("photo", ImageModel.class);
        idCard = GoodPrefs.getInstance().getObject("id_card", IDCardModel.class);

        //Log.d("UPDATED_PHOTO",userPhoto.getUrl());

        bindPhoto();

    }


}
