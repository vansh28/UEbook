package com.ue.uebook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class NotepadScreen extends AppCompatActivity implements View.OnClickListener {
    private ImageButton back_btn ,edit_btn,delete_btn;
    private Intent intent;
    private Button updateNote;
    private EditText notes_view;


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
        int id = intent.getIntExtra("id",0);
        if (id==1){
            edit_btn.setVisibility(View.VISIBLE);
            delete_btn.setVisibility(View.VISIBLE);
            notes_view.setEnabled(false);
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
            notes_view.requestFocus();
        }
        else if (view==updateNote){

        }
    }
    private void confirmDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You sure, that you want Delete ")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
