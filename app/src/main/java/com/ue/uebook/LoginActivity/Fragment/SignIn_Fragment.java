package com.ue.uebook.LoginActivity.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.Data.NetworkAPI;
import com.ue.uebook.Data.NetworkService;
import com.ue.uebook.HomeActivity.HomeScreen;
import com.ue.uebook.LoginActivity.Pojo.LoginResponse;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignIn_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignIn_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignIn_Fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button login_btn;
    private TextView create_AccountBtn;
    private NetworkAPI networkAPI;
    private OnFragmentInteractionListener mListener;
    private EditText userName, userPassword;
    private CheckBox keepMeSign;
    private LinearLayout fragment;
    private ProgressDialog dialog;
    private static final int UNAUTHORIZED = 401;

    public SignIn_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignIn_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignIn_Fragment newInstance(String param1, String param2) {
        SignIn_Fragment fragment = new SignIn_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkAPI = NetworkService.getAPI().create(NetworkAPI.class);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in_, container, false);

        login_btn = view.findViewById(R.id.login_btn);
        dialog = new ProgressDialog(getContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        create_AccountBtn = view.findViewById(R.id.create_AccountBtn);
        userName = view.findViewById(R.id.user_login);
        userPassword = view.findViewById(R.id.password_login);
        keepMeSign = view.findViewById(R.id.keepMESIgned_Btn);
        fragment = view.findViewById(R.id.fragment);
        create_AccountBtn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        return view;
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
    public void onClick(View view) {
        if (view == login_btn) {

            if (isvalidate()) {
                String user = userName.getText().toString().trim();
                String userpass = userPassword.getText().toString().trim();
                requestforLogin(user, userpass);
            }
//            else {
//                userPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//            }


        } else if (view == create_AccountBtn) {

            loadFragment(new SignUp_Fragment());

        }
    }

    public void gotoHome() {
        Intent intent = new Intent(getActivity(), HomeScreen.class);
        intent.putExtra("login",1);
        getActivity().startActivity(intent);
        getActivity().finish();
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

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_Fragment, fragment);
        transaction.commit();
    }

//    public void userLogin(final String username, final String passwordu) {
//
//        RequestBody lRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), pFile);
//        MultipartBody.Part user_name = MultipartBody.Part.createFormData("user_name", "vansh");
//        MultipartBody.Part password = MultipartBody.Part.createFormData("password", "vans@123");
//        Call<LoginResponse> loginResponseCall = networkAPI.userLogin(user_name,password);
//        loginResponseCall.enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
//                LoginResponse loginResponse = response.body();
//
//                Log.w("Full",new Gson().toJson(response));
//
//
//                if (loginResponse != null) {
//
//                } else {
//
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
//                Log.d("error", "error");
//            }
//        });
//    }


    private Boolean isvalidate() {
        String user_NAme = userName.getText().toString();
        String userpass = userPassword.getText().toString();
        if (!user_NAme.isEmpty()) {
            if (!userpass.isEmpty()) {
                return true;
            } else {
                userPassword.setError("Enter your Password");
                userPassword.requestFocus();
                userPassword.setEnabled(true);
                return false;
            }

        } else {
            userName.setError("Enter your Username");
            userName.requestFocus();
            userName.setEnabled(true);

            return false;
        }
    }


    public void requestforLogin(final String user_name, final String password) {
        String url = null;
        dialog.setMessage("please wait");

        dialog.show();
        url = "http://dnddemo.com/ebooks/api/v1/userLogin";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_name", user_name)
                .addFormDataPart("password", password)
                .addFormDataPart("device_type","android")
                .addFormDataPart("device_token", FirebaseInstanceId.getInstance().getToken())
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                final String myResponse = e.getLocalizedMessage();
                dialog.dismiss();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final LoginResponse form = gson.fromJson(myResponse, LoginResponse.class);
                if (form.getError() == false) {
                    new SessionManager(getApplicationContext()).storeUserEmail(form.getResponse().getEmail());

                    new SessionManager(getContext().getApplicationContext()).storeUserPublishtype(form.getResponse().getPublisher_type());
                    new SessionManager(getContext().getApplicationContext()).storeUseruserID(form.getResponse().getId());
                    new SessionManager(getContext().getApplicationContext()).storeUserName(form.getResponse().getUser_name());
                    getActivity().runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
//                            Toast.makeText(getContext(), "Succesfully Login", Toast.LENGTH_SHORT).show();
                            if (keepMeSign.isChecked()) {
                                new SessionManager(getContext().getApplicationContext()).storeUserLoginStatus(1);

                            } else {
                                new SessionManager(getContext().getApplicationContext()).storeUserLoginStatus(0);

                            }

                            gotoHome();
                        }
                    });




                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            showDialogWithOkButton("Login Error", form.getMessage());
                            final PrettyDialog pDialog=  new PrettyDialog(getActivity());
                            pDialog  .setTitle("Login Error");
                            pDialog.setIcon(R.drawable.cancel);
                            pDialog.setMessage(form.getMessage());
                            pDialog   .addButton(
                                            "OK",					// button text
                                            R.color.pdlg_color_white,		// button text color
                                            R.color.colorPrimary,		// button background color
                                            new PrettyDialogCallback() {		// button OnClick listener
                                                @Override
                                                public void onClick() {
                                                    pDialog.dismiss();
                                                    dialog.dismiss();
                                                }
                                            }
                                    )
                                    .show();

                        }
                    });
                }


            }
        });

    }


    public void showDialogWithOkButton(String title, String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(title);
        //dialog.setCancelable(false);
        dialog.setMessage(msg);
        dialog.setPositiveButton(getString(R.string.ok_txt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateUserChatId(String chatID ) {
        ApiRequest request = new ApiRequest();
        request.requestforPostChatId(new SessionManager(getApplicationContext()).getUserID(),chatID, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String myResponse = response.body().string();


            }
        });
    }


}
