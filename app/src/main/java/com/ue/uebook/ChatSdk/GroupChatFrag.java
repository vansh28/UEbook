package com.ue.uebook.ChatSdk;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.ChatSdk.Adapter.GroupListAdapter;
import com.ue.uebook.ChatSdk.Pojo.GrouplistResponse;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupChatFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupChatFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupChatFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView groupList;
     private GroupListAdapter groupListAdapter;
    private OnFragmentInteractionListener mListener;
    private SwipeRefreshLayout swipe_refresh_layout;

    public GroupChatFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupChatFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupChatFrag newInstance(String param1, String param2) {
        GroupChatFrag fragment = new GroupChatFrag();
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
        View view= inflater.inflate(R.layout.fragment_group_chat, container, false);
        groupList=view.findViewById(R.id.groupList);
        swipe_refresh_layout=view.findViewById(R.id.swipe_refresh_layout);
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(getContext());
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        groupList.setLayoutManager(linearLayoutManagerPopularList);
        groupList.setNestedScrollingEnabled(false);
        getGroupList(new SessionManager(getActivity().getApplicationContext()).getUserID());
        pullTorefreshswipe();
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
    private void getGroupList(String user_id   ) {
        ApiRequest request = new ApiRequest();
        request.requestforgetGroupList(user_id ,new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final GrouplistResponse form = gson.fromJson(myResponse, GrouplistResponse.class);
                if (form.getError()==false && form.getGroup_details()!=null){
//
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        groupListAdapter = new GroupListAdapter((AppCompatActivity)getActivity(),form.getGroup_details());
                        groupList.setAdapter(groupListAdapter);



                    }
                });
//
//
                }
            }
        });
    }

    private void pullTorefreshswipe(){
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onRefresh() {
                 getGroupList(new SessionManager(getActivity().getApplicationContext()).getUserID());
                swipe_refresh_layout.setRefreshing(false);
            }
        });
    }



}
