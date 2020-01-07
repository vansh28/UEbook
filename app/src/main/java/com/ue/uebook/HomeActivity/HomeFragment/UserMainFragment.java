package com.ue.uebook.HomeActivity.HomeFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ue.uebook.AuthorProfileActivity.AuthorProfileScreen;
import com.ue.uebook.ChatSdk.ChatListScreen;
import com.ue.uebook.Dashboard.StartActivityHome;
import com.ue.uebook.Dictionary.DictionaryScreen;
import com.ue.uebook.PendingBook.PendingBookScreen;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;
import com.ue.uebook.UploadBook.Upload_Book_Screen;

import java.util.Objects;

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
    private RelativeLayout userInfo_container, companyInfo_Container, uploadBook_Container, logOut, chat_Container, author_Container, pendingbook_Container;
    private OnFragmentInteractionListener mListener;
    private View view_uploadBook, pendingRequest_view;
    private Dialog mdialog;
    private int textSize;
    private TextView userView, companyView, uploadView, chatView, dictionaryView, logoutView;

    public UserMainFragment() {
        // Required empty public constructor
    }
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
        View view = inflater.inflate(R.layout.usermaincategory, container, false);
        userInfo_container = view.findViewById(R.id.userInfo_container);
        fontsize();

        chat_Container = view.findViewById(R.id.chat_Container);
        userView = view.findViewById(R.id.user_info_view);
        companyView = view.findViewById(R.id.company_info_view);
        uploadView = view.findViewById(R.id.upload_view);
        chatView = view.findViewById(R.id.chatWithOther_view);
        dictionaryView = view.findViewById(R.id.dictionary_view);
        logoutView = view.findViewById(R.id.logout_view);
        userView.setTextSize(textSize);
        companyView.setTextSize(textSize);
        uploadView.setTextSize(textSize);
        chatView.setTextSize(textSize);
        dictionaryView.setTextSize(textSize);
        logoutView.setTextSize(textSize);
        pendingbook_Container = view.findViewById(R.id.pendingbook_Container);
        pendingbook_Container.setOnClickListener(this);
        pendingRequest_view = view.findViewById(R.id.pending_view);
        chat_Container.setOnClickListener(this);
        uploadBook_Container = view.findViewById(R.id.uploadBook_Container);
        view_uploadBook = view.findViewById(R.id.uploadBook_view);
        author_Container = view.findViewById(R.id.author_Container);
        author_Container.setOnClickListener(this);
        if (new SessionManager(getActivity().getApplicationContext()).getUserPublishType().equalsIgnoreCase("Reader")) {
            uploadBook_Container.setVisibility(View.GONE);
            view_uploadBook.setVisibility(View.GONE);
            pendingRequest_view.setVisibility(View.GONE);
            pendingbook_Container.setVisibility(View.GONE);
        } else {
            uploadBook_Container.setVisibility(View.VISIBLE);
            view_uploadBook.setVisibility(View.VISIBLE);
            pendingRequest_view.setVisibility(View.VISIBLE);
            pendingbook_Container.setVisibility(View.VISIBLE);
        }
        logOut = view.findViewById(R.id.logOut);
        logOut.setOnClickListener(this);
        uploadBook_Container.setOnClickListener(this);
        userInfo_container.setOnClickListener(this);
        companyInfo_Container = view.findViewById(R.id.companyInfo_Container);
        companyInfo_Container.setOnClickListener(this);
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
        if (view == userInfo_container) {
            if ((new SessionManager(getActivity().getApplicationContext()).getUserPublishType().equalsIgnoreCase("Reader"))) {

                loadFragment(new UserProfile_Fragment());
            } else {
                Intent intent = new Intent(getContext(), AuthorProfileScreen.class);
                intent.putExtra("id", 1);
                intent.putExtra("userID", new SessionManager(getActivity().getApplicationContext()).getUserID());
                getContext().startActivity(intent);
            }

        } else if (view == companyInfo_Container) {

            loadFragment(new CompanyInfo_Fragment());
        } else if (view == uploadBook_Container) {
            Intent intent = new Intent(getContext(), Upload_Book_Screen.class);
            getContext().startActivity(intent);
        } else if (view == logOut) {
            confirmLogoutDialog();
        } else if (view == chat_Container) {
            Intent intent = new Intent(getContext(), ChatListScreen.class);
            getContext().startActivity(intent);

        } else if (view == author_Container) {
            Intent intent = new Intent(getContext(), DictionaryScreen.class);
            getContext().startActivity(intent);
        } else if (view == pendingbook_Container) {
            Intent intent = new Intent(getContext(), PendingBookScreen.class);
            getContext().startActivity(intent);
        }
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.user_Container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void confirmLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("You sure, that you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new SessionManager(getActivity().getApplicationContext()).clearUserCredentials();

                        Intent intent = new Intent(getContext(), StartActivityHome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("id",1);
                        startActivity(intent);
                        new SessionManager(getActivity().getApplicationContext()).setCurrentLanguage("en");
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

    private void fontsize() {
        switch (new SessionManager(getActivity().getApplicationContext()).getfontSize()) {
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


}
