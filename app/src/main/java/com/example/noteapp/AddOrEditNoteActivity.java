package com.example.noteapp;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapp.Models.NoteModel;
import com.example.noteapp.database.DataBasehalper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddOrEditNoteActivity extends AppCompatActivity {

    TextView tv_dateAndChars;
    EditText et_title;
    EditText et_description;
    FloatingActionButton fab_submit;
    DataBasehalper dataBasehalper;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_note);

        tv_dateAndChars = (TextView) findViewById(R.id.tv_dateAndChars);
        et_title = (EditText) findViewById(R.id.et_titleInEdit);
        et_description = (EditText) findViewById(R.id.et_description);
        fab_submit = (FloatingActionButton) findViewById(R.id.fab_submitOrUpdate);
        dataBasehalper = new DataBasehalper(AddOrEditNoteActivity.this);

        // for making marquee in the date view.
        tv_dateAndChars.setSelected(true);
        // ..............................

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = dateFormat.format(currentDate);

        boolean isAdd = getIntent().getIntExtra(MainActivity.KEY_EDIT_OR_ADD, 0) == 0;
        // checking if its edit mode, if it is then change the fields.
        if(!isAdd) {
            NoteModel note = (NoteModel) getIntent().getSerializableExtra(MainActivity.KEY_NOTE_MODEL);
            et_title.setText(note.getTitle());
            et_description.setText(note.getDescription());
            tv_dateAndChars.setText(date);
        }

        // setting event listeners
        fab_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isAdd) {
                    // checking if the fields are empty if it does, then do nothing until he fill them.
                    if (et_description.getText().toString().trim().isEmpty() || et_title.getText().toString().trim().isEmpty() || tv_dateAndChars.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "You must feel all the fields", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    NoteModel createNoteModel = new NoteModel(-1, et_title.getText().toString(), et_description.getText().toString(), date);

                    boolean inserted = dataBasehalper.addNoteRow(createNoteModel);
                    if (inserted) {
                        Intent i = new Intent(AddOrEditNoteActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                } else {

                    NoteModel createNoteModel = (NoteModel) getIntent().getSerializableExtra(MainActivity.KEY_NOTE_MODEL);
                    createNoteModel.setDate(date);
                    createNoteModel.setDescription(et_description.getText().toString());
                    createNoteModel.setTitle(et_title.getText().toString());

                    boolean updated = dataBasehalper.updateNoteRow(createNoteModel);
                    if(updated) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Error while editing the note", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        et_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateDateAndCharTextView(s.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void updateDateAndCharTextView(int numChar) {
        tv_dateAndChars.setText(date + " |Characters: " + numChar);
    }
}