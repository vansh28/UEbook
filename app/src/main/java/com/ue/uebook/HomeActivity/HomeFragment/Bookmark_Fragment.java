package com.ue.uebook.HomeActivity.HomeFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.DeatailActivity.Book_Detail_Screen;
import com.ue.uebook.HomeActivity.HomeFragment.Adapter.Bookmark_List_Adapter;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.UserBookmarkResponse;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Bookmark_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Bookmark_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Bookmark_Fragment extends Fragment implements Bookmark_List_Adapter.BookmarkBookItemClick {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView bookmark_Book_list;
    private Bookmark_List_Adapter bookmark_list_adapter;
    private ProgressDialog dialog;
    private TextView textNobookmarkList;
    private Dialog mdialog;
    private int textSize = 16;

    private OnFragmentInteractionListener mListener;

    public Bookmark_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Bookmark_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Bookmark_Fragment newInstance(String param1, String param2) {
        Bookmark_Fragment fragment = new Bookmark_Fragment();
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
        View view= inflater.inflate(R.layout.fragment_bookmark_, container, false);
        bookmark_Book_list = view.findViewById(R.id.bookmark_Book_list);
         dialog = new ProgressDialog(getContext());

        LinearLayoutManager linearLayoutManagerBookmark = new LinearLayoutManager(getContext());
        linearLayoutManagerBookmark.setOrientation(LinearLayoutManager.VERTICAL);
        bookmark_Book_list.setLayoutManager(linearLayoutManagerBookmark);
        textNobookmarkList=view.findViewById(R.id.textNobookmarkList);
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
    public  void onStart(){
        super.onStart();
        getBookmarkList(new SessionManager(getActivity().getApplicationContext()).getUserID());

    }
    @Override
    public void onItemClick(int position, String bookid) {
        Intent intent = new Intent(getActivity(), Book_Detail_Screen.class);
        intent.putExtra("book_id", bookid);
        getActivity().startActivity(intent);
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
    private void getBookmarkList(String user_id) {
        ApiRequest request = new ApiRequest();
        showLoadingIndicator();
        request.requestforgetBookmarkList(user_id,new okhttp3.Callback() {
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
                final UserBookmarkResponse form = gson.fromJson(myResponse, UserBookmarkResponse.class);
             if (form.getData()!=null&&form.getError().equalsIgnoreCase("false")){
                 getActivity().runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         bookmark_list_adapter = new Bookmark_List_Adapter((AppCompatActivity) getContext(),form.getData(),textSize);
                         bookmark_Book_list.setVisibility(View.VISIBLE);
                         bookmark_Book_list.setAdapter(bookmark_list_adapter);
                         bookmark_list_adapter.setItemClickListener(Bookmark_Fragment.this);
                         bookmark_list_adapter.notifyDataSetChanged();
                         textNobookmarkList.setVisibility(View.GONE);
                     }
                 });
             }
             else {
                 getActivity().runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         textNobookmarkList.setVisibility(View.VISIBLE);
                         bookmark_Book_list.setVisibility(View.GONE);
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
