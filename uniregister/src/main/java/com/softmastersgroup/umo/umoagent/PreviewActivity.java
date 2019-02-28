package com.softmastersgroup.umo.umoagent;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.otaliastudios.cameraview.CameraUtils;
import com.softmastersgroup.umo.umoagent.models.IDCardModel;
import com.softmastersgroup.umo.umoagent.models.ImageModel;
import com.softmastersgroup.umo.umoagent.utils.AndroidUtils;
import com.softmastersgroup.umo.umoagent.utils.FaceOverlayView;
import com.softmastersgroup.umo.umoagent.utils.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import nouri.in.goodprefslib.GoodPrefs;

import static com.softmastersgroup.umo.umoagent.AddressActivity.ivDoc;
import static com.softmastersgroup.umo.umoagent.IDProofActivity.ivIDCard;
import static com.softmastersgroup.umo.umoagent.IDProofActivity.ivTakePhoto;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener {

    /*
    type is passed through the intent to indicate the type of activity;
    Either it is being previewed from an already saved picture or just taken picture */

    ImageView imageView;
    private FaceOverlayView mFaceOverlayView;
    private static WeakReference<byte[]> image;
    ProgressBar pb;
    ImageButton cancel, retake, auotocrop, crop,save;
    Bitmap imgBitmap = null;
    int session;

    final int CROP_IMAGE = 1;

    String pictureurl;
    boolean pictaken = false;
    private long id;

    ImageModel userPhoto;
    IDCardModel idCard;
    IDCardModel proof_of_address;

    File photo;



    public static void setImage(@Nullable byte[] im) {
        image = im != null ? new WeakReference<>(im) : null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        userPhoto = GoodPrefs.getInstance().getObject("photo", ImageModel.class);
        idCard = GoodPrefs.getInstance().getObject("id_card", IDCardModel.class);

        if (userPhoto == null) {
            userPhoto = new ImageModel();
        }

        if (idCard == null){
            idCard = new IDCardModel();
        }

        if (proof_of_address == null){
            proof_of_address = new IDCardModel();
        }

        imageView = findViewById(R.id.imageview);
        mFaceOverlayView = findViewById(R.id.image);

        crop = findViewById(R.id.crop);
        cancel = findViewById(R.id.cancel);
        retake = findViewById(R.id.retake);
        auotocrop = findViewById(R.id.autocrop);
        save = findViewById(R.id.save);

        int type = getIntent().getIntExtra("type", 1);
        session = getIntent().getIntExtra("session", 1);
        id = getIntent().getLongExtra("id", 1);

        //photo
        if (id == 1){
            save.setVisibility(View.GONE);
            crop.setVisibility(View.VISIBLE);

            if (session == 1) {
                pictureurl = userPhoto.getUrl();
                pictaken = userPhoto.isTaken();
            } else if (session == 2) {
                pictureurl = userPhoto.getUrl();
                pictaken = userPhoto.isTaken();
            }

        }else{ //id card
            save.setVisibility(View.VISIBLE);
            crop.setVisibility(View.GONE);

            if (session == 1) {
                pictureurl = idCard.getImage();
                pictaken = idCard.isTaken();
            } else if (session == 2) {
                pictureurl = idCard.getImage();
                pictaken = idCard.isTaken();
            }
        }



        if (type == 1) previewJustSnaped();
        else if (type == 2) previewOldSnaped();

        cancel.setOnClickListener(this);
        retake.setOnClickListener(this);
        crop.setOnClickListener(this);
        auotocrop.setOnClickListener(this);

    }


    public void previewJustSnaped() {
        pb = new ProgressBar(this);
        pb.show();

        byte[] b = null;

        b = image == null ? null : image.get();
        if (b == null) {
            finish();
            return;
        }

        CameraUtils.decodeBitmap(b, 1000, 1000, new CameraUtils.BitmapCallback() {
            @Override
            public void onBitmapReady(Bitmap bitmap) {
                //InputStream stream = getResources().openRawResource( R.raw.v);
                //bitmap = BitmapFactory.decodeStream(stream);
                /*Glide.with(PreviewActivity.this)
                        .load(bitmap)
                        .into(imageView);*/
                mFaceOverlayView.setType(2);
                boolean faceDetected = mFaceOverlayView.setBitmap(bitmap);
                imgBitmap = bitmap;
                crop.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                retake.setVisibility(View.VISIBLE);
                if (faceDetected)
                    auotocrop.setVisibility(View.VISIBLE);
                pb.done();
            }
        });

    }


    public void previewOldSnaped() {
        mFaceOverlayView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);


        if (pictaken) {
            Glide.with(this)
                    .load(pictureurl)
                    .into(imageView);
            //crop.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            retake.setVisibility(View.VISIBLE);
            //xxx.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel){
            cancel();
        }else if(v.getId() == R.id.retake){
            retake();
        }else if(v.getId() == R.id.save){
            try {
                saveID();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(v.getId() == R.id.autocrop){
            autocrop();
        }else if(v.getId() == R.id.crop){
            try {
                crop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void retake() {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra("session", session);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        finish();
    }

    private void autocrop() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Intent intent = new Intent(getApplicationContext(), Autocrop.class);
        intent.putExtra("image", byteArray);
        intent.putExtra("session", session);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        finish();
    }

    private void saveID() throws IOException{
        photo = new File(AndroidUtils.getAlbumStorageDir("Camera"), String.format("Camera_%d.jpg", System.currentTimeMillis()));

        AndroidUtils.saveBitmapToJPG(imgBitmap, photo, this);

        Uri photoURI = FileProvider.getUriForFile(PreviewActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                new File(photo.getPath()));

        File file = new File(String.valueOf(photo));

        if (GoodPrefs.getInstance().getInt("img_type",1) == 1){

            userPhoto.setUrl(file.getPath());
            userPhoto.setTaken(true);
            userPhoto.setImage_name(file.getName());

            GoodPrefs.getInstance().saveObject("photo",userPhoto);

            Glide.with(getApplicationContext()).load(file.getPath()).into(ivTakePhoto);


        }else if (GoodPrefs.getInstance().getInt("img_type",1) == 2){


            idCard.setImage(file.getPath());
            idCard.setTaken(true);
            idCard.setImage_name(file.getName());

            Glide.with(getApplicationContext()).load(file.getPath()).into(ivIDCard);

            GoodPrefs.getInstance().saveObject("id_card",idCard);

            finish();
        }else{

            proof_of_address.setImage(file.getPath());
            proof_of_address.setTaken(true);
            proof_of_address.setImage_name(file.getName());

            Glide.with(getApplicationContext()).load(file.getPath()).into(ivDoc);


            GoodPrefs.getInstance().saveObject("proof_of_address",proof_of_address);

            finish();

        }

    }

    private void crop() throws IOException {

        photo = new File(AndroidUtils.getAlbumStorageDir("Camera"), String.format("Camera_%d.jpg", System.currentTimeMillis()));

        AndroidUtils.saveBitmapToJPG(imgBitmap, photo, this);

        Uri photoURI = FileProvider.getUriForFile(PreviewActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                new File(photo.getPath()));

        File file = new File(String.valueOf(photo));

        if (GoodPrefs.getInstance().getInt("img_type",1) == 1){
            CropImage(photoURI);
        }else if (GoodPrefs.getInstance().getInt("img_type",1) == 2){


            idCard.setImage(file.getPath());
            idCard.setTaken(true);
            idCard.setImage_name(file.getName());

            Glide.with(getApplicationContext()).load(file.getPath()).into(ivIDCard);

            GoodPrefs.getInstance().saveObject("id_card",idCard);

            finish();
        }else{

            proof_of_address.setImage(file.getPath());
            proof_of_address.setTaken(true);
            proof_of_address.setImage_name(file.getName());

            Glide.with(getApplicationContext()).load(file.getPath()).into(ivDoc);


            GoodPrefs.getInstance().saveObject("proof_of_address",proof_of_address);

            finish();

        }


        Intent intent = new Intent();

        setResult(RESULT_OK, intent);
    }

    public void CropImage(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, CROP_IMAGE);
        } catch (ActivityNotFoundException e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CROP_IMAGE:
                    File file = new File(String.valueOf(photo));
                    /*try {
                        AndroidUtils.saveBitmapToJPG(bmap,file,getContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                    userPhoto.setUrl(file.getPath());
                    userPhoto.setTaken(true);
                    userPhoto.setImage_name(file.getName());

                    GoodPrefs.getInstance().saveObject("photo",userPhoto);

                    Glide.with(getApplicationContext()).load(file.getPath()).into(ivTakePhoto);



                    Log.d("CROP_FILE",photo.getPath());

                    break;
            }

            finish();

        }
    }

    private void cancel() {
        finish();
    }
}