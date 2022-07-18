package com.example.noteapp;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.Models.NoteModel;
import com.example.noteapp.database.DataBasehalper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class RecyclerViewNoteAdapter extends RecyclerView.Adapter<RecyclerViewNoteAdapter.NoteViewHolder> {

    List<NoteModel> noteModelList;
    Context context;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class NoteViewHolder extends RecyclerView.ViewHolder  {
        private final TextView tv_title;
        private final TextView tv_description;
        private final TextView tv_date;
        private final FloatingActionButton fab_deleteButton;

        public NoteViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            tv_title = view.findViewById(R.id.tv_title);
            tv_description = view.findViewById(R.id.tv_desctiption);
            tv_date = view.findViewById(R.id.tv_date);
            fab_deleteButton = view.findViewById(R.id.fab_deleteButtn);


        }

        public TextView getTv_title() {
            return tv_title;
        }

        public TextView getTv_date() {
            return tv_date;
        }

        public TextView getTv_description() {
            return tv_description;
        }

        public FloatingActionButton getFab_deleteButton() {
            return fab_deleteButton;
        }

    }


    public RecyclerViewNoteAdapter(List<NoteModel> list, Context context) {
        this.noteModelList = list;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardbox_note, viewGroup, false);

        return new NoteViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(NoteViewHolder noteViewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        noteViewHolder.getTv_title().setText(noteModelList.get(position).getTitle());
        noteViewHolder.getTv_description().setText(noteModelList.get(position).getDescription());
        noteViewHolder.getTv_date().setText(noteModelList.get(position).getDate());

        // sets event listeners to the item and his widgets
        noteViewHolder.getFab_deleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // deletes the note from the database
                DataBasehalper dataBasehalper = new DataBasehalper(context);
                boolean deleted = dataBasehalper.deleteNoteRow(noteModelList.get(position));
                // checks if the item was deleted if so, then remove it from the recycler view
                if(deleted) {
                    removeFromRecyclerView(noteViewHolder.getAdapterPosition());
                }
            }
        });

        noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), AddOrEditNoteActivity.class);
                i.putExtra(MainActivity.KEY_EDIT_OR_ADD, 1);
                i.putExtra(MainActivity.KEY_NOTE_MODEL, noteModelList.get(position));
                v.getContext().startActivity(i);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return noteModelList.size();
    }

    private void removeFromRecyclerView(int position) {
        noteModelList.remove(position);
        notifyItemRemoved(position);

        notifyItemRangeChanged(position, noteModelList.size());
    }

}

