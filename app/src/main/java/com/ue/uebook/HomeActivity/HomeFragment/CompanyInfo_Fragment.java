package com.ue.uebook.HomeActivity.HomeFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.ue.uebook.AboutUs.AboutusScreen;
import com.ue.uebook.Contactus.ContactusScreen;
import com.ue.uebook.Help.HelpCenterScreen;
import com.ue.uebook.HomeActivity.InerfaceLanguageScreen;
import com.ue.uebook.R;
import com.ue.uebook.ShareUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompanyInfo_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompanyInfo_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyInfo_Fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RelativeLayout share_container ,language_container,contactUs_container,help_container,aboutUs_container;

    private OnFragmentInteractionListener mListener;

    public CompanyInfo_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompanyInfo_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompanyInfo_Fragment newInstance(String param1, String param2) {
        CompanyInfo_Fragment fragment = new CompanyInfo_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_company_info_, container, false);
        share_container=view.findViewById(R.id.share_container);
        language_container=view.findViewById(R.id.language_container);
        contactUs_container=view.findViewById(R.id.contactUs_container);
        help_container=view.findViewById(R.id.help_container);
        aboutUs_container=view.findViewById(R.id.aboutUs_container);
        help_container.setOnClickListener(this);
        aboutUs_container.setOnClickListener(this);
        contactUs_container.setOnClickListener(this);
        language_container.setOnClickListener(this);
        share_container.setOnClickListener(this);
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
    public void onClick(View view) {
        if (view==share_container){

            ShareUtils.shareToAllApp(getActivity(),"this is Uebook App");
        }
        else if (view == language_container){
        Intent intent= new Intent(getContext(), InerfaceLanguageScreen.class);
        getContext().startActivity(intent );
        }
        else if (view==contactUs_container){
            Intent intent= new Intent(getContext(), ContactusScreen.class);
            getContext().startActivity(intent );

        }
        else if (view==aboutUs_container){
            Intent intent= new Intent(getContext(), AboutusScreen.class);
            getContext().startActivity(intent );
        }
        else if (view==help_container){

            Intent intent= new Intent(getContext(), HelpCenterScreen.class);
            getContext().startActivity(intent );
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
}
