package com.ue.uebook.ChatSdk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.ChatSdk.Adapter.CallLogAdapter;
import com.ue.uebook.ChatSdk.Pojo.CallAllResponse;
import com.ue.uebook.ChatSdk.Pojo.callResponse;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;
import com.ue.uebook.VideoSdk.VideoCall;
import com.ue.uebook.VoiceCall.VoiceCallActivity;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CallLogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CallLogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CallLogFragment extends Fragment implements CallLogAdapter.ItemClick {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CallLogAdapter callLogAdapter;
    private RecyclerView callLogList;
    private List<callResponse>callResponseList;
    private TextView textViewNocall;

    private OnFragmentInteractionListener mListener;

    public CallLogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CallLogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CallLogFragment newInstance(String param1, String param2) {
        CallLogFragment fragment = new CallLogFragment();
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
        callResponseList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_call_log, container, false);
         callLogList = view.findViewById(R.id.callLogList);
        textViewNocall=view.findViewById(R.id.textViewNocall);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        callLogList.setLayoutManager(linearLayoutManager);
       getAllCallLogList(new SessionManager(getContext().getApplicationContext()).getUserID());

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
    public void onContactListItemClick(String channelId, String FriendID, String type) {

        Log.e("type",channelId+FriendID+type);
          if (type.equalsIgnoreCase("audio")){
              Intent intents = new Intent(getContext(), VoiceCallActivity.class);
              intents.putExtra("id", channelId);
              intents.putExtra("receiverid", FriendID);
              getContext().startActivity(intents);
        }
        else if (type.equalsIgnoreCase("video")){
              Intent intent = new Intent(getContext(), VideoCall.class);
              intent.putExtra("id", channelId);
              intent.putExtra("receiverid", FriendID);
              getContext().startActivity(intent);
        }
    }

    @Override
    public void onDeleteClick(View view ,String id) {
         showFilterPopup(view,id);

    }
    private void showFilterPopup(View v , String chatid) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.inflate(R.menu.deletepopup);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        deleteCall(chatid);
                        Toast.makeText(getContext(),"deleted",Toast.LENGTH_SHORT).show();


                    default:
                        return false;
                }
            }
        });
        popup.show();
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

    public void getAllCallLogList(final String userID){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiRequest.testBaseUrl +"userstatus/addViewCallLogs",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e(" statusview_response", response);
                        if (callResponseList.size()>0)
                            callResponseList.clear();
                        Gson gson = new GsonBuilder().create();
                        final CallAllResponse form = gson.fromJson(response, CallAllResponse.class);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (form.getData().size()>0){
                                    callLogList.setVisibility(View.VISIBLE);
                                    callLogAdapter = new CallLogAdapter((AppCompatActivity) getContext(),form.getData(),userID);
                                    callLogList.setAdapter(callLogAdapter);
                                    textViewNocall.setVisibility(View.GONE);
                                    callLogAdapter.setItemClickListener(CallLogFragment.this);
                                }
                                else {
                                    callLogList.setVisibility(View.GONE);
                                    textViewNocall.setVisibility(View.VISIBLE);
                                }

                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
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
                arguments.put("flag","view" );
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

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of HomeFragment");
        super.onResume();
        getAllCallLogList(new SessionManager(getContext().getApplicationContext()).getUserID());
    }
    public void deleteCall(final String log_id){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiRequest.testBaseUrl +"userstatus/deleteCallLogs",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e(" statusview_response", response);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getAllCallLogList(new SessionManager(getContext().getApplicationContext()).getUserID());
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
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
                arguments.put("user_id",new SessionManager(getActivity().getApplicationContext()).getUserID());
                arguments.put("log_id",log_id );
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

}
