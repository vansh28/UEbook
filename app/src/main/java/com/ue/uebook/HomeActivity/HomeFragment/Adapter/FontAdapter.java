package com.ue.uebook.HomeActivity.HomeFragment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ue.uebook.R;

public class FontAdapter   extends RecyclerView.Adapter<FontAdapter.MyViewHolder> {
    private FontItemClick fontItemClick ;
    private String[] lanuage;
    private String[] fontname;
    private Context context;
    private Boolean isSelectedLanguage = false;
    private String val []={"smallest", "small","normal","large","largest"};
    private int id;
    public FontAdapter(Context applicationContext,  String[] fontname, int id) {
        this.context = applicationContext;
        this.fontname = fontname;
        this.id=id;
    }
    public interface FontItemClick {
        void onfontItemClick(int position ,String value);
    }
    public void setItemClickListener(FontItemClick clickListener) {
        fontItemClick = clickListener;
    }
    @NonNull
    @Override
    public FontAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_list_item, parent, false);
// set the view's size, margins, paddings and layout parameters
        FontAdapter.MyViewHolder vh = new FontAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull final FontAdapter.MyViewHolder holder, final int position) {
        holder.selected_language.setChecked(position==id);
       holder.language_name.setVisibility(View.GONE);
//        holder.selected_language.setChecked(position == mCheckedPostion);
        holder.language_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fontItemClick != null) {
                    fontItemClick.onfontItemClick(position,val[position]);
                    if (position == id) {
                        holder.selected_language.setChecked(true);
                        id = -1;
                    } else {
                        id = position;
                        notifyDataSetChanged();
                    }
                }
            }
        });
        holder.language.setText(fontname[position]);
    }
    @Override
    public int getItemCount() {
        return fontname.length;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout language_change;
        TextView language, language_name;
        CheckBox selected_language;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            language_change = itemView.findViewById(R.id.language_change);
            language = itemView.findViewById(R.id.language);
            language_name = itemView.findViewById(R.id.language_name);
            selected_language = itemView.findViewById(R.id.selected_language);
        }
    }
}
