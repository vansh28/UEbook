package com.ue.uebook.HomeActivity.HomeFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.DeatailActivity.Book_Detail_Screen;
import com.ue.uebook.HomeActivity.HomeFragment.Adapter.Search_History_Adapter;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.HomeListing;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.HomeListingResponse;
import com.ue.uebook.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Search_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Search_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Search_Fragment extends Fragment implements View.OnClickListener, Search_History_Adapter.SearchHistoryItemClick {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView search_history_list;
    private Search_History_Adapter search_history_adapter;
    private EditText edittext_search;
    private List<HomeListing>bookList;
    private ImageView bookimage_search;


    private OnFragmentInteractionListener mListener;

    public Search_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Search_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Search_Fragment newInstance(String param1, String param2) {
        Search_Fragment fragment = new Search_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookList=new ArrayList<>();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_, container, false);
        search_history_list=view.findViewById(R.id.search_history_list);
        edittext_search=view.findViewById(R.id.edittext_search);
        bookimage_search=view.findViewById(R.id.bookimage_search);
        edittext_search.requestFocus();
        edittext_search.setOnClickListener(this);
        getAllBookList();
        search_history_list.setNestedScrollingEnabled(true);
        search_history_list.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(getActivity());
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        search_history_list.setLayoutManager(linearLayoutManagerPopularList);
        edittext_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             try {
                 search_history_list.setVisibility(View.VISIBLE);
                 bookimage_search.setVisibility(View.GONE);
                 search_history_adapter.filter(charSequence.toString());
             } catch (Exception e) {
                 e.printStackTrace();
             }
             if (charSequence.length()==0){
                 bookimage_search.setVisibility(View.GONE);
             }
             else {
                 bookimage_search.setVisibility(View.GONE);
             }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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

       }

    @Override
    public void onItemClick(int position, String book_id) {
        Intent intent = new Intent(getActivity(), Book_Detail_Screen.class);
        intent.putExtra("book_id", book_id);
        intent.putExtra("position",position);
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
    private void getRecommenedBookList(String categoryId) {
        ApiRequest request = new ApiRequest();
        if (bookList.size()>0)
            bookList.clear();

        request.requestforgetBookList(categoryId, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final HomeListingResponse form = gson.fromJson(myResponse, HomeListingResponse.class);

            }
        });
    }
    private void getAllBookList() {
        ApiRequest request = new ApiRequest();
        request.requestforGetAllBook(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myresponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final HomeListingResponse form = gson.fromJson(myresponse, HomeListingResponse.class);
                if (form.getError().equalsIgnoreCase("false") && form.getData() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            search_history_adapter = new Search_History_Adapter((AppCompatActivity) getActivity(),form.getData());
                            search_history_list.setAdapter(search_history_adapter);
                            search_history_adapter.setItemClickListener(Search_Fragment.this);
                            search_history_adapter.notifyDataSetChanged();
                        }
                    });
                }
            }

        });


    }



}
