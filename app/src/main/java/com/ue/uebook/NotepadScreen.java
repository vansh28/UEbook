package com.ue.uebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.Data.ApiRequest;

import java.io.IOException;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class NotepadScreen extends AppCompatActivity implements View.OnClickListener {
    private ImageButton back_btn ,save_Post,delete_btn;
    private Intent intent;
    private Button updateNote;
    private EditText notes_view,notes_title;
    private String description,note_id,title;
    private Integer  id;
    private int textSize;
    private RelativeLayout layoutmain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad_screen);
        fontsize();
        back_btn = findViewById(R.id.back_btn_notes);
        save_Post=findViewById(R.id.save_Post);
        delete_btn=findViewById(R.id.delete_Btn);
        notes_view=findViewById(R.id.notes_view);
        updateNote=findViewById(R.id.updateNote);
        notes_title=findViewById(R.id.notes_title);
        updateNote.setOnClickListener(this);
        save_Post.setOnClickListener(this);
        delete_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        intent = getIntent();
        notes_view.setTextSize(textSize);
        notes_view.setOnClickListener(this);
        layoutmain=findViewById(R.id.layoutmain);
        layoutmain.setOnClickListener(this);
         id = intent.getIntExtra("id",0);
        description=intent.getStringExtra("description");
        note_id=intent.getStringExtra("note_id");
        title=intent.getStringExtra("title");
        if (id==1){
            delete_btn.setVisibility(View.VISIBLE);
             notes_view.setText(description);
             notes_title.setText(title);

        }
        else {
            delete_btn.setVisibility(View.GONE);
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
        else if (view==save_Post) {

            if (id == 1) {
                if (isvalidate()){

                    updateNotes(note_id, notes_view.getText().toString(),notes_title.getText().toString());
                }
            } else {
                if (isvalidate())
                {
                    AddNotes(new SessionManager(getApplicationContext()).getUserID(), notes_view.getText().toString(),notes_title.getText().toString());
                }
                else
                    {
                    Toast.makeText(getApplicationContext(), "Please Add Notes For Update", Toast.LENGTH_SHORT).show();
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
    private void AddNotes(String user_id,String desc,String title) {
        ApiRequest request = new ApiRequest();
        request.requestforAddNotes(user_id,desc,title,new okhttp3.Callback() {
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
    private void updateNotes(String note_id , String description,String title) {
        ApiRequest request = new ApiRequest();
        request.requestforupdateNote(note_id,description,title,new okhttp3.Callback() {
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
                                  finish();
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
    private void fontsize(){
        switch(new SessionManager(getApplicationContext()).getfontSize()) {
            case "smallest":
                textSize = 12;
                break;
            case "small":
                textSize = 14;
                break;
            case "normal":
                textSize = 16;
                break;
            case "large":
                textSize = 18;
                break;
            case "largest":
                textSize = 24;
                break;
        }
    }
    private Boolean isvalidate() {
        String notestitle = notes_title.getText().toString();
        String notesview = notes_view.getText().toString();
        if (!notestitle.isEmpty()) {
            if (!notesview.isEmpty()) {
                return true;
            } else {
                notes_view.setError("Enter your Note here");
                notes_view.requestFocus();
                notes_view.setEnabled(true);
                return false;
            }

        } else {
            notes_title.setError("Enter your note title");
            notes_title.requestFocus();
            notes_title.setEnabled(true);

            return false;
        }
    }

}
