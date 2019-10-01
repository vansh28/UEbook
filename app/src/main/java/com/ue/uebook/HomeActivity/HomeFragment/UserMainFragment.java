package com.ue.uebook.HomeActivity.HomeFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.ue.uebook.AuthorProfileActivity.AuthorList;
import com.ue.uebook.AuthorProfileActivity.PendingRequestScreen;
import com.ue.uebook.LoginActivity.LoginScreen;
import com.ue.uebook.Quickblox_Chat.utils.SharedPrefsHelper;
import com.ue.uebook.Quickblox_Chat.utils.chat.ChatHelper;
import com.ue.uebook.Quickblox_Chat.utils.ui.activity.DialogsActivity;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;
import com.ue.uebook.UploadBook.Upload_Book_Screen;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserMainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserMainFragment extends Fragment implements View.OnClickListener, UserProfile_Fragment.OnFragmentInteractionListener, CompanyInfo_Fragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RelativeLayout userInfo_container, companyInfo_Container, uploadBook_Container, logOut,chat_Container,author_Container,pendingRequest_Container;

    private OnFragmentInteractionListener mListener;
    private View view_uploadBook,pendingRequest_view;


    public UserMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserMainFragment newInstance(String param1, String param2) {
        UserMainFragment fragment = new UserMainFragment();
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
        View view = inflater.inflate(R.layout.fragment_user_main, container, false);
        userInfo_container = view.findViewById(R.id.userInfo_container);
        chat_Container=view.findViewById(R.id.chat_Container);
        pendingRequest_Container=view.findViewById(R.id.pendingRequest_Container);
        pendingRequest_Container.setOnClickListener(this);
        pendingRequest_view=view.findViewById(R.id.pending_view);
        chat_Container.setOnClickListener(this);
        uploadBook_Container = view.findViewById(R.id.uploadBook_Container);
        view_uploadBook=view.findViewById(R.id.uploadBook_view);
        author_Container=view.findViewById(R.id.author_Container);
        author_Container.setOnClickListener(this);
        if (new SessionManager(getActivity().getApplicationContext()).getUserPublishType().equalsIgnoreCase("Reader")){
            uploadBook_Container.setVisibility(View.GONE);
            view_uploadBook.setVisibility(View.GONE);
            pendingRequest_Container.setVisibility(View.GONE);
            pendingRequest_view.setVisibility(View.GONE);
        }
        else {
            uploadBook_Container.setVisibility(View.VISIBLE);
            view_uploadBook.setVisibility(View.VISIBLE);
            pendingRequest_Container.setVisibility(View.GONE);
            pendingRequest_view.setVisibility(View.GONE);
        }
        logOut = view.findViewById(R.id.logOut);
        logOut.setOnClickListener(this);
        uploadBook_Container.setOnClickListener(this);
        userInfo_container.setOnClickListener(this);
        companyInfo_Container = view.findViewById(R.id.companyInfo_Container);
        companyInfo_Container.setOnClickListener(this);
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
        if (view == userInfo_container) {
            if ((new SessionManager(getActivity().getApplicationContext()).getUserPublishType().equalsIgnoreCase("Reader"))){

                loadFragment(new UserProfile_Fragment());
            }
            else {
                loadFragment(new UserProfile_Fragment());

//                Intent intent = new Intent(getContext(), AuthorProfileScreen.class);
//                intent.putExtra("id",1);
//                intent.putExtra("userID",new SessionManager(getActivity().getApplicationContext()).getUserID());
//                getContext().startActivity(intent);
            }

        } else if (view == companyInfo_Container) {

            loadFragment(new CompanyInfo_Fragment());
        } else if (view == uploadBook_Container) {
            Intent intent = new Intent(getContext(), Upload_Book_Screen.class);
            getContext().startActivity(intent);
        } else if (view == logOut) {
            confirmLogoutDialog();
//            Intent intent = new Intent(getContext(), SettingsActivity.class);
//            startActivity(intent);
        }
        else if (view==chat_Container){

            if (SharedPrefsHelper.getInstance().hasQbUser()) {
                restoreChatSession();
            }
        }
        else if (view==author_Container){
            Intent intent = new Intent(getContext(), AuthorList.class);
            getContext().startActivity(intent);
        }
        else if (view==pendingRequest_Container){
            Intent intent = new Intent(getContext(), PendingRequestScreen.class);
            getContext().startActivity(intent);
        }
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.user_Container, fragment);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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

    private void confirmLogoutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("You sure, that you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new SessionManager(getActivity().getApplicationContext()).clearUserCredentials();
                        Intent intent = new Intent(getContext(), LoginScreen.class);
                        getContext().startActivity(intent);
                        SharedPrefsHelper.getInstance().clearAllData();
                        getActivity().finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private QBUser getUserFromSession() {
        QBUser user = SharedPrefsHelper.getInstance().getQbUser();
        QBSessionManager qbSessionManager = QBSessionManager.getInstance();
        if (qbSessionManager.getSessionParameters() == null) {
            ChatHelper.getInstance().destroy();
            return null;
        }
        Integer userId = qbSessionManager.getSessionParameters().getUserId();
        user.setId(userId);
        return user;
    }

    private void restoreChatSession() {
        if (ChatHelper.getInstance().isLogged()) {
            Intent intent = new Intent(getContext(), DialogsActivity.class);
            getContext().startActivity(intent);

        } else {
            QBUser currentUser = getUserFromSession();
            if (currentUser == null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"Network Error please try Again",Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                loginToChat(currentUser);
            }
        }
    }

    private void loginToChat(final QBUser user) {
//        com.quickblox.sample.chat.java.ui.dialog.ProgressDialogFragment.show(getSupportFragmentManager(), R.string.dlg_restoring_chat_session);

        ChatHelper.getInstance().loginToChat(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {

//                com.quickblox.sample.chat.java.ui.dialog.ProgressDialogFragment.hide(getSupportFragmentManager());


            }

            @Override
            public void onError(QBResponseException e) {
                if (e.getMessage().equals("You have already logged in chat")) {
                    loginToChat(user);
                } else {
                    Toast.makeText(getContext(),"Network Error please try Again",Toast.LENGTH_SHORT).show();

//                    com.quickblox.sample.chat.java.ui.dialog.ProgressDialogFragment.hide(getSupportFragmentManager());
                    loginToChat(user);

                }
            }
        });
    }

}
