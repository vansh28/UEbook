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

public class Language_adapter extends RecyclerView.Adapter<Language_adapter.MyViewHolder> {


    private LanguageItemClick languageItemClick;
    private String[] lanuage;
    private String[] language_name;
    private Context context;
    private Boolean isSelectedLanguage = false;
    private String val []={"en", "fr","de","es"};

    private static CheckBox lastChecked = null;
    private static int mCheckedPostion = 0;

    public Language_adapter(Context applicationContext, String[] language, String[] language_name) {
        this.context = applicationContext;
        this.lanuage = language;
        this.language_name = language_name;
    }


    public interface LanguageItemClick {
        void onItemClick(int position ,String value);
    }

    public void setItemClickListener(LanguageItemClick clickListener) {
        languageItemClick = clickListener;
    }


    @NonNull
    @Override
    public Language_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_list_item, parent, false);
// set the view's size, margins, paddings and layout parameters
        Language_adapter.MyViewHolder vh = new Language_adapter.MyViewHolder(v); // pass the view to View Holder


        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final Language_adapter.MyViewHolder holder, final int position) {

        holder.selected_language.setChecked(position == mCheckedPostion);
        holder.language_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (languageItemClick != null) {
                    languageItemClick.onItemClick(position,val[position]);
                    if (position == mCheckedPostion) {
                        holder.selected_language.setChecked(false);
                        mCheckedPostion = -1;
                    } else {
                        mCheckedPostion = position;
                        notifyDataSetChanged();
                    }


                }
            }
        });

        holder.language_name.setText(language_name[position]);
        holder.language.setText(lanuage[position]);

    }

    @Override
    public int getItemCount() {
        return language_name.length;
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
