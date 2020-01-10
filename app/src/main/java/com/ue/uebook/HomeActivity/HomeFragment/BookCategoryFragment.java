package com.ue.uebook.HomeActivity.HomeFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.BookCategory.BookCategorywiseList;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.HomeActivity.HomeFragment.Adapter.BookCategoryAdapter;
import com.ue.uebook.R;
import com.ue.uebook.UploadBook.Pojo.BookCategoryResponsePojo;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class BookCategoryFragment extends Fragment implements BookCategoryAdapter.CategoryItemClick, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView categoryList;
    private EditText edittext_search;
    private OnFragmentInteractionListener mListener;
    private BookCategoryAdapter bookCategoryAdapter;

    public BookCategoryFragment() {
        // Required empty public constructor
    }
    public static BookCategoryFragment newInstance(String param1, String param2) {
        BookCategoryFragment fragment = new BookCategoryFragment();
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
        View view= inflater.inflate(R.layout.fragment_book_category, container, false);
        categoryList=view.findViewById(R.id.categoryList);
        edittext_search=view.findViewById(R.id.edittext_search);
        edittext_search.setOnClickListener(this);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
//        categoryList.setLayoutManager(gridLayoutManager);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        categoryList.setLayoutManager(linearLayoutManager);


        getBookCategory();
        edittext_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bookCategoryAdapter.filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    @Override
    public void onItemClick(String CategoryId,String CategotyName) {
        Intent intent = new Intent(getContext(), BookCategorywiseList.class);
        intent.putExtra("categoryId",CategoryId);
        intent.putExtra("categoryName",CategotyName);
        getContext().startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view==edittext_search){


        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void getBookCategory() {
        ApiRequest request = new ApiRequest();
        request.requestforGetbookCategory(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myresponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final BookCategoryResponsePojo form = gson.fromJson(myresponse, BookCategoryResponsePojo.class);

                if (getActivity() != null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bookCategoryAdapter= new BookCategoryAdapter((AppCompatActivity) getContext(),form.getResponse());
                            categoryList.setAdapter(bookCategoryAdapter);
                            bookCategoryAdapter.setItemClickListener(BookCategoryFragment.this);
                        }
                    });
                }
            }
        });
    }

}
