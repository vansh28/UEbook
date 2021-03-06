package com.ue.uebook.HomeActivity.HomeFragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.ImageUtils;
import com.ue.uebook.LoginActivity.Pojo.RegistrationResponse;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class User_Fragment extends Fragment implements View.OnClickListener, UserMainFragment.OnFragmentInteractionListener, UserProfile_Fragment.OnFragmentInteractionListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 99;
    Context context;
    private String mParam1;
    private String mParam2;
    private Button newfrsag;
    private OnFragmentInteractionListener mListener;
    private ImageView profile_image_user;
    private int textSize;

    private String encodedString;
    private String uriData, image;
    private Bitmap bitmap;

    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 111, PICK_IMAGE_GALLERY = 211;
    private TextView address_user, username;
    private ProgressDialog dialog;
    ImageUtils imageUtils;

    public User_Fragment() {
        // Required empty public constructor
    }
    public static User_Fragment newInstance(String param1, String param2) {
        User_Fragment fragment = new User_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     fontsize();
        imageUtils = new ImageUtils(getActivity().getParent());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_, container, false);
        username = view.findViewById(R.id.username);
        dialog = new ProgressDialog(getActivity());
        address_user = view.findViewById(R.id.address_user);
        profile_image_user = view.findViewById(R.id.profile_image_user);
        profile_image_user.setOnClickListener(this);
        username.setText(new SessionManager(getContext().getApplicationContext()).getUserName());
        loadFragment(new UserMainFragment());
        image = new SessionManager(getApplicationContext()).getUserimage();
        return view;
    }
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
                    +  " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if (view == profile_image_user) {
            selectImage();
        }

    }
    public void onStart(){
        super.onStart();
        if (!isStoragePermissionGranted()){
            isStoragePermissionGranted();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {


    }
    private void selectImage() {
        try {
            PackageManager pm = getContext().getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getContext().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery","View Profile Image", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();

                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                        else if (options[item].equals("View Profile Image")) {
                           imagePreview(new SessionManager(getApplicationContext()).getUserimage());
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
    private void imagePreview(String file) {
        final Dialog previewDialog = new Dialog(getContext());
        previewDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        previewDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout
                , null));
        ImageView imageView = previewDialog.findViewById(R.id.image_view);
        GlideUtils.loadImage((AppCompatActivity) getActivity(), ApiRequest.BaseUrl+"upload/" + file, imageView, R.drawable.user_default, R.drawable.user_default);
        Button ok_Btn = previewDialog.findViewById(R.id.buton_ok);
        ok_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewDialog.dismiss();

            }
        });
        previewDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;
        if (requestCode == PICK_IMAGE_CAMERA) {

            try {
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                encodedString = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
                imgPath = destination.getAbsolutePath();
                profile_image_user.setImageBitmap(bitmap);
                UpdateUser(new File(getPath(getImageUri(getContext(),bitmap))));
            } catch (Exception e) {
                e.printStackTrace();

            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            if (data != null) {
                Uri selectedImage = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    profile_image_user.setImageBitmap(bitmap);
                    UpdateUser(new File(getPath(selectedImage)));
                } catch (Exception e) {

                    e.printStackTrace();

                }

            }
            else
                {
                Toast.makeText(getContext(), "Please Try Again ", Toast.LENGTH_SHORT).show();
            }


        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.user_Container, fragment);
        transaction.commit();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                {
                    String address = new SessionManager(getApplicationContext()).getUserLocation();
                    address_user.setText(address);
                }
            }
        }, 2000);

        if (!image.isEmpty()) {
            GlideUtils.loadImage((AppCompatActivity) getActivity(), ApiRequest.BaseUrl+"upload/" + image, profile_image_user, R.drawable.user_default, R.drawable.user_default);
        } else {

            profile_image_user.setImageResource(R.drawable.user_default);
        }
    }
    private void UpdateUser(File imagepath) {
        ApiRequest request = new ApiRequest();
        dialog.setMessage("please wait");
        dialog.show();
        request.requestforUpdateProfilePic(new SessionManager(getContext()).getUserID(), imagepath, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                dialog.dismiss();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                dialog.dismiss();
                String myResponse = response.body().string();
                Log.e("profile",myResponse);
                Gson gson = new GsonBuilder().create();
                final RegistrationResponse form = gson.fromJson(myResponse, RegistrationResponse.class);
                if (form.getError().equalsIgnoreCase("false") && form.getUser_data() != null) {
                    new SessionManager(getContext()).storeUserPublishtype(form.getUser_data().getPublisher_type());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new SessionManager(getApplicationContext()).storeUserImage(form.getUser_data().getUrl());
                        }
                    });

                }
            }
        });
    }
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("", "Permission is granted");
                return true;
            } else {

                Log.v("", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            Log.v("", "Permission is granted");
            return true;
        }
    }
    private void fontsize(){
        switch(new SessionManager(getActivity().getApplicationContext()).getfontSize()) {
            case "smallest":
                textSize = 12;
                break;
            case "small":
                textSize = 14;
                break;
            case "normal":
                textSize = 16;
                break;
            case "large":
                textSize = 18;
                break;
            case "largest":
                textSize = 24;
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();


    }
}

