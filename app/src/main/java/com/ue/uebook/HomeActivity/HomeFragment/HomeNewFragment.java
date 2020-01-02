package com.ue.uebook.HomeActivity.HomeFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.ChatSdk.ChatListScreen;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.DeatailActivity.Book_Detail_Screen;
import com.ue.uebook.HomeActivity.BookListing;
import com.ue.uebook.HomeActivity.HomeFragment.Adapter.BookListnewAdaper;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.HomeListing;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.HomeListingResponse;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;
import com.ue.uebook.UploadBook.Upload_Book_Screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeNewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeNewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeNewFragment extends Fragment implements View.OnClickListener ,BookCategoryFragment.OnFragmentInteractionListener ,BookListnewAdaper.PopularBookItemClick{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView booklist;
    private OnFragmentInteractionListener mListener;
    private BookListnewAdaper bookListnewAdaper;
    private LinearLayout uploadBook,readBook,chatBtn;
    private List<HomeListing> recommendedList_book, newBookList, popularBook_List;
    private int textSize = 16;

    public HomeNewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeNewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeNewFragment newInstance(String param1, String param2) {
        HomeNewFragment fragment = new HomeNewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popularBook_List= new ArrayList<>();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home_new, container, false);
        booklist=view.findViewById(R.id.booklist);
        uploadBook=view.findViewById(R.id.uploadBookBtn);
        readBook=view.findViewById(R.id.readBookBtn);
        chatBtn=view.findViewById(R.id.chatBtn);
        uploadBook.setOnClickListener(this);
        readBook.setOnClickListener(this);
        chatBtn.setOnClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        booklist.setLayoutManager(gridLayoutManager);
        getPopularList();
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
    public void onClick(View v) {
        if (v==uploadBook){
            Intent intent = new Intent(getContext(), Upload_Book_Screen.class);
            getContext().startActivity(intent);
        }
        else if (v==readBook)
        {
            Intent intent = new Intent(getContext(), BookListing.class);
            startActivity(intent);
        }
        else if (v==chatBtn)
        {
            Intent intent = new Intent(getContext(), ChatListScreen.class);
            startActivity(intent);
        }

    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onItemClick_PopularBook(int position, String book_id) {
        Intent intent = new Intent(getActivity(), Book_Detail_Screen.class);
        intent.putExtra("book_id", book_id);
        intent.putExtra("position", position);
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
    private void getPopularList() {
        if (popularBook_List.size() > 0)
            popularBook_List.clear();
        ApiRequest request = new ApiRequest();
        request.requestforGetPopularBook(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", e.getLocalizedMessage());


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String myresponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final HomeListingResponse form = gson.fromJson(myresponse, HomeListingResponse.class);
                if (form.getData() != null) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bookListnewAdaper = new BookListnewAdaper((AppCompatActivity) getActivity(), popularBook_List, textSize);
                                booklist.setAdapter(bookListnewAdaper);
                                bookListnewAdaper.setItemClickListener(HomeNewFragment.this);
//                                popularview.setVisibility(View.GONE);
//                                popular_view.setTextSize(textSize);
//                                popular_more_btn.setTextSize(textSize);
                            }
                        });
                    }
                    if (form.getData().size() > 6) {
                        popularBook_List.add(form.getData().get(0));
                        popularBook_List.add(form.getData().get(1));
                        popularBook_List.add(form.getData().get(2));
                        popularBook_List.add(form.getData().get(3));
                        popularBook_List.add(form.getData().get(4));
                        popularBook_List.add(form.getData().get(5));
                    } else {
                        popularBook_List.addAll(form.getData());
                    }

                }
            }
        });
    }
    private void fontsize() {
        switch (new SessionManager(getActivity().getApplicationContext()).getfontSize()) {
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
