package com.ue.uebook.ChatSdk;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ue.uebook.ChatSdk.Adapter.StatusAdapter;
import com.ue.uebook.ChatSdk.Pojo.Statusmodel;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.ImageUtils;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatusFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatusFragment extends Fragment implements View.OnClickListener ,StatusAdapter.ItemClick{
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
    Uri imageUri;
    private List<Statusmodel>statusmodelList;
    private StatusAdapter statusAdapter;
    private RecyclerView friendStatusList;
    private RelativeLayout rootview;
    private ImageView userProfiles;
    private BottomSheetDialog mBottomSheetDialog;
    private ImageButton camerabtn,videobtn,gallerybtn;
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
        statusmodelList = new ArrayList<>();
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
        friendStatusList = view.findViewById(R.id.friendStatusList);
        image = new SessionManager(getActivity().getApplicationContext()).getUserimage();
        rootview = view.findViewById(R.id.header);
        userProfiles = view.findViewById(R.id.profilePic);
        rootview.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        friendStatusList.setLayoutManager(linearLayoutManager);
        imageview = view.findViewById(R.id.imageview);
        addImageStatus.setOnClickListener(this);
        addTextStatus.setOnClickListener(this);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getAllStatus(new SessionManager(getActivity()).getUserID());

        if (!image.isEmpty()) {
            GlideUtils.loadImage((AppCompatActivity) getActivity(), ApiRequest.BaseUrl+"upload/" + image, userProfiles, R.drawable.user_default, R.drawable.user_default);
        } else {

            userProfiles.setImageResource(R.drawable.user_default);
        }

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
//                Intent intent = new Intent(getContext(),CameraView.class);
//                startActivity(intent);

            showBottomSheet();
        }
        else if (v==addTextStatus){
            Intent intent = new Intent(getContext(),TextStatusCreateScreen.class);
            startActivity(intent);
        }
        else if (v==rootview){
            Intent intent = new Intent(getContext(),StatusViewScreen.class);
            intent.putExtra("ownStatus",1);
            intent.putExtra("friendId","");
            getContext().startActivity(intent);
        }
        else if (v==camerabtn){
            mBottomSheetDialog.dismiss();
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
             imageUri = getContext().getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, 2);

        }
        else if (v==gallerybtn){
            mBottomSheetDialog.dismiss();
//            Intent i = new Intent(Intent.ACTION_PICK,
//                    android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//            final int ACTIVITY_SELECT_IMAGE = 1234;
//            startActivityForResult(i, ACTIVITY_SELECT_IMAGE);

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/* video/*");
            startActivityForResult(pickIntent, 1234);

        }
        else if (v==videobtn){
            mBottomSheetDialog.dismiss();
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (takeVideoIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, 1);
            }
        }
    }

    @Override
    public void ontItemClick(Statusmodel oponentData, int position) {
                    Intent intent = new Intent(getContext(),StatusViewScreen.class);
                    intent.putExtra("friendId",oponentData.getUser_id());
                     intent.putExtra("ownStatus",2);
                    getContext().startActivity(intent);
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
                    if (selectedImage.toString().contains("image")) {
                        //handle image

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


                    } else  if (selectedImage.toString().contains("video")) {

                         Log.e("vid","videp");

                        //handle video
                    }

                }
            }
        if(requestCode == 1234 && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            Intent intent = new Intent(getContext(),StatusTrimVideo.class);
            intent.putExtra("file",getPath(selectedImage));
            intent.putExtra("type","image");
            getContext().startActivity(intent);
        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            Intent intent = new Intent(getContext(),StatusTrimVideo.class);
            intent.putExtra("file",videoUri.toString());
            intent.putExtra("type","video");
            startActivity(intent);
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            try {
                Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                        getActivity().getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(getContext(), StatusTrimVideo.class);
            intent.putExtra("file", getRealPathFromURI(imageUri));
            intent.putExtra("type", "cameraimage");
            startActivity(intent);
        }

}

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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


    public void getAllStatus(final String userID ){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiRequest.testBaseUrl +"userstatus/latestUserStatusList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        if(statusmodelList.size()>0)
                            statusmodelList.clear();
                        Log.e(" response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            if (jsonObject.getBoolean("error")==false){
                                JSONArray jsonObjectResponse = jsonObject.getJSONArray("data");
                                if (jsonObjectResponse!=null){
                                    for (int i=0; i<jsonObjectResponse.length();i++){
                                        Statusmodel statusmodel = new Statusmodel();
                                        JSONObject rec = jsonObjectResponse.getJSONObject(i);
                                        statusmodel.setStatus_id(rec.getString("status_id"));
                                        statusmodel.setMessage(rec.getString("message"));
                                        statusmodel.setMessage_type(rec.getString("message_type"));
                                        statusmodel.setUrl(rec.getString("url"));
                                        statusmodel.setUser_id(rec.getString("user_id"));
                                        statusmodel.setUser_name(rec.getString("user_name"));

                                        statusmodelList.add(statusmodel);
                                    }
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            statusAdapter = new StatusAdapter((AppCompatActivity) getContext(),statusmodelList);
                                            friendStatusList.setAdapter(statusAdapter);
                                            statusAdapter.setItemClickListener(StatusFragment.this);
                                        }
                                    });
                                }
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject data = new JSONObject(responseBody);

                            Toast.makeText(getContext(), data.optString("message","Something wrong!"), Toast.LENGTH_LONG).show();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> arguments = new HashMap<String, String>();
                arguments.put("user_id",userID);
                return arguments;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void showBottomSheet() {
        final View bottomSheetLayout = getLayoutInflater().inflate(R.layout.camerabottomitem, null);
        mBottomSheetDialog = new BottomSheetDialog(getContext());
        mBottomSheetDialog.setContentView(bottomSheetLayout);
        gallerybtn = mBottomSheetDialog.findViewById(R.id.galleryBtn);
        camerabtn = mBottomSheetDialog.findViewById(R.id.cameraBtn);
        videobtn = mBottomSheetDialog.findViewById(R.id.videoBtn);
        gallerybtn.setOnClickListener(this);
        camerabtn.setOnClickListener(this);
        videobtn.setOnClickListener(this);
        mBottomSheetDialog.show();
    }


}
