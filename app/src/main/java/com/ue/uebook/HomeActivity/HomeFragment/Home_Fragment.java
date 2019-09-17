package com.ue.uebook.HomeActivity.HomeFragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private ProgressDialog dialog;
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
//        getRecommenedBookList("1");
//        getnewBookList("2");

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
        dialog = new ProgressDialog(getContext());
        edittext_search = view.findViewById(R.id.edittext_search);
        newBook_list = view.findViewById(R.id.newBook_list);
        popular_more_btn = view.findViewById(R.id.popular_more_btn);
        popular_list = view.findViewById(R.id.popular_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
        recommended_list.setLayoutManager(linearLayoutManager);
        popular_more_btn.setOnClickListener(this);
        activity = (HomeScreen) getActivity();
//        recommendedList_book = activity.getRecommendedListBookData();
//        popularBook_List=activity.getpopularBookData();
        displayData();
        LinearLayoutManager linearLayoutManagerBook = new LinearLayoutManager(getContext());
        linearLayoutManagerBook.setOrientation(LinearLayoutManager.HORIZONTAL);
        newBook_list.setLayoutManager(linearLayoutManagerBook);
//        newBookList = activity.getnewBookData();
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
//        edittext_search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
////                home_recommended_adapter.filter(charSequence.toString());
////                new_book_home_adapter.filter(charSequence.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
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
                                        getRecommenedBookList("1");

                                        getnewBookList("2");
                                        getPopularList();




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
        dialog.show();
        ApiRequest request = new ApiRequest();
        if (recommendedList_book.size()>0)
            recommendedList_book.clear();

        request.requestforgetBookList(categoryId, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                dialog.dismiss();

            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String myResponse = response.body().string();
                dialog.dismiss();
                Gson gson = new GsonBuilder().create();
                final HomeListingResponse form = gson.fromJson(myResponse, HomeListingResponse.class);
                if (form.getError().equalsIgnoreCase("false") && form.getData() != null && !form.getData().isEmpty()) {
                    recommendedList_book.addAll(form.getData());
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        home_recommended_adapter = new Home_recommended_Adapter((AppCompatActivity) getActivity(), recommendedList_book);
                        recommended_list.setAdapter(home_recommended_adapter);
                        home_recommended_adapter.setItemClickListener(Home_Fragment.this);
                        home_recommended_adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getnewBookList(String categoryId) {
        dialog.show();
        ApiRequest request = new ApiRequest();
        if (newBookList.size()>0)
            newBookList.clear();

        request.requestforgetBookList(categoryId, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
                dialog.dismiss();

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String myResponse = response.body().string();
                dialog.dismiss();
                Gson gson = new GsonBuilder().create();
                final HomeListingResponse form = gson.fromJson(myResponse, HomeListingResponse.class);
                if (form.getError().equalsIgnoreCase("false") && !form.getData().isEmpty())  {
                    newBookList.addAll(form.getData());
                }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new_book_home_adapter = new New_Book_Home_Adapter((AppCompatActivity) getActivity(), newBookList);
                    newBook_list.setAdapter(new_book_home_adapter);
                    new_book_home_adapter.setItemClickListener(Home_Fragment.this);
                }
            });
            }
        });
    }

    private void getPopularList() {
        if (popularBook_List.size()>0)
            popularBook_List.clear();
        dialog.show();
        ApiRequest request = new ApiRequest();
        request.requestforGetPopularBook(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", e.getLocalizedMessage());
                dialog.dismiss();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dialog.dismiss();
                String myresponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                final HomeListingResponse form = gson.fromJson(myresponse, HomeListingResponse.class);
                if (form.getError().equalsIgnoreCase("false") && !form.getData().isEmpty())  {
                    if (form.getData().size()>3){
                        popularBook_List.add(form.getData().get(0));
                        popularBook_List.add(form.getData().get(1));
                        popularBook_List.add(form.getData().get(2));
                    }
                    else {
                        popularBook_List.addAll(form.getData());
                    }

                }
              getActivity().runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      popularList_home_adapter = new PopularList_Home_Adapter((AppCompatActivity) getActivity(),popularBook_List);
                      popular_list.setAdapter(popularList_home_adapter);
                      popularList_home_adapter.setItemClickListener(Home_Fragment.this);
                  }
              });

            }
        });


}



}
