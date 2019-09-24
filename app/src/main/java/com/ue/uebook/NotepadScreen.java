package com.ue.uebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.Data.ApiRequest;

import java.io.IOException;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class NotepadScreen extends AppCompatActivity implements View.OnClickListener {
    private ImageButton back_btn ,edit_btn,delete_btn;
    private Intent intent;
    private Button updateNote;
    private EditText notes_view;
    private String description,note_id;
    private Integer  id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad_screen);
        back_btn = findViewById(R.id.back_btn_notes);
        edit_btn=findViewById(R.id.edit_Post);
        delete_btn=findViewById(R.id.delete_Btn);
        notes_view=findViewById(R.id.notes_view);
        updateNote=findViewById(R.id.updateNote);
        updateNote.setOnClickListener(this);
        edit_btn.setOnClickListener(this);
        delete_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        intent = getIntent();
         id = intent.getIntExtra("id",0);
        description=intent.getStringExtra("description");
        note_id=intent.getStringExtra("note_id");
        if (id==1){
            edit_btn.setVisibility(View.VISIBLE);
            delete_btn.setVisibility(View.VISIBLE);
            notes_view.setEnabled(false);
            notes_view.setText(description);
        }
        else {

            edit_btn.setVisibility(View.GONE);
            delete_btn.setVisibility(View.GONE);
            notes_view.setEnabled(true);
            notes_view.setFocusable(true);
            notes_view.requestFocus();
        }

    }

    @Override
    public void onClick(View view) {
        if (view==back_btn)
        {
            finish();
        }
        else if (view==delete_btn){
            confirmDeleteDialog();
        }
        else if (view==edit_btn){
            notes_view.setEnabled(true);;
            notes_view.setFocusable(true);
            notes_view.setSelection(notes_view.getText().length());
            notes_view.requestFocus();
        }
        else if (view==updateNote){

            if (id==1){
                updateNotes(note_id,notes_view.getText().toString());
                notes_view.setEnabled(false);
            }
            else {
                if (!notes_view.getText().toString().isEmpty()){
                    AddNotes(new SessionManager(getApplicationContext()).getUserID(),notes_view.getText().toString());
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please Add Notes For Update",Toast.LENGTH_SHORT).show();
                }

            }


        }
    }
    private void confirmDeleteDialog() {
        final PrettyDialog pDialog=  new PrettyDialog(this);
        pDialog.setIcon(R.drawable.cancel);
        pDialog.setTitle("Notes");
        pDialog.setMessage("You sure, that you want Delete");
        pDialog   .addButton(
                "Yes",					// button text
                R.color.pdlg_color_white,		// button text color
                R.color.red,		// button background color
                new PrettyDialogCallback() {		// button OnClick listener
                    @Override
                    public void onClick() {
                        deleteNotes(note_id);
                        pDialog.dismiss();
                    }
                }
        );
        pDialog   .addButton(
                "No",					// button text
                R.color.pdlg_color_white,		// button text color
                R.color.pdlg_color_green,		// button background color
                new PrettyDialogCallback() {		// button OnClick listener
                    @Override
                    public void onClick() {
                        pDialog.dismiss();
                    }
                }
        )
                .show();


    }
    private void AddNotes(String user_id,String desc) {
        ApiRequest request = new ApiRequest();
        request.requestforAddNotes(user_id,desc,new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Notes Saved",Toast.LENGTH_SHORT).show();
                        notes_view.setEnabled(false);;
                        finish();
//                        delete_btn.setVisibility(View.VISIBLE);
//                        edit_btn.setVisibility(View.VISIBLE);
                    }
                });

            }
        });
    }
    private void updateNotes(String note_id , String description) {
        ApiRequest request = new ApiRequest();
        request.requestforupdateNote(note_id,description,new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                final String myResponse = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Successfuly Updated",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
    private void deleteNotes(String note_id ) {
        ApiRequest request = new ApiRequest();
        request.requestforDeleteNote(note_id,new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                final String myResponse = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Successfuly Deleted",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

}