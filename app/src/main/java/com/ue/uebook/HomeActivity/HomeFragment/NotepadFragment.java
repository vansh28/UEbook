package com.ue.uebook.HomeActivity.HomeFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.HomeActivity.HomeFragment.Adapter.NotepadAdapter;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.NotepadResponse;
import com.ue.uebook.NotepadScreen;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;
import com.ue.uebook.ShareUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotepadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotepadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotepadFragment extends Fragment  implements  NotepadAdapter.NotepadItemClick{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView notepad_list;
    private NotepadAdapter notepadAdapter;
    private ProgressDialog dialog;
    private TextView textNonotepadList;
    private Dialog mdialog;
    private int textSize;
    private SwipeRefreshLayout swipe_refresh_layout;
    private OnFragmentInteractionListener mListener;

    public NotepadFragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static NotepadFragment newInstance(String param1, String param2) {
        NotepadFragment fragment = new NotepadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fontsize();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_notepad, container, false);
        notepad_list=view.findViewById(R.id.notepad_list);
        dialog = new ProgressDialog(getContext());
        swipe_refresh_layout=view.findViewById(R.id.swipe_refresh_layout);

        textNonotepadList=view.findViewById(R.id.textNonotepadList);
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(getContext());
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        notepad_list.setLayoutManager(linearLayoutManagerPopularList);
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
    private void pullTorefreshswipe(){
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onRefresh() {
                getnotePadList(new SessionManager(getActivity().getApplicationContext()).getUserID());
                swipe_refresh_layout.setRefreshing(false);
            }
        });
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onStart(){
        super.onStart();
        getnotePadList(new SessionManager(getActivity().getApplicationContext()).getUserID());


    }

    @Override
    public void onItemClick(String note_id, String description) {
        Intent intent = new Intent(getContext(), NotepadScreen.class);
        intent.putExtra("note_id",note_id);
        intent.putExtra("description",description);
        intent.putExtra("id",1);
        startActivity(intent);
    }

    @Override
    public void sharenotes(String note) {
        ShareUtils.shareToAllApp(getActivity(),note);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getnotePadList(String user_id) {
        ApiRequest request = new ApiRequest();
    showLoadingIndicator();
        request.requestforgetNotesList(user_id,new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                hideLoadingIndicator();

            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                hideLoadingIndicator();
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final NotepadResponse form = gson.fromJson(myResponse, NotepadResponse.class);
                if (form.getData()!=null&&form.getError().equalsIgnoreCase("false")){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notepadAdapter = new NotepadAdapter(form.getData(),textSize);
                            notepad_list.setVisibility(View.VISIBLE);
                            textNonotepadList.setVisibility(View.GONE);
                            notepad_list.setAdapter(notepadAdapter);
                            notepad_list.setNestedScrollingEnabled(true);
                            notepadAdapter.setItemClickListener(NotepadFragment.this);

                        }
                    });
                }
                else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        notepad_list.setVisibility(View.GONE);
                        textNonotepadList.setVisibility(View.VISIBLE);
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
    private void fontsize(){
        SharedPreferences pref = getActivity().getSharedPreferences(getActivity().getPackageName() + "_preferences", Context.MODE_PRIVATE);
        switch(new SessionManager(getActivity().getApplicationContext()).getfontSize()) {
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
