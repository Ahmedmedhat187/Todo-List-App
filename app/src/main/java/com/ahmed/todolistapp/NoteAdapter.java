package com.ahmed.todolistapp;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ahmed.todolistapp.database.NoteModel;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    private static final String DATE_FORMAT = "dd/MM/yyy";

    final private ItemClickListener mItemClickListener;
    private Context mContext;
    private List<NoteModel> mNoteEntries;

    public NoteAdapter(Context context, ItemClickListener listener){
        mContext = context;
        mItemClickListener = listener;
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.note_item, viewGroup, false);
        NoteViewHolder noteViewHolder = new NoteViewHolder(view);
        return noteViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        String description =  mNoteEntries.get(position).getDescription();
        int priority       =  mNoteEntries.get(position).getPriority();
        int id             =  mNoteEntries.get(position).getId();
        String updatedAt   = dateFormat.format( mNoteEntries.get(position).getUpdatedAt());

        holder.noteID.setText(id + "");
        holder.noteDescriptionView.setText(description);
        holder.updatedAtView.setText(updatedAt);
        holder.priorityView.setText(priority + "");

        GradientDrawable priorityCircle = (GradientDrawable) holder.priorityView.getBackground();
        int priorityColor = getPriorityColor(priority);
        priorityCircle.setColor(priorityColor);
    }



    private int getPriorityColor(int priority) {
        int priorityColor = 0;

        switch (priority) {
            case 1:
                priorityColor = ContextCompat.getColor(mContext, R.color.materialRed);
                break;
            case 2:
                priorityColor = ContextCompat.getColor(mContext, R.color.materialOrange);
                break;
            case 3:
                priorityColor = ContextCompat.getColor(mContext, R.color.materialYellow);
                break;
            default:
                break;
        }
        return priorityColor;
    }



    @Override
    public int getItemCount() {
        if (mNoteEntries == null) {
            return 0;
        }
        return mNoteEntries.size();
    }


    public void setTasks(List<NoteModel> taskEntries) {
        mNoteEntries = taskEntries;
        notifyDataSetChanged();
    }


    public List<NoteModel> getNoteEntries() {
        return mNoteEntries;
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }


    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView noteDescriptionView;
        TextView updatedAtView;
        TextView priorityView;
        TextView noteID;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteID = itemView.findViewById(R.id.taskID);
            noteDescriptionView = itemView.findViewById(R.id.taskDescription);
            updatedAtView = itemView.findViewById(R.id.taskUpdatedAt);
            priorityView = itemView.findViewById(R.id.priorityTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int elementId = mNoteEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}
