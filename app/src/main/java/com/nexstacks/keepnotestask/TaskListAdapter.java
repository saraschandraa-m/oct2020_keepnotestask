package com.nexstacks.keepnotestask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import model.Items;
import model.Task;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListHolder> {

    private Context context;
    private ArrayList<Task> tasks;

    public TaskListAdapter(Context context, ArrayList<Task> tasks){
        this.tasks = tasks;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.cell_task, parent, false);
//        TaskListHolder holder = new TaskListHolder(view);
//        return holder;

        return new TaskListHolder(LayoutInflater.from(context).inflate(R.layout.cell_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListHolder holder, int position) {
        Task item = tasks.get(position);

        holder.mTvTaskTitle.setText(item.taskTitle);

        ArrayList<Items> itemsList = Items.convertJSONArrayStringToArrayList(item.taskItems);

        holder.mLlTaskItems.removeAllViews();

        for(Items value : itemsList){
            View view = LayoutInflater.from(context).inflate(R.layout.cell_items_view, null);
            TextView mTvItemName = view.findViewById(R.id.tv_view_item);
            CheckBox mChItem = view.findViewById(R.id.ch_view_item);

            mTvItemName.setText(value.itemName);

            holder.mLlTaskItems.addView(view);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class TaskListHolder extends RecyclerView.ViewHolder{

        private TextView mTvTaskTitle;
        private LinearLayout mLlTaskItems;

        public TaskListHolder(@NonNull View itemView) {
            super(itemView);

            mTvTaskTitle = itemView.findViewById(R.id.tv_task_title);
            mLlTaskItems = itemView.findViewById(R.id.ll_view_items);
        }
    }
}
