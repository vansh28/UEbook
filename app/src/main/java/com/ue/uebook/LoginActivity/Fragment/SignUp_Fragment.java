package com.ue.uebook.LoginActivity.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.Data.NetworkAPI;
import com.ue.uebook.Data.NetworkService;
import com.ue.uebook.HomeActivity.HomeScreen;
import com.ue.uebook.LoginActivity.Pojo.RegistrationBody;
import com.ue.uebook.LoginActivity.Pojo.RegistrationResponse;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUp_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUp_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUp_Fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private NetworkAPI networkAPI;
    private EditText username, userEmail, userPassword;
    private RadioButton male, female;
    private CheckBox reader, writer, publisher;
    private RadioGroup radioGroup;
    private Button create_user;
    private String gender;
    private String checkboxlist;
    private OnFragmentInteractionListener mListener;
    private Spinner actorType;
    private ProgressDialog dialog;
    private LinearLayout viewLinear;

    public SignUp_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUp_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUp_Fragment newInstance(String param1, String param2) {
        SignUp_Fragment fragment = new SignUp_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_, container, false);
        networkAPI = NetworkService.getAPI().create(NetworkAPI.class);
        dialog = new ProgressDialog(getContext());
        username = view.findViewById(R.id.username_edit_text);
        viewLinear = view.findViewById(R.id.signupView);
        userEmail = view.findViewById(R.id.email_edit_text);
        userPassword = view.findViewById(R.id.password_edit_text);
        create_user = view.findViewById(R.id.create_USerAccount);
        radioGroup = view.findViewById(R.id.radioGrp);
        reader = view.findViewById(R.id.reader_checkbox);
        writer = view.findViewById(R.id.writer_checkbox);
        publisher = view.findViewById(R.id.publish_checkbox);
        reader.setOnClickListener(this);
        writer.setOnClickListener(this);
        publisher.setOnClickListener(this);
        actorType = view.findViewById(R.id.actor_Spinner_signup);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actorType.setSelection(0);
        actorType.setAdapter(adapter);
        create_user.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioM:
                        gender = "male";
                        break;
                    case R.id.radioF:
                        gender = "female";
                        break;

                }
            }
        });
        actorType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                String label = parent.getItemAtPosition(arg2).toString();
                // Showing selected spinner item
                checkboxlist = label;


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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
        switch (view.getId()) {

            case R.id.create_USerAccount:
                if (isvalidate()) {
                    registrationUser(username.getText().toString(), userPassword.getText().toString(), userEmail.getText().toString(), checkboxlist, gender);
                }
                break;


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

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_Fragment, fragment);
        transaction.commit();
    }

    public void fetchUserCredentials(final String user_name, final String password, final String email, final String publisher_type, String gender) {
        Call<RegistrationResponse> registrationResponseCall = networkAPI.userRegistration(new RegistrationBody(user_name, password, email, publisher_type, gender));
        registrationResponseCall.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegistrationResponse> call, @NonNull Response<RegistrationResponse> response) {
                RegistrationResponse registrationResponse = response.body();
                Log.d("response", registrationResponse.toString());
                if (registrationResponse != null) {
                    if (registrationResponse.getError().equalsIgnoreCase("true")) {
                        Log.d("pub", registrationResponse.getUser_data().get(0).getPublisher_type());
                        new SessionManager(getActivity().getApplicationContext()).storeUserPublishtype(registrationResponse.getUser_data().get(0).getPublisher_type());
                        new SessionManager(getActivity().getApplicationContext()).storeUserLoginStatus(1);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Succesfully Login", Toast.LENGTH_SHORT).show();
                            }
                        });
                        gotoHome();
                    } else {

                        Toast.makeText(getContext(), "Login Error", Toast.LENGTH_LONG).show();

                    }

                } else {

                }
            }

            @Override
            public void onFailure(@NonNull Call<RegistrationResponse> call, @NonNull Throwable t) {
                Log.d("error", t.getLocalizedMessage());
            }
        });
    }

    private Boolean isvalidate() {
        String userNAme = username.getText().toString();
        String user_Email = userEmail.getText().toString();
        String userpass = userPassword.getText().toString();
        if (!userNAme.isEmpty()) {
            if (!user_Email.isEmpty()) {
                if (!userpass.isEmpty()) {
                    if (gender != null) {
                        return true;
                    } else {

                        Toast.makeText(getContext(), "Please Select your gender", Toast.LENGTH_LONG).show();
                        return false;
                    }


                } else {

                    Toast.makeText(getContext(), "Please Enter your Password", Toast.LENGTH_LONG).show();
                    return false;
                }
            } else {
                Toast.makeText(getContext(), "Please Enter your Email", Toast.LENGTH_LONG).show();
                return false;
            }


        } else {
            Toast.makeText(getContext(), "Please Enter your Username", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    public void gotoHome() {
        Intent intent = new Intent(getActivity(), HomeScreen.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }


    private void registrationUser(String full_name, String password, String email, String publisher_type, String gender) {
        ApiRequest request = new ApiRequest();
        dialog.setMessage("please wait");
        dialog.show();
        request.requestforRegistration(full_name, password, email, publisher_type, gender, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                dialog.dismiss();
                String myResponse = response.body().string();

                Gson gson = new GsonBuilder().create();
                RegistrationResponse form = gson.fromJson(myResponse, RegistrationResponse.class);
                new SessionManager(getContext().getApplicationContext()).storeUseruserID(form.getUser_data().get(0).getId());
                new SessionManager(getContext().getApplicationContext()).storeUserName(form.getUser_data().get(0).getUser_name());

                if (form.getError().equalsIgnoreCase("false")) {

                    new SessionManager(getContext().getApplicationContext()).storeUserPublishtype(form.getUser_data().get(0).getPublisher_type());
                    new SessionManager(getContext().getApplicationContext()).storeUserLoginStatus(1);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Succesfully Login", Toast.LENGTH_SHORT).show();
                        }
                    });
                    gotoHome();
                }
            }
        });
    }


}


