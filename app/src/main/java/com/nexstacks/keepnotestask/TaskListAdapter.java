package com.nexstacks.keepnotestask;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import model.Items;
import model.Task;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListHolder> {

    private Context context;
    private ArrayList<Task> tasks;

    private TaskListClickListener listener;

    private ArrayList<Integer> selectedPositions; // [1,2]


    // [2, 3, 0, 1]

    public TaskListAdapter(Context context, ArrayList<Task> tasks) {
        this.tasks = tasks;
        this.context = context;
        this.selectedPositions = new ArrayList<>();
    }

    public void setListener(TaskListClickListener listener) {
        this.listener = listener;
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
    public void onBindViewHolder(@NonNull TaskListHolder holder, final int position) {
        final Task item = tasks.get(position);

        if(selectedPositions.contains(position)){
            holder.mCdRoot.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.bg_rect_selected, null));
        }else{
            holder.mCdRoot.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.bg_rect_unselected, null));
        }

        holder.mTvTaskTitle.setText(item.taskTitle);

        ArrayList<Items> itemsList = Items.convertJSONArrayStringToArrayList(item.taskItems);

        holder.mCdRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onTaskClicked(item);
                }
            }
        });

        holder.mCdRoot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(selectedPositions.contains(position)) {
                    selectedPositions.remove(Integer.valueOf(position));
//                    selectedPositions.remove(position);
                }else{
                    selectedPositions.add(position);
                }

                if(listener != null){
                    listener.onMultiSelectClicked(selectedPositions);
                }
                notifyDataSetChanged();
                return true;
            }
        });

        holder.mLlTaskItems.removeAllViews();

        for (final Items value : itemsList) {
            View view = LayoutInflater.from(context).inflate(R.layout.cell_items_view, null);
            TextView mTvItemName = view.findViewById(R.id.tv_view_item);
            CheckBox mChItem = view.findViewById(R.id.ch_view_item);

            mTvItemName.setText(value.itemName);
            mChItem.setChecked(value.isChecked);



            if (value.isChecked) {
                mTvItemName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                //notifyDataSetChanged(); refresh the adapter internally
            }

            mChItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (listener != null) {
                        listener.onItemUpdateClicked(value, item, isChecked);
                    }
                }
            });

            holder.mLlTaskItems.addView(view);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class TaskListHolder extends RecyclerView.ViewHolder {

        private TextView mTvTaskTitle;
        private LinearLayout mLlTaskItems;
        private CardView mCdRoot;

        public TaskListHolder(@NonNull View itemView) {
            super(itemView);

            mTvTaskTitle = itemView.findViewById(R.id.tv_task_title);
            mLlTaskItems = itemView.findViewById(R.id.ll_view_items);
            mCdRoot = itemView.findViewById(R.id.cd_root);
        }
    }

    public interface TaskListClickListener {
        void onItemUpdateClicked(Items updatedItem, Task task, boolean checkedValue);

        void onTaskClicked(Task task);

        void onMultiSelectClicked(ArrayList<Integer> selectedPositions);
    }
}
