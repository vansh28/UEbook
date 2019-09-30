package com.ue.uebook.Quickblox_Chat.utils.ui;

import java.util.List;

public class FriendListPojo {

    private boolean error;
    private List<FriendData>data;

    public FriendListPojo(boolean error, List<FriendData> data) {
        this.error = error;
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<FriendData> getData() {
        return data;
    }

    public void setData(List<FriendData> data) {
        this.data = data;
    }
}
