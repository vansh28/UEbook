package com.ue.uebook.HomeActivity.HomeFragment;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.DeatailActivity.Book_Detail_Screen;
import com.ue.uebook.HomeActivity.HomeFragment.Adapter.Home_recommended_Adapter;
import com.ue.uebook.HomeActivity.HomeFragment.Adapter.New_Book_Home_Adapter;
import com.ue.uebook.HomeActivity.HomeFragment.Adapter.PopularList_Home_Adapter;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.HomeListing;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.HomeListingResponse;
import com.ue.uebook.HomeActivity.HomeScreen;
import com.ue.uebook.PopularActivity.Popular_List_Screen;
import com.ue.uebook.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.ue.uebook.NetworkUtils.getInstance;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_Fragment extends Fragment implements View.OnClickListener, Home_recommended_Adapter.RecommendedItemClick, New_Book_Home_Adapter.NewBookItemClick, PopularList_Home_Adapter.PopularBookItemClick, UserMainFragment.OnFragmentInteractionListener , Search_Fragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recommended_list, newBook_list, popular_list;
    private Home_recommended_Adapter home_recommended_adapter;
    private Button popular_more_btn;
    private PopularList_Home_Adapter popularList_home_adapter;
    private New_Book_Home_Adapter new_book_home_adapter;
    private EditText edittext_search;
    private List<HomeListing> recommendedList_book, newBookList,popularBook_List;
    private HomeScreen activity;
    private OnFragmentInteractionListener mListener;
    private TextView recommemnded_view,newBookview,textNobookfound;
    private RelativeLayout popularview;
    private Dialog mdialog;
    private SwipeRefreshLayout pullTorfrsh;

    public Home_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Home_Fragment newInstance(String param1, String param2) {
        Home_Fragment fragment = new Home_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recommendedList_book = new ArrayList<>();
        newBookList = new ArrayList<>();
        popularBook_List = new ArrayList<>();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_, container, false);
        recommended_list = view.findViewById(R.id.recommended_list);

        edittext_search = view.findViewById(R.id.edittext_search);
        newBook_list = view.findViewById(R.id.newBook_list);
        popular_more_btn = view.findViewById(R.id.popular_more_btn);
        popular_list = view.findViewById(R.id.popular_list);
        recommemnded_view=view.findViewById(R.id.recommended_view);
        newBookview=view.findViewById(R.id.newBook_view);
        popularview=view.findViewById(R.id.popularList_view);
        textNobookfound=view.findViewById(R.id.textNobookfound);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
        recommended_list.setLayoutManager(linearLayoutManager);
        pullTorfrsh=view.findViewById(R.id.swipe_refresh_layout);
        popular_more_btn.setOnClickListener(this);
        activity = (HomeScreen) getActivity();
        displayData();
        LinearLayoutManager linearLayoutManagerBook = new LinearLayoutManager(getContext());
        linearLayoutManagerBook.setOrientation(LinearLayoutManager.HORIZONTAL);
        newBook_list.setLayoutManager(linearLayoutManagerBook);
        recommended_list.setNestedScrollingEnabled(false);
        newBook_list.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(getContext());
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        popular_list.setLayoutManager(linearLayoutManagerPopularList);
        popular_list.setNestedScrollingEnabled(false);
        edittext_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new Search_Fragment());
            }
        });
        pullTorefreshswipe();
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
    public void onStart() {
        super.onStart();
        if (getInstance(getActivity()).isConnectingToInternet()){
            getRecommenedBookList("1");
            getnewBookList("2");
            getPopularList();
        }
        else {

            Toast.makeText(getContext(),"No Internet Connection",Toast.LENGTH_SHORT);
        }

    }

    @Override
    public void onClick(View view) {
        if (view == popular_more_btn) {
            Intent intent = new Intent(getActivity(), Popular_List_Screen.class);
            getActivity().startActivity(intent);

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
    private void pullTorefreshswipe(){
        pullTorfrsh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onRefresh() {
                getRecommenedBookList("1");
                getnewBookList("2");
                getPopularList();
                pullTorfrsh.setRefreshing(false);
            }
        });
    }
    @Override
    public void onItemClick(int position, String book_id) {
        Intent intent = new Intent(getActivity(), Book_Detail_Screen.class);
        intent.putExtra("book_id", book_id);
        intent.putExtra("position",position);
        getActivity().startActivity(intent);
    }

    @Override
    public void onItemClick_NewBook(int position, String book_id) {
        Intent intent = new Intent(getActivity(), Book_Detail_Screen.class);
        intent.putExtra("book_id", book_id);
        intent.putExtra("position",position);
        getActivity().startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onItemClick_PopularBook(int position, String book_id) {
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
    private void displayData() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        if (recommendedList_book != null && !(recommendedList_book.isEmpty() && (newBook_list!=null) && (popularBook_List!=null))) {
                            Thread.sleep(1000);
                            if(getActivity()!=null){
                                getActivity().runOnUiThread(new Runnable() {
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void run() {

                                    }
                                });
                            }
                            break;
                        } else
                            Thread.sleep(1000);
                    }
                } catch (InterruptedException ignored) {
                }
            }
        };
        t.start();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getRecommenedBookList(String categoryId) {
        showLoadingIndicator();
        ApiRequest request = new ApiRequest();
        if (recommendedList_book.size()>0)
            recommendedList_book.clear();

        request.requestforgetBookList(categoryId, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                hideLoadingIndicator();

            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String myResponse = response.body().string();
                hideLoadingIndicator();
                Gson gson = new GsonBuilder().create();
                final HomeListingResponse form = gson.fromJson(myResponse, HomeListingResponse.class);
                if (form.getData() != null || !form.getData().isEmpty()||form.getError().equalsIgnoreCase("false")) {
                    recommendedList_book.addAll(form.getData());
                    if (getActivity()!=null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            home_recommended_adapter = new Home_recommended_Adapter((AppCompatActivity) getActivity(), recommendedList_book);
                            recommended_list.setAdapter(home_recommended_adapter);
                            home_recommended_adapter.setItemClickListener(Home_Fragment.this);
                            home_recommended_adapter.notifyDataSetChanged();
                            recommended_list.setNestedScrollingEnabled(false);
                            recommemnded_view.setVisibility(View.VISIBLE);
                        }
                    });
                }}
                else {
                    recommemnded_view.setVisibility(View.GONE);
                }

            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getnewBookList(String categoryId) {

        ApiRequest request = new ApiRequest();
        if (newBookList.size()>0)
            newBookList.clear();

        request.requestforgetBookList(categoryId, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                hideLoadingIndicator();

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String myResponse = response.body().string();

                Gson gson = new GsonBuilder().create();
                final HomeListingResponse form = gson.fromJson(myResponse, HomeListingResponse.class);
                if (form.getData() != null || !form.getData().isEmpty()||form.getError().equalsIgnoreCase("false")) {
                    newBookList.addAll(form.getData());
                    if(getActivity()!=null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new_book_home_adapter = new New_Book_Home_Adapter((AppCompatActivity) getActivity(), newBookList);
                                newBook_list.setAdapter(new_book_home_adapter);
                                new_book_home_adapter.setItemClickListener(Home_Fragment.this);
                                newBookview.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
                else {
                    newBookview.setVisibility(View.GONE);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getPopularList() {
        if (popularBook_List.size()>0)
            popularBook_List.clear();
        ApiRequest request = new ApiRequest();
        request.requestforGetPopularBook(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", e.getLocalizedMessage());
                hideLoadingIndicator();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String myresponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final HomeListingResponse form = gson.fromJson(myresponse, HomeListingResponse.class);
                if (form.getData() != null || !form.getData().isEmpty()||form.getError().equalsIgnoreCase("false")) {
                    if (getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                popularList_home_adapter = new PopularList_Home_Adapter((AppCompatActivity) getActivity(),popularBook_List);
                                popular_list.setAdapter(popularList_home_adapter);
                                popularList_home_adapter.setItemClickListener(Home_Fragment.this);
                                popularview.setVisibility(View.VISIBLE);
                            }
                        });


                    }
                    if (form.getData().size()>3){
                        popularBook_List.add(form.getData().get(0));
                        popularBook_List.add(form.getData().get(1));
                        popularBook_List.add(form.getData().get(2));
                    }
                    else {
                        popularBook_List.addAll(form.getData());
                    }
                }
                else {
                    popularview.setVisibility(View.GONE);
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
}
