package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.noteapp.Models.NoteModel;
import com.example.noteapp.database.DataBasehalper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tv_notesText;
    SearchView sv_notesSearcher;
    FloatingActionButton fab_addButton;
    RecyclerView rv_notesHolder;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    DataBasehalper dataBasehalper;
    List<NoteModel> noteModelList;

    // global key values for sending to other activity to know which task to do;
    // if the value equals to 0, then add.
    // if the value equals to 1, then edit.
    public static String KEY_EDIT_OR_ADD = "EDIT_OR_ADD";
    public static String KEY_NOTE_MODEL = "NOTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializes apps widgets for future use.
        tv_notesText = findViewById(R.id.tv_notesText);
        sv_notesSearcher = findViewById(R.id.sv_notesSearcher);
        fab_addButton = findViewById(R.id.fab_addButton);
        dataBasehalper = new DataBasehalper(MainActivity.this);
        noteModelList = dataBasehalper.getAllNotes();
        rv_notesHolder = findViewById(R.id.rv_notesHolder);

        // for making hint on the search view.
        sv_notesSearcher.setQueryHint("Search notes");

        // initializes the recycler view adapter and layout manager.
        layoutManager = new LinearLayoutManager(MainActivity.this);
        rv_notesHolder.setLayoutManager(layoutManager);
        updateAdapter(noteModelList);

        // sets listeners for the items


        fab_addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sends the client to activity where he will add notes.
                Intent i = new Intent(MainActivity.this, AddOrEditNoteActivity.class);
                // 0 means it's for adding a new note
                i.putExtra(KEY_EDIT_OR_ADD, 0);
                startActivity(i);
            }
        });

        // set on text change listener to SearchView
        sv_notesSearcher.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // when text changes send it to searchNotesByTitle function
                // and then update the adapter to show only the items the
                // function returns
                updateAdapter(dataBasehalper.searchNotesByTitle(newText));
                return true;
            }
        });
    }
    // function that gets list of notes and puts it into the adapter.
    private void updateAdapter(List<NoteModel> list) {
        //no instance
        adapter = new RecyclerViewNoteAdapter(list, MainActivity.this);
        rv_notesHolder.setAdapter(adapter);
    }
}
