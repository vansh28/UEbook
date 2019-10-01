package com.ue.uebook.Quickblox_Chat.utils.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.ue.uebook.R;


public class MessageTextViewRight extends MessageTextView {

    public MessageTextViewRight(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void setLinearSide() {
        LayoutParams layoutParams = (LayoutParams) frameLinear.getLayoutParams();
        layoutParams.gravity = Gravity.RIGHT;
        frameLinear.setLayoutParams(layoutParams);
    }

    @Override
    protected void setTextLayout() {
        viewTextStub.setLayoutResource(R.layout.widget_text_msg_right);
        layoutStub = (LinearLayout) viewTextStub.inflate();
    }
}