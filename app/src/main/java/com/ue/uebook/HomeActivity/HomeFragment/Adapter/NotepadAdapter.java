package com.ue.uebook.HomeActivity.HomeFragment.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.HomeActivity.HomeFragment.Pojo.NotepadUserList;
import com.ue.uebook.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotepadAdapter  extends RecyclerView.Adapter<NotepadAdapter.MyViewHolder>{
    private  NotepadItemClick notepadItemClickl;
    private List<NotepadUserList>data;
    public NotepadAdapter(List<NotepadUserList> data) {
        this.data=data;
    }

    public interface NotepadItemClick {
        void onItemClick(int position);
    }
    public void setItemClickListener(NotepadItemClick clickListener) {
        notepadItemClickl = clickListener;
    }
    @NonNull
    @Override
    public NotepadAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notepad_item, parent, false);
        NotepadAdapter.MyViewHolder vh = new NotepadAdapter.MyViewHolder(v);
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

        holder.notepadTextdate.setText(data.get(position).getCreated_at());
        if (data.get(position).getDescription().length()>11){
            holder.textView.setText(getFirst10Words(data.get(position).getDescription())+"...");
        }
        else {
            holder.textView.setText(data.get(position).getDescription());
        }
    }

    public String getFirst10Words(String arg) {
        Pattern pattern = Pattern.compile("([\\S]+\\s*){1,10}");
        Matcher matcher = pattern.matcher(arg);
        matcher.find();
        return matcher.group();
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView,notepadTextdate;
        LinearLayout notepadContainer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            notepadContainer=itemView.findViewById(R.id.notepadContainer);
            textView=itemView.findViewById(R.id.notepadText);
            notepadTextdate=itemView.findViewById(R.id.notepadTextdate);
        }
    }
}
