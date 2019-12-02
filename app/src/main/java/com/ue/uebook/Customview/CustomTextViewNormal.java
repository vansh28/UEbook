package com.ue.uebook.Customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.ue.uebook.Constants;
import com.ue.uebook.FontCache;

public class CustomTextViewNormal extends AppCompatTextView {
    public CustomTextViewNormal(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public CustomTextViewNormal(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public CustomTextViewNormal(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

   /* public CustomTextViewNormal(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyCustomFont(context);
    }*/



    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(Constants.FONT_NORMAL, context);
        setTypeface(customFont);
    }


}
