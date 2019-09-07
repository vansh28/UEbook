package com.ue.uebook.HomeActivity.HomeFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ue.uebook.DeatailActivity.Book_Detail_Screen;
import com.ue.uebook.HomeActivity.HomeFragment.Adapter.Home_recommended_Adapter;
import com.ue.uebook.HomeActivity.HomeFragment.Adapter.New_Book_Home_Adapter;
import com.ue.uebook.HomeActivity.HomeFragment.Adapter.PopularList_Home_Adapter;
import com.ue.uebook.PopularActivity.Popular_List_Screen;
import com.ue.uebook.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_Fragment extends Fragment implements View.OnClickListener , Home_recommended_Adapter.RecommendedItemClick , New_Book_Home_Adapter.NewBookItemClick , PopularList_Home_Adapter.PopularBookItemClick ,UserMainFragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recommended_list,newBook_list ,popular_list;
    private Home_recommended_Adapter home_recommended_adapter;
    private Button popular_more_btn;
    private PopularList_Home_Adapter popularList_home_adapter;
    private New_Book_Home_Adapter new_book_home_adapter;


    private OnFragmentInteractionListener mListener;

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
        View view= inflater.inflate(R.layout.fragment_home_, container, false);
        recommended_list=view.findViewById(R.id.recommended_list);
        newBook_list = view.findViewById(R.id.newBook_list);
        popular_more_btn= view.findViewById(R.id.popular_more_btn);
        popular_list=view.findViewById(R.id.popular_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
        recommended_list.setLayoutManager(linearLayoutManager);
        home_recommended_adapter = new Home_recommended_Adapter();
        recommended_list.setAdapter(home_recommended_adapter);
        popular_more_btn.setOnClickListener(this);
        home_recommended_adapter.setItemClickListener(this);

        LinearLayoutManager linearLayoutManagerBook = new LinearLayoutManager(getContext());
        linearLayoutManagerBook.setOrientation(LinearLayoutManager.HORIZONTAL);
        newBook_list.setLayoutManager(linearLayoutManagerBook);
        new_book_home_adapter = new New_Book_Home_Adapter();
        newBook_list.setAdapter(new_book_home_adapter);
        new_book_home_adapter.setItemClickListener(this);


        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(getContext());
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        popular_list.setLayoutManager(linearLayoutManagerPopularList);
        popularList_home_adapter = new PopularList_Home_Adapter();
        popular_list.setAdapter(popularList_home_adapter);
        popularList_home_adapter.setItemClickListener(this);
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
        if (view==popular_more_btn){
            Intent intent = new Intent(getActivity(), Popular_List_Screen.class);
            getActivity().startActivity(intent);

        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), Book_Detail_Screen.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void onItemClick_NewBook(int position) {
        Intent intent = new Intent(getActivity(), Book_Detail_Screen.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void onItemClick_PopularBook(int position) {
        Intent intent = new Intent(getActivity(), Book_Detail_Screen.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
}
