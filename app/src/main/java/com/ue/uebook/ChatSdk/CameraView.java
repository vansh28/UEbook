package com.ue.uebook.ChatSdk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ue.uebook.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraView extends AppCompatActivity implements View.OnClickListener {
    private  static  final  int CAMERA_REQUEST=999;
    private static final int SELECT_IMAGE = 111;
    private FrameLayout camera_preview;
    private Camera mCamera;
    private CameraPreview mPreview;
    CameraManager manager;
    private String cameraId = CAMERA_BACK;
    private ImageButton captureImage,button_gallary ,button_video ;
    private Camera.PictureCallback mPicture;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private boolean isRecording = false;
    private SurfaceView preview;
    private MediaRecorder mediaRecorder;
    private BottomSheetDialog mBottomSheetDialog;
    private Bitmap bitmap;
    public static final String CAMERA_FRONT = "1";
    public static final String CAMERA_BACK = "0";
    private String cameraIdFront;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);
        camera_preview = findViewById(R.id.camera_preview);
        captureImage = findViewById(R.id.button_capture);
        button_gallary = findViewById(R.id.button_gallary);
        button_video = findViewById(R.id.button_video);

        button_gallary.setOnClickListener(this);
        mCamera = getCameraInstance();
        checkCameraHardware(getApplicationContext());
        Camera.Parameters params = mCamera.getParameters();
         manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
             cameraIdFront = manager.getCameraIdList()[1];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(params);
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, 2);
                }
            }
        });

        button_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, 1);
                }

//                if (isRecording) {
//                    // stop recording and release camera
//                    mediaRecorder.stop();  // stop the recording
//                    releaseMediaRecorder(); // release the MediaRecorder object
//                    mCamera.lock();         // take camera access back from MediaRecorder
//                    Toast.makeText(CameraView.this,"Stop",Toast.LENGTH_SHORT).show();
//                    // inform the user that recording has stopped
//                    isRecording = false;
//                } else {
//                    // initialize video camera
//                    if (prepareVideoRecorder()) {
//                        // Camera is available and unlocked, MediaRecorder is prepared,
//                        // now you can start recording
//                        mediaRecorder.start();
//                        // inform the user that recording has started
//                        Toast.makeText(CameraView.this,"Start",Toast.LENGTH_SHORT).show();
//                        isRecording = true;
//                    } else {
//                        // prepare didn't work, release the camera
//                        releaseMediaRecorder();
//                        // inform user
//                    }
//                }
            }
        });
        // Create our Preview view and set it as the content of our activity.
        mPicture = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                if (pictureFile == null){
                    Log.d("dd", "Error creating media file, check storage permissions");
                    return;
                }

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {

                    Uri selectedImage = Uri.fromFile(pictureFile);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        Intent intent = new Intent(CameraView.this,StatusTrimVideo.class);
                        intent.putExtra("file",getPath(selectedImage));
                        intent.putExtra("imagefile",pictureFile);
                        intent.putExtra("type","image");
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void  OpenCamera(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent,CAMERA_REQUEST);
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            mPreview = new CameraPreview(this, mCamera);
            mCamera.setDisplayOrientation(90);
            camera_preview.addView(mPreview);

            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


//
//    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(mediaFile);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            bmp.compress(Bitmap.CompressFormat.PNG, 80, out);


            Log.e("fle",mediaFile.toString());
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
            Log.e("fle",mediaFile.toString());
        } else {
            return null;
        }

        return mediaFile;
    }

    private boolean prepareVideoRecorder(){

        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(90);
        mediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        // Step 4: Set output file
        mediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

        // Step 5: Set the preview output
        mediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        // Step 6: Prepare configured MediaRecorder
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d("eroor", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d("eroor", "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }
    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mCamera.stopPreview();
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
    public void store_image(File file, Bitmap bmp) {
        try {
            FileOutputStream out = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.PNG, 80, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v==button_gallary){
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            final int ACTIVITY_SELECT_IMAGE = 1234;
            startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 1234:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();
                    Intent intent = new Intent(CameraView.this,StatusTrimVideo.class);
                    intent.putExtra("file",getPath(selectedImage));

                    intent.putExtra("type","gallary");
                  startActivity(intent);
                    finish();
                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                    /* Now you have choosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */
                }

            case 1:
                if (requestCode == 1 && resultCode == RESULT_OK) {
                    Uri videoUri = data.getData();
                    Intent intent = new Intent(CameraView.this,StatusTrimVideo.class);
                    intent.putExtra("file",videoUri.toString());
                    intent.putExtra("type","video");
                    startActivity(intent);
                    finish();
                }
            case 2:
                if (requestCode == 2 && resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();



                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    Intent intent = new Intent(CameraView.this, StatusTrimVideo.class);
                    intent.putExtra("file",byteArray);
                    intent.putExtra("type", "image");
                    startActivity(intent);
                    finish();
                }
        }



    };

//    private void showBottomSheet() {
//        final View bottomSheetLayout = getLayoutInflater().inflate(R.layout.bottomlayoutchat, null);
//        mBottomSheetDialog = new BottomSheetDialog(this);
//
//        mBottomSheetDialog.setContentView(bottomSheetLayout);
//        mBottomSheetDialog.show();
//    }
@Override
protected void onResume() {
    super.onResume();
    mCamera = getCameraInstance();
    checkCameraHardware(getApplicationContext());
    Camera.Parameters params = mCamera.getParameters();
    params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
    mCamera.setParameters(params);
}
    public Uri getImageUri(Context inContext, Bitmap inImage) {

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = 0;
        if (cursor != null) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        } else
            return uri.getPath();
    }


}
