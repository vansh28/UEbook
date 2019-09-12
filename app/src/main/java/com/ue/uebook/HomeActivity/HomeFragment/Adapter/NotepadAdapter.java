package com.ue.uebook.HomeActivity.HomeFragment.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.R;

public class NotepadAdapter  extends RecyclerView.Adapter<NotepadAdapter.MyViewHolder>{



    private  NotepadItemClick notepadItemClickl;

    public interface NotepadItemClick {
        void onItemClick(int position);
    }

    public void setItemClickListener(NotepadItemClick clickListener) {
        notepadItemClickl = clickListener;
    }





    @NonNull
    @Override
    public NotepadAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notepad_item, parent, false);
// set the view's size, margins, paddings and layout parameters
        NotepadAdapter.MyViewHolder vh = new NotepadAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotepadAdapter.MyViewHolder holder, final int position) {
        holder.notepadContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notepadItemClickl != null) {
                    notepadItemClickl.onItemClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout notepadContainer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            notepadContainer=itemView.findViewById(R.id.notepadContainer);
            textView=itemView.findViewById(R.id.notepadText);
        }
    }
}
