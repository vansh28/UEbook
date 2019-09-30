package com.ue.uebook.Quickblox_Chat.utils.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.ue.uebook.Quickblox_Chat.utils.ToastUtils;
import com.ue.uebook.Quickblox_Chat.utils.chat.ChatHelper;
import com.ue.uebook.Quickblox_Chat.utils.qb.QbUsersHolder;
import com.ue.uebook.Quickblox_Chat.utils.ui.adapter.UsersAdapter;
import com.ue.uebook.R;
import java.util.List;
public class ChatInfoActivity extends BaseActivity {
    private static final String EXTRA_DIALOG = "dialog";
    private ListView usersListView;
    private QBChatDialog qbDialog;

    public static void start(Context context, QBChatDialog qbDialog) {
        Intent intent = new Intent(context, ChatInfoActivity.class);
        intent.putExtra(EXTRA_DIALOG, qbDialog);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_info);

//        actionBar.setDisplayHomeAsUpEnabled(false);
        usersListView = findViewById(R.id.list_chat_info_users);
        qbDialog = (QBChatDialog) getIntent().getSerializableExtra(EXTRA_DIALOG);

        getDialog();
    }
    private void getDialog() {
        String dialogID = qbDialog.getDialogId();
        ChatHelper.getInstance().getDialogById(dialogID, new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                qbDialog = qbChatDialog;
                buildUserList();
            }

            @Override
            public void onError(QBResponseException e) {
                ToastUtils.shortToast(e.getMessage());
                finish();
            }
        });
    }
    private void buildUserList() {
        List<Integer> userIds = qbDialog.getOccupants();
        List<QBUser> users = QbUsersHolder.getInstance().getUsersByIds(userIds);
        UsersAdapter adapter = new UsersAdapter(this, users);
        usersListView.setAdapter(adapter);
    }
}