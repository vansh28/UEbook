package com.ue.uebook.HomeActivity.HomeFragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.ue.uebook.HomeActivity.HomeFragment.Adapter.User_Search_List;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.HomeListing;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.HomeListingResponse;
import com.ue.uebook.R;
import com.ue.uebook.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;
public class Search_Fragment extends Fragment implements View.OnClickListener, Search_History_Adapter.SearchHistoryItemClick ,User_Search_List.SearchListItemClick{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView search_history_list,search_list;
    private Search_History_Adapter search_history_adapter;
    private EditText edittext_search;
    private ImageView bookimage_search ,audio_search_btn;
    private List<HomeListing>data;
    private User_Search_List user_search_list;
    private OnFragmentInteractionListener mListener;
    private List<String> list;
    private int textSize;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public Search_Fragment() {
        // Required empty public constructor
    }
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
        data= new ArrayList<>();
        fontsize();
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
        search_list=view.findViewById(R.id.search_list);
        search_list.setVisibility(View.GONE);
        edittext_search=view.findViewById(R.id.edittext_search);
        audio_search_btn=view.findViewById(R.id.audio_search_btn);
        audio_search_btn.setOnClickListener(this);
        bookimage_search=view.findViewById(R.id.bookimage_search);
        edittext_search.requestFocus();
        edittext_search.setOnClickListener(this);
        getAllBookList();
        search_history_list.setNestedScrollingEnabled(true);
        search_history_list.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(getActivity());
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        search_history_list.setLayoutManager(linearLayoutManagerPopularList);
        if (new SessionManager(getActivity().getApplicationContext()).getArrayList("list")!=null){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            search_list.setLayoutManager(linearLayoutManager);
            user_search_list = new User_Search_List(new SessionManager(getActivity()).getArrayList("list"));
            search_list.setAdapter(user_search_list);
        user_search_list.setItemClickListener(this);
        }
        edittext_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             try {
                 search_history_list.setVisibility(View.VISIBLE);
                 bookimage_search.setVisibility(View.GONE);
                 search_list.setVisibility(View.GONE);
                 search_history_adapter.filter(charSequence.toString());
             } catch (Exception e) {
                 e.printStackTrace();
             }
             if (charSequence.length()==0){
                 bookimage_search.setVisibility(View.GONE);
                 if (new SessionManager(getActivity().getApplicationContext()).getArrayList("list")!=null){
                     LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                     linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                     search_list.setLayoutManager(linearLayoutManager);
                     user_search_list = new User_Search_List(new SessionManager(getActivity()).getArrayList("list"));
                     search_list.setAdapter(user_search_list);
                     user_search_list.setItemClickListener(Search_Fragment.this);
                 }
                 search_list.setVisibility(View.GONE);
             }
             else {
                 bookimage_search.setVisibility(View.GONE);
                 search_list.setVisibility(View.GONE);
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
        if (view==audio_search_btn)
        {
       promptSpeechInput();

        }

       }

    @Override
    public void onItemClick(int position, String book_id) {
        Intent intent = new Intent(getActivity(), Book_Detail_Screen.class);
        intent.putExtra("book_id", book_id);
        intent.putExtra("position",position);
        List<String>list =new ArrayList<>();
        if (new SessionManager(getActivity().getApplicationContext()).getArrayList("list")==null){
            list.add(data.get(position).getBook_title());
        }
        else {
            if (new SessionManager(getActivity().getApplicationContext()).getArrayList("list").contains(data.get(position).getBook_title())){
                list.addAll(new SessionManager(getActivity().getApplicationContext()).getArrayList("list"));

            }
            else {
                list.addAll(new SessionManager(getActivity().getApplicationContext()).getArrayList("list"));
                list.add(data.get(position).getBook_title());

            }

        }
        Collections.reverse(list);
        new SessionManager(getActivity().getApplicationContext()).saveArrayList(list,"list");
        getActivity().startActivity(intent);
    }

    @Override
    public void onItemClick(String book_id) {
        edittext_search.setText(book_id);
        edittext_search.requestFocus();
        edittext_search.setSelection(edittext_search.getText().length());
        search_history_list.setVisibility(View.VISIBLE);
        search_history_adapter.filter(book_id);
        search_list.setVisibility(View.GONE);
    }
    @Override
    public void ondeleteItemClick(String position) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(),"Remove Successfully",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
                            data=form.getData();
                            search_history_adapter = new Search_History_Adapter((AppCompatActivity) getActivity(),form.getData(),textSize);
                            search_history_list.setAdapter(search_history_adapter);
                            search_history_adapter.setItemClickListener(Search_Fragment.this);
                            search_history_adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 2000000000);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edittext_search.setText(result.get(0));
                    edittext_search.setSelection(edittext_search.getText().length());
                }
                break;
            }

        }
    }
    private void fontsize()
    {
        SharedPreferences pref = getActivity().getSharedPreferences(getActivity().getPackageName() + "_preferences", Context.MODE_PRIVATE);
        switch(new SessionManager(getActivity().getApplicationContext()).getfontSize())
        {
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
