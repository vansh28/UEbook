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

import com.ue.uebook.HomeActivity.HomeFragment.Adapter.NotepadAdapter;
import com.ue.uebook.HomeActivity.HomeFragment.Adapter.PopularList_Home_Adapter;
import com.ue.uebook.NotepadScreen;
import com.ue.uebook.R;

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

    private OnFragmentInteractionListener mListener;

    public NotepadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotepadFragment.
     */
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notepad, container, false);
        notepad_list=view.findViewById(R.id.notepad_list);
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(getContext());
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        notepad_list.setLayoutManager(linearLayoutManagerPopularList);
        notepadAdapter = new NotepadAdapter();
        notepad_list.setAdapter(notepadAdapter);
        notepad_list.setNestedScrollingEnabled(true);
        notepadAdapter.setItemClickListener(this);


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
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), NotepadScreen.class);
        intent.putExtra("id",1);
        startActivity(intent);

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
