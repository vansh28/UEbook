package com.ue.uebook.Customview;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.ue.uebook.Constants;
import com.ue.uebook.FontCache;

public class CustomEditTextNormal extends EditText {
    public CustomEditTextNormal(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public CustomEditTextNormal(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public CustomEditTextNormal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

   /* public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }*/

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(Constants.FONT_NORMAL, context);
        setTypeface(customFont);
    }
}

