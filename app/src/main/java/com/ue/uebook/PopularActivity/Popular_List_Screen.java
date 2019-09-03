package com.ue.uebook.PopularActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.ue.uebook.HomeActivity.HomeFragment.Adapter.PopularList_Home_Adapter;
import com.ue.uebook.R;

public class Popular_List_Screen extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView popularList;
    private Adapter popularList_adapter;
    private ImageButton backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular__list__screen);
        popularList=findViewById(R.id.popularList);
        LinearLayoutManager linearLayoutManagerPopularList = new LinearLayoutManager(this);
        linearLayoutManagerPopularList.setOrientation(LinearLayoutManager.VERTICAL);
        popularList.setLayoutManager(linearLayoutManagerPopularList);
        popularList_adapter = new Adapter();
        popularList.setAdapter(popularList_adapter);
        backbtn=findViewById(R.id.backbtn_popular);
        backbtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if (view==backbtn){
            finish();
        }
    }
}
