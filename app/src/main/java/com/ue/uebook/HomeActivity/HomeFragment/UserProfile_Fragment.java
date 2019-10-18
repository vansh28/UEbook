package com.ue.uebook.HomeActivity.HomeFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.LoginActivity.Pojo.LoginResponse;
import com.ue.uebook.LoginActivity.Pojo.RegistrationResponse;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;
import java.util.Objects;

import static com.ue.uebook.R.id.country__userProfile;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserProfile_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserProfile_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfile_Fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProgressDialog dialog;
    private EditText username, useremail, userpassword, country__user, about_me;
    private Spinner actor_Spinner;
    private Button update__userProfile;
    private String actortype;
    private Dialog mdialog;


    private OnFragmentInteractionListener mListener;

    public UserProfile_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfile_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfile_Fragment newInstance(String param1, String param2) {
        UserProfile_Fragment fragment = new UserProfile_Fragment();
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile_, container, false);
        dialog = new ProgressDialog(getContext());
        actor_Spinner = view.findViewById(R.id.actor_Spinner);
        username = view.findViewById(R.id.username_edit_text);
        useremail = view.findViewById(R.id.email_userProfile);
        userpassword = view.findViewById(R.id.password__userProfile);
        update__userProfile = view.findViewById(R.id.update__userProfile);
        about_me = view.findViewById(R.id.brief_desc);
        country__user = view.findViewById(country__userProfile);
        update__userProfile.setOnClickListener(this);
        UserInfo(new SessionManager(getContext()).getUserID());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.planets_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        actor_Spinner.setAdapter(adapter);
        actor_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                String label = parent.getItemAtPosition(arg2).toString();

                actortype = label;


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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.update__userProfile:
                UpdateUser(userpassword.getText().toString(), useremail.getText().toString(), actortype, country__user.getText().toString(), about_me.getText().toString());
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void UserInfo(String userID) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforgetUserInfo(userID, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
              hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                String myResponse = response.body().string();

                Gson gson = new GsonBuilder().create();
                final LoginResponse form = gson.fromJson(myResponse, LoginResponse.class);
                if (form.getError() == false) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            username.setText(form.getResponse().getUser_name());
                            useremail.setText(form.getResponse().getEmail());
                            userpassword.setText(form.getResponse().getPassword());
                            country__user.setText(form.getResponse().getCountry());
                            about_me.setText(form.getResponse().getAbout_me());
                            if (form.getResponse().getPublisher_type().equalsIgnoreCase("Reader")) {
                                actor_Spinner.setSelection(0);
                            } else if (form.getResponse().getPublisher_type().equalsIgnoreCase("Writer")) {
                                actor_Spinner.setSelection(1);
                            } else if (form.getResponse().getPublisher_type().equalsIgnoreCase("Publish House")) {
                                actor_Spinner.setSelection(2);
                            } else if (form.getResponse().getPublisher_type().equalsIgnoreCase("Reader and Writer")) {
                                actor_Spinner.setSelection(3);
                            }
                        }
                    });
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void UpdateUser(String password, String email, String publisher_type, String country, String about_me) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforUpdateProfile(new SessionManager(getContext()).getUserID(), password, email, publisher_type, country, about_me, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                hideLoadingIndicator();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                RegistrationResponse form = gson.fromJson(myResponse, RegistrationResponse.class);
                if (form.getError().equals("false") && form.getUser_data() != null) {
                    new SessionManager(getContext()).storeUserPublishtype(form.getUser_data().getPublisher_type());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            UserInfo(new SessionManager(getContext()).getUserID());

                        }
                    });

                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void showLoadingIndicator() {
        mdialog = new Dialog(getContext());
        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(mdialog.getWindow()).setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mdialog.setContentView(R.layout.layout_loading_indicator);
        mdialog.setCancelable(true);
        mdialog.setCanceledOnTouchOutside(true);
        mdialog.show();
    }

    public void hideLoadingIndicator() {
        if (mdialog != null && mdialog.isShowing()) {
            mdialog.cancel();
            mdialog = null;
        }
    }


}
