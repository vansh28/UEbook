package com.ue.uebook.ChatSdk;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.ChatSdk.Adapter.ChatListAdapter;
import com.ue.uebook.ChatSdk.Pojo.AllchatResponse;
import com.ue.uebook.ChatSdk.Pojo.Data;
import com.ue.uebook.ChatSdk.Pojo.UserList;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.GlideUtils;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatHistoryFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatHistoryFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatHistoryFrag extends Fragment implements View.OnClickListener, ChatListAdapter.ItemClick {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ProgressDialog progressDialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView chatList;
    private ChatListAdapter chatAdapter;
    private SwipeRefreshLayout swipe_refresh_layout;
    private FloatingActionButton newChatbtn;
    private OnFragmentInteractionListener mListener;
    private Data data;
    private List<UserList> userListList;
    private BroadcastReceiver mReceiver;
    private TextView noHistoryView;

    public ChatHistoryFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatHistoryFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatHistoryFrag newInstance(String param1, String param2) {
        ChatHistoryFrag fragment = new ChatHistoryFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_history, container, false);
        chatList = view.findViewById(R.id.chatList);
        userListList = new ArrayList<>();
        swipe_refresh_layout = view.findViewById(R.id.swipe_refresh_layout);
        newChatbtn = view.findViewById(R.id.newChatbtn);
        newChatbtn.setOnClickListener(this);
        noHistoryView = view.findViewById(R.id.noHistoryView);
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(getContext());
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        chatList.setLayoutManager(linearLayoutManagerPopularList);
        chatList.setNestedScrollingEnabled(false);
        getChatHistory(new SessionManager(getActivity().getApplicationContext()).getUserID());
        pullTorefreshswipe();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void pullTorefreshswipe() {
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onRefresh() {
                getChatHistory(new SessionManager(getActivity().getApplicationContext()).getUserID());
                swipe_refresh_layout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.MAIN");
        getChatHistorys(new SessionManager(getActivity().getApplicationContext()).getUserID());
        mReceiver = new BroadcastReceiver() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
                String msg_for_me = intent.getStringExtra("some_msg");
                //log our message value
                Log.i("InchooTutorial", msg_for_me);
                getChatHistorys(new SessionManager(getActivity().getApplicationContext()).getUserID());
            }
        };
        //registering our receiver
        getActivity().registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //unregister our receiver
        getActivity().unregisterReceiver(this.mReceiver);
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
        if (v == newChatbtn) {
            Intent intent = new Intent(getContext(), ContactListScreen.class);
            getContext().startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getChatHistory(String user_id) {
        ApiRequest request = new ApiRequest();
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        if (userListList.size() > 0)
            userListList.clear();
        request.requestforgetAllchatHistory(user_id, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                progressDialog.dismiss();
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                progressDialog.dismiss();
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final AllchatResponse form = gson.fromJson(myResponse, AllchatResponse.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (form.getUserList() != null) {
                            noHistoryView.setVisibility(View.INVISIBLE);
                            userListList = form.getUserList();
                            data = form.getData();
                            chatAdapter = new ChatListAdapter(form.getData(), form.getUserList(), (AppCompatActivity) getContext(), new SessionManager(getActivity().getApplicationContext()).getUserID());
                            chatList.setAdapter(chatAdapter);
                            chatAdapter.setItemClickListener(ChatHistoryFrag.this);
                        } else {
                            noHistoryView.setVisibility(View.VISIBLE);

                        }
                    }
                });
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getChatHistorys(String user_id)
    {
        ApiRequest request = new ApiRequest();

        if (userListList.size() > 0)
            userListList.clear();
        request.requestforgetAllchatHistory(user_id, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final AllchatResponse form = gson.fromJson(myResponse, AllchatResponse.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (form.getUserList() != null) {
                            userListList = form.getUserList();
                            data = form.getData();
                            chatAdapter = new ChatListAdapter(form.getData(), form.getUserList(), (AppCompatActivity) getContext(), new SessionManager(getActivity().getApplicationContext()).getUserID());
                            chatList.setAdapter(chatAdapter);
                            chatAdapter.setItemClickListener(ChatHistoryFrag.this);
                        } else {

                        }
                    }
                });
            }
        });
    }

    @Override
    public void onUserChatClick(String channelID, String sendTo, String name, String image ,int type) {
        if (type == 11) {

            Intent intent = new Intent(getContext(), MessageScreen.class);
            intent.putExtra("sendTo", sendTo);
            intent.putExtra("channelID", channelID);
            intent.putExtra("name", name);
            intent.putExtra("imageUrl", image);
            intent.putExtra("id", 1);
            startActivity(intent);
            getActivity().finish();
        }



    }
    @Override
    public void onUserProfileClick(String imageurl, String oponentName) {
        imagePreview(imageurl, oponentName);
    }

    @Override
    public void onBroadcastClick(UserList userList) {
        Intent intent = new Intent(getContext(), BroadcastMessageScreen.class);
        intent.putExtra("name",userList.getBroadcast_name());
        intent.putExtra("ids",userList.getBroadcast_ids());
        intent.putExtra("broadcast_No",userList.getId());
        getActivity().startActivity(intent);
    }

    private void imagePreview(String file, String oponentName) {
        final Dialog previewDialog = new Dialog(getContext());
        previewDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        previewDialog.setContentView(getLayoutInflater().inflate(R.layout.image_layout
                , null));
        ImageView imageView = previewDialog.findViewById(R.id.image_view);
        GlideUtils.loadImage((AppCompatActivity) getActivity(), ApiRequest.BaseUrl + "upload/" + file, imageView, R.drawable.user_default, R.drawable.user_default);
        Button ok_Btn = previewDialog.findViewById(R.id.buton_ok);
        ok_Btn.setVisibility(View.GONE);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OponentUserDetailsScren.class);
                intent.putExtra("name", oponentName);
                intent.putExtra("image", file);
                intent.putExtra("id", 0);
                getActivity().startActivity(intent);
                previewDialog.dismiss();
            }
        });
        ok_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewDialog.dismiss();
            }
        });
        previewDialog.show();
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
