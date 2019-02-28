package com.softmastersgroup.umo.umoagent;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.Size;
import com.softmastersgroup.umo.umoagent.utils.AndroidUtils;
import com.softmastersgroup.umo.umoagent.databinding.ActivityCameraBinding;

import java.util.List;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityCameraBinding cameraActivityBinding;
    private boolean mCapturingPicture;
    int session;
    // To show stuff in the callback
    private Size mCaptureNativeSize;
    private long mCaptureTime;
    final int PREVIEW_REQUEST_CODE = 1;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        cameraActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_camera);

        cameraActivityBinding.capturePhoto.setOnClickListener(this);

        session = getIntent().getIntExtra("session",1);
        id = getIntent().getLongExtra("id",1);

        cameraActivityBinding.camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(final byte[] picture) {

                onPicture(picture);

            }
        });

        cameraActivityBinding.camera.capturePicture();

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.capturePhoto) {
             capturePhoto();
        }
    }

    private void capturePhoto() {

        if (mCapturingPicture) return;
        mCapturingPicture = true;
        mCaptureTime = System.currentTimeMillis();
        mCaptureNativeSize = cameraActivityBinding.camera.getPictureSize();
        cameraActivityBinding.camera.capturePicture();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestCameraPermission();

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        cameraActivityBinding.camera.stop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        cameraActivityBinding.camera.destroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return false;
    }

    private void onPicture(byte[] jpeg) {
        //mCapturingPicture = false;
        //long callbackTime = System.currentTimeMillis();

        //if (mCaptureTime == 0) mCaptureTime = callbackTime - 300;
        //if (mCaptureNativeSize == null) mCaptureNativeSize = cameraActivityBinding.camera.getPictureSize();

        PreviewActivity.setImage(jpeg);

        Intent intent = new Intent(CameraActivity.this, PreviewActivity.class);
        intent.putExtra("type",1);
        intent.putExtra("session",session);
        intent.putExtra("id",id);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        finish();

        //mCaptureTime = 0;
        //mCaptureNativeSize = null;
    }



    String photo="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case PREVIEW_REQUEST_CODE:
                    Intent intent = new Intent();
                    photo = data.getStringExtra("photopath");
                    intent.putExtra("photopath",photo);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;

            }
        }
    }

    private void requestCameraPermission() {
        Dexter.withActivity(CameraActivity.this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            openCamera();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            AndroidUtils.showSettingsDialog(CameraActivity.this);
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                    }
                })
                .onSameThread()
                .check();
    }

    private void openCamera() {
        cameraActivityBinding.camera.start();
    }
}

