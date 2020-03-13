package com.ue.uebook.ChatSdk;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.BuildConfig;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ue.uebook.ImageUtils;
import com.ue.uebook.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatusFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatusFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int PICK_IMAGE_CAMERA = 111, PICK_IMAGE_GALLERY = 211;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 99;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton addTextStatus,addImageStatus;

    private OnFragmentInteractionListener mListener;
    private ImageUtils imageUtils;
    private String encodedString;
    private String uriData, image;
    private Bitmap bitmap;

    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private ImageView imageview;
    Uri outPutfileUri;
    public StatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatusFragment newInstance(String param1, String param2) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageUtils = new ImageUtils(getActivity().getParent());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        addImageStatus = view.findViewById(R.id.addImageStatus);
        addTextStatus = view.findViewById(R.id.addTextStatus);
        imageview = view.findViewById(R.id.imageview);
        addImageStatus.setOnClickListener(this);
        addTextStatus.setOnClickListener(this);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v==addImageStatus){
                selectImage();
        }
        else if (v==addTextStatus){
            Intent intent = new Intent(getContext(),TextStatusCreateScreen.class);
            startActivity(intent);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void sendTakePictureIntent() {

//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra( MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
//        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
//
//            File pictureFile = null;
//            try {
//                pictureFile = getPictureFile();
//            } catch (IOException ex) {
//                Toast.makeText(this,
//                        "Photo file can't be created, please try again",
//                        Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (pictureFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.zoftino.android.fileprovider",
//                        pictureFile);
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
//            }
//        }
        Intent intent;
        String BX1 = android.os.Build.MANUFACTURER;
        if (BX1.equalsIgnoreCase("samsung")) {
            Log.e(" device", " isssssssssss samsung" + BX1);
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, PICK_IMAGE_CAMERA);
        } else {
            Log.e(" inside other", " devices isssssssssss");
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = new File(Environment.getExternalStorageDirectory(), String.valueOf(System.currentTimeMillis()) + ".jpg");
            //     outPutfileUri = Uri.fromFile(file);
            outPutfileUri = FileProvider.getUriForFile(getContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
            String uriString = outPutfileUri.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            // this method is used to get pic name
            // getPicName(path, outPutfileUri, myFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
            startActivityForResult(intent, PICK_IMAGE_CAMERA);
        }
    }
    private void selectImage() {
        try {
            PackageManager pm = getContext().getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getContext().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intents, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
//                            Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                            intent.setType("video/*, image/*");
//                            startActivityForResult(intent, PICK_IMAGE_GALLERY);

                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            photoPickerIntent.setType("*/*");
                            startActivityForResult(photoPickerIntent, PICK_IMAGE_GALLERY);


                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }

                    }


                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);


            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private String getEncodedString(Bitmap bitmap){

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG,100, os);

      /* or use below if you want 32 bit images

       bitmap.compress(Bitmap.CompressFormat.PNG, (0–100 compression), os);*/
        byte[] imageArr = os.toByteArray();
        return Base64.encodeToString(imageArr, Base64.URL_SAFE);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;
        if (resultCode == RESULT_OK) {

            if (requestCode==PICK_IMAGE_CAMERA){
                Uri filePath = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");


                Intent intent = new Intent(getContext(),StatusTrimVideo.class);
                intent.putExtra("file",getPath(getImageUri(getContext(),bitmap)));
                intent.putExtra("type","image");
                getContext().startActivity(intent);
                // scam_image_img.setImageBitmap(bitmap);

            }



        }if (requestCode == PICK_IMAGE_GALLERY) {
                Uri selectedImage = data.getData();
                if (selectedImage!=null){

                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContext().getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                imageview.setImageBitmap(thumbnail);
                Intent intent = new Intent(getContext(),StatusTrimVideo.class);
                intent.putExtra("file",picturePath);
                intent.putExtra("type","image");
                getContext().startActivity(intent);
                }
            }

        else
            {
                Toast.makeText(getContext(), "Please Try Again ", Toast.LENGTH_SHORT).show();
            }


}
    public Uri getImageUri(Context inContext, Bitmap inImage) {

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
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
