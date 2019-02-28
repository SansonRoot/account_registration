package com.softmastersgroup.umo.umoagent.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class FaceOverlayView extends View {

    private Bitmap mBitmap,passport;
    private SparseArray<Face> mFaces;
    private int type = 1;
    double scale;
    boolean faceDetected = false;


    public FaceOverlayView(Context context) {
        this(context, null);
    }

    public FaceOverlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FaceOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public boolean setBitmap( Bitmap bitmap ) {
        FaceDetector detector = new FaceDetector.Builder( getContext() )
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();

        if (!detector.isOperational()) {
            //Handle contingency
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            mFaces = detector.detect(frame);
            detector.release();
        }
        invalidate();
        mBitmap = bitmap;

        if(mFaces.size()==1)faceDetected = true;

        return faceDetected;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if ((mBitmap != null) && (mFaces != null)) {
            double scale = drawBitmap(canvas);
            drawFaceBox(canvas, scale);
        }
    }

    public Bitmap getmBitmap() {
        if(type==2)return mBitmap;
        Rect srcBound = new Rect(((int) Math.floor(left)),((int) Math.floor(top)),((int) Math.floor(right)),((int) Math.floor(bottom)));
        RectF destBound = new RectF(0,0,413,531);
        passport = Bitmap.createBitmap( 413,531, Bitmap.Config.RGB_565 );
        Canvas canvas = new Canvas(passport);
        canvas.drawBitmap( mBitmap, srcBound, destBound, paint );
        return passport;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public void setType(int type) {
        this.type = type;
    }

    private double drawBitmap(Canvas canvas ) {
        double viewWidth = canvas.getWidth();
        double viewHeight = canvas.getHeight();
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();
        scale = Math.min( viewWidth / imageWidth, viewHeight / imageHeight );

        mBitmap = Bitmap.createScaledBitmap(mBitmap, (int)(imageWidth*scale), (int)(imageHeight*scale), true);

        Rect destBounds = new Rect( 0, 0, (int) ( imageWidth*scale ), (int) ( imageHeight*scale) );
        if(type==2)
            canvas.drawBitmap( mBitmap, null, destBounds, null );
        return scale;
    }

    float left = 0;
    float top = 0;
    float right = 0;
    float bottom = 0;
    Paint paint = new Paint();

    private void drawFaceBox(Canvas canvas, double scale) {
        //paint should be defined as a member variable rather than
        //being created on each onDraw request, but left here for
        //emphasis.

        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);


        for( int i = 0; i < mFaces.size(); i++ ) {
            Face face = mFaces.valueAt(i);
            float h = face.getHeight()/4; float w = face.getWidth()/4;
            left = (float)  scale*( face.getPosition().x-w);
            top = (float)   scale*( face.getPosition().y);
            right = (float)  scale*( face.getPosition().x + face.getWidth()+w);
            bottom = (float) scale*( face.getPosition().y + face.getHeight()+h);


            Rect srcBound = new Rect(((int) Math.floor(left)),((int) Math.floor(top)),((int) Math.floor(right)),((int) Math.floor(bottom)));
            //413
            RectF destBound = new RectF(0,0,531,531);


            if(type==1)
                canvas.drawBitmap( mBitmap, srcBound, destBound, paint );
            else if(type==2)
                canvas.drawRect( srcBound, paint );
            //canvas.drawRect( destBound, paint );
        }
    }



}