package com.ue.uebook.Data;

import java.io.File;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ApiRequest {
    private static final String BaseUrl = "http://dnddemo.com/ebooks/api/v1/";

    public void requestforRegistration(final String full_name, final String password, final String email, final String publisher_type, final String gender, final String country, final String about_me, Callback callback) {
        String url = null;
        url = BaseUrl + "createUser";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_name", full_name)
                .addFormDataPart("password", password)
                .addFormDataPart("email", email)
                .addFormDataPart("publisher_type", publisher_type)
                .addFormDataPart("gender", gender)
                .addFormDataPart("country", country)
                .addFormDataPart("about_me", about_me)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public void requestforgetUserInfo(String user_id, Callback callback) {
        String url = null;
        url = BaseUrl + "getUserInfo";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", user_id)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public void requestforforgotPassword(final String email, Callback callback) {
        String url = null;
        url = BaseUrl + "forgetPassword";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("email", email)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void requestforUpdateProfile(final String user_id, final String password, final String email, final String publisher_type, final String country, final String about_me, Callback callback) {
        String url = null;
        url = BaseUrl + "userEdit";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", user_id)
                .addFormDataPart("password", password)
                .addFormDataPart("email", email)
                .addFormDataPart("publisher_type", publisher_type)
                .addFormDataPart("country", country)
                .addFormDataPart("about_me", about_me)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public void requestforUpdateProfilePic(final String user_id, File profile_image, Callback callback) {
        String url = null;
        url = BaseUrl + "UpdatePrfilePic";
        OkHttpClient client = new OkHttpClient();
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", user_id)
                .addFormDataPart("profile_image", profile_image.getName(), RequestBody.create(MEDIA_TYPE_PNG, profile_image))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public void requestforUploadBook(final String user_id, File video, String category_id, String book_title, Callback callback) {
        String url = null;
        url = BaseUrl + "addNewBook";
        OkHttpClient client = new OkHttpClient();
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("*/*");
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", user_id)
                .addFormDataPart("category_id", category_id)
                .addFormDataPart("book_title", book_title)
                .addFormDataPart("video_url", video.getName(), RequestBody.create(MEDIA_TYPE_PNG, video))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public void requestforGetbookCategory(Callback callback) {
        String url = null;
        url = BaseUrl + "getAllCategory";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(null, new byte[]{});

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public void requestforgetBookList(final String category_id, Callback callback) {
        String url = null;
        url = BaseUrl + "getBooksByTypes";
        OkHttpClient client = new OkHttpClient();
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("*/*");
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("category_id", category_id)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public void requestforgetBookDetail(final String book_id, final String user_id, Callback callback) {
        String url = null;
        url = BaseUrl + "getBookDetail";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("book_id", book_id)
                .addFormDataPart("user_id", user_id)

                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public void requestforaddBookmark(final String book_id, final String bookmark_status, final String userId, Callback callback) {
        String url = null;
        url = BaseUrl + "bookMark";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("book_id", book_id)
                .addFormDataPart("user_id", userId)

                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void requestforgetBookmarkList(final String userId, Callback callback) {
        String url = null;
        url = BaseUrl + "getAllbookMarkByUser";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)

                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void requestforAddNotes(final String userId, final String description, Callback callback) {
        String url = null;
        url = BaseUrl + "addNote";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)
                .addFormDataPart("description", description)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void requestforgetNotesList(final String userId, Callback callback) {
        String url = null;
        url = BaseUrl + "getAllNotebyUser";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userId)

                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void requestforupdateNote(final String note_id, final String description, Callback callback) {
        String url = null;
        url = BaseUrl + "UpdateNoteBook";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("note_id", note_id)
                .addFormDataPart("description", description)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void requestforDeleteNote(final String note_id, Callback callback) {
        String url = null;
        url = BaseUrl + "DeleteNote";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("note_id", note_id)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }


    public void requestforGetPopularBook(Callback callback) {
        String url = null;
        url = BaseUrl + "getAllpopularBook";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(null, new byte[]{});
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }


    public void requestforGetAllBook(Callback callback) {
        String url = null;
        url = BaseUrl + "saerchAllbooks";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(null, new byte[]{});
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void requestforAddComment(final String user_id, final String books_id, String comment, String rating, Callback callback) {
        String url = null;
        url = BaseUrl + "addReview";
        OkHttpClient client = new OkHttpClient();
        final MediaType MEDIA_TYPE_PNG = MediaType.parse("*/*");
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", user_id)
                .addFormDataPart("books_id", books_id)
                .addFormDataPart("comment", comment)
                .addFormDataPart("rating", rating)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public void requestforContactUs(final String user_id, final String name, String email, String phone, String contatMessage, Callback callback) {
        String url = null;
        url = BaseUrl + "contact_us";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", user_id)
                .addFormDataPart("name", name)
                .addFormDataPart("email", email)
                .addFormDataPart("phone", phone)
                .addFormDataPart("contatMessage", contatMessage)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void requestforPostChatId(final String user_id, final String chat_id, Callback callback) {
        String url = null;
        url = BaseUrl + "createChatId";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", user_id)
                .addFormDataPart("chat_id", chat_id)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void requestforSendFriendRequest(final String user_id, final String frnd_id, Callback callback) {
        String url = null;
        url = BaseUrl + "sendFrndReq";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", user_id)
                .addFormDataPart("frnd_id", frnd_id)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void requestforgetUploadByInfo(String user_id, Callback callback) {
        String url = null;
        url = BaseUrl + "getUserDetails";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", user_id)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public void requestforgetPendingRequest(String user_id, Callback callback) {
        String url = null;
        url = BaseUrl + "getAllRequestbyUser";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", user_id)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void requestforAcceptRequest(String friend_id, String status, Callback callback) {
        String url = null;
        url = BaseUrl + "acceptedRequest";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("friend_id", friend_id)
                .addFormDataPart("status", status)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    public void requestforgetAllFriendList(String user_id, Callback callback) {
        String url = null;
        url = BaseUrl + "getAllAcceptedFriend";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", user_id)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void requestforsubmitAssignmentAnswer(String answer, Callback callback) {
        String url = null;
        url = BaseUrl + "answerQuestion";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("answer", answer)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void requestforDeleteAuthorBook(String answer, Callback callback) {
        String url = null;
        url = BaseUrl + "DeleteBook";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("books_id", answer)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public void requestforSendMail(String from, String emailto,String subject,String message ,Callback callback) {
        String url = null;
        url = BaseUrl + "sendEmailData";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("from", from)
                .addFormDataPart("emailto", emailto)
                .addFormDataPart("subject", subject)
                .addFormDataPart("message", message)

                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void requestforWordDefination(String word, Callback callback) {
        String url = null;
        url = BaseUrl + "dictionaryWord";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("word", word)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public void requestforVerifyISBN(String isbnNumber, Callback callback) {
        String url = null;
        url = BaseUrl + "validateIsbn";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("isbn_number", isbnNumber)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public void requestforPendingBook(String userID, Callback callback) {
        String url = null;
        url = BaseUrl + "getPendingBookByUser";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("user_id", userID)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public void requestforgetPendingBookDetail(String books_id, Callback callback) {
        String url = null;
        url = BaseUrl + "getBookById";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("books_id", books_id)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}


