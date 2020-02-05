package com.ue.uebook.ChatSdk;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.ChatSdk.Adapter.ContactListAdapter;
import com.ue.uebook.ChatSdk.Pojo.ContactListResponse;
import com.ue.uebook.ChatSdk.Pojo.OponentData;
import com.ue.uebook.ChatSdk.Pojo.UserData;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendListFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendListFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendListFrag extends Fragment implements ContactListAdapter.ItemClick, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageButton back_btn;
    private ContactListAdapter contactListAdapter;
    private RecyclerView contactList;
    private LinearLayout creategroupBtn;
    private TextView noView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FriendListFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendListFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendListFrag newInstance(String param1, String param2) {
        FriendListFrag fragment = new FriendListFrag();
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
        View view= inflater.inflate(R.layout.fragment_friend_list, container, false);
        creategroupBtn = view.findViewById(R.id.creategroupBtn);
        noView=view.findViewById(R.id.noView);
        creategroupBtn.setOnClickListener(this);
        contactList=view.findViewById(R.id.contactList);

        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(getContext());
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        contactList.setLayoutManager(linearLayoutManagerPopularList);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getContactList(new SessionManager(getActivity().getApplicationContext()).getUserID());
        }
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
    public void onContactListItemClick(OponentData oponentData , UserData userData)
    {
        Intent intent = new Intent(getContext(),MessageScreen.class);
        intent.putExtra("oponentdata",oponentData);
        intent.putExtra("userData",userData);
        intent.putExtra("id",2);
        startActivity(intent);
        getActivity().finish();
    }
    @Override
    public void onProfileClick(String url) {
        imagePreview(url);
    }
    private void imagePreview(String file) {
        final Dialog previewDialog = new Dialog(getContext());
        previewDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        previewDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout ,null));
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
    public void onClick(View v) {
        if (v==creategroupBtn){

            Intent intent = new Intent(getContext(),GroupFriendList.class);
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getContactList(String  user_id) {
        ApiRequest request = new ApiRequest();

        request.requestforgetContactList( user_id,new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");

            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final ContactListResponse form = gson.fromJson(myResponse, ContactListResponse.class);

                if ( form.error == false && form.getUserList()!=null){
                   getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            creategroupBtn.setVisibility(View.VISIBLE);
                            contactListAdapter = new ContactListAdapter((AppCompatActivity) getActivity(),form.getUserList(),form.getData());
                            contactList.setAdapter(contactListAdapter);
                            contactListAdapter.setItemClickListener(FriendListFrag.this);
                            contactListAdapter.notifyDataSetChanged();
                            noView.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            creategroupBtn.setVisibility(View.INVISIBLE);
                            noView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
    }
}
