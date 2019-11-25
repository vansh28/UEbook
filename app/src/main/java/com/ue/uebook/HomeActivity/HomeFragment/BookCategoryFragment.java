package com.ue.uebook.HomeActivity.HomeFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookCategoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookCategoryFragment extends Fragment implements BookCategoryAdapter.CategoryItemClick {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView categoryList;
    private OnFragmentInteractionListener mListener;
    private BookCategoryAdapter bookCategoryAdapter;

    public BookCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_book_category, container, false);
        categoryList=view.findViewById(R.id.categoryList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        categoryList.setLayoutManager(gridLayoutManager);
        getBookCategory();
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
    public void onItemClick(String CategoryId,String CategotyName) {
        Intent intent = new Intent(getContext(), BookCategorywiseList.class);
        intent.putExtra("categoryId",CategoryId);
        intent.putExtra("categoryName",CategotyName);
        getContext().startActivity(intent);
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
    private void getBookCategory() {
        ApiRequest request = new ApiRequest();
        request.requestforGetbookCategory(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", e.getLocalizedMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myresponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final BookCategoryResponsePojo form = gson.fromJson(myresponse, BookCategoryResponsePojo.class);
             getActivity().runOnUiThread(new Runnable() {
             @Override
             public void run() {
               bookCategoryAdapter= new BookCategoryAdapter((AppCompatActivity) getContext(),form.getResponse());
               categoryList.setAdapter(bookCategoryAdapter);
               bookCategoryAdapter.setItemClickListener(BookCategoryFragment.this);
           }
       });
            }
        });


    }

}
