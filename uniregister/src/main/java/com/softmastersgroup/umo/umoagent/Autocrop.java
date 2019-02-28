package com.softmastersgroup.umo.umoagent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.softmastersgroup.umo.umoagent.models.ImageModel;
import com.softmastersgroup.umo.umoagent.utils.AndroidUtils;
import com.softmastersgroup.umo.umoagent.utils.FaceOverlayView;

import java.io.File;
import java.io.IOException;

import nouri.in.goodprefslib.GoodPrefs;

public class Autocrop extends AppCompatActivity implements View.OnClickListener {


    private FaceOverlayView mFaceOverlayView;
    ImageButton save;
    int session;
    private long id;
    ImageModel userPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autocrop);

        userPhoto = GoodPrefs.getInstance().getObject("photo",ImageModel.class);

        if (userPhoto == null){
            userPhoto = new ImageModel();
        }

        mFaceOverlayView = findViewById( R.id.face_overlay );

        session = getIntent().getIntExtra("session",1);
        id = getIntent().getLongExtra("id",1);
        save = findViewById(R.id.save);

        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        mFaceOverlayView.setBitmap(bitmap);

        save.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.save) {
            save();
        }
    }

    private void save() {
        File photo =  new File(AndroidUtils.getAlbumStorageDir("Camera"), String.format("Camera_%d.jpg", System.currentTimeMillis()));
        try {
            AndroidUtils.saveBitmapToJPG(mFaceOverlayView.getmBitmap(),photo,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(session == 1) {
            userPhoto.setTaken(true);
            userPhoto.setUrl(photo.getPath());
            userPhoto.setImage_name(photo.getName());
        }else if(session == 2){
            userPhoto.setTaken(true);
            userPhoto.setUrl(photo.getPath());
            userPhoto.setImage_name(photo.getName());
            saveOnline();
        }
        finish();
    }

    private void saveOnline() {
    }

}
