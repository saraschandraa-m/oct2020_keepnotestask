package com.nexstacks.keepnotestask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import model.Items;
import model.Task;

public class ViewTaskActivity extends AppCompatActivity implements TaskListAdapter.TaskListClickListener {

    private RecyclerView mRcTasks;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        mRcTasks = findViewById(R.id.rc_tasks);
        mRcTasks.setLayoutManager(new GridLayoutManager(this, 2));

        dbHelper = new DatabaseHelper(ViewTaskActivity.this);

        setDataToRecycler();
    }

    public void onAddNewTaskClicked(View view) {
        startActivityForResult(new Intent(ViewTaskActivity.this, MainActivity.class), 913);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 913 && resultCode == Activity.RESULT_OK) {
//            setDataToRecycler();
//        }else if(requestCode == 432 && resultCode == Activity.RESULT_OK){
//            setDataToRecycler();
//        }

        if(resultCode == Activity.RESULT_OK && (requestCode == 913 || requestCode == 432)){
            setDataToRecycler();
        }
    }

    private void setDataToRecycler() {
        ArrayList<Task> tasks = dbHelper.getTasksFromDatabase(dbHelper.getReadableDatabase());

        TaskListAdapter adapter = new TaskListAdapter(ViewTaskActivity.this, tasks);
        adapter.setListener(this);
        mRcTasks.setAdapter(adapter);
    }

    @Override
    public void onItemUpdateClicked(Items updatedItem, Task task, boolean checkedValue) {

        ArrayList<Items> oldItemsValues = Items.convertJSONArrayStringToArrayList(task.taskItems);

        if (oldItemsValues != null && oldItemsValues.size() > 0) {
            for (Items oldItem : oldItemsValues) {
                if (oldItem.id == updatedItem.id) {
                    oldItem.isChecked = checkedValue;

                    //u can remove either by value or by position. Always safest method is by value;
//                    if(checkedValue){
//                        oldItemsValues.remove(oldItem);
//                    }
                }


            }

        }



        Task updatedTask = new Task();
        updatedTask.id = task.id;
        updatedTask.taskTitle = task.taskTitle;
        updatedTask.taskItems = Items.convertArrayListToJSONArrayString(oldItemsValues);

        dbHelper.updateItemsInDatabase(dbHelper.getWritableDatabase(), updatedTask);

        setDataToRecycler();
    }

    @Override
    public void onTaskClicked(Task task) {
        startActivityForResult(new Intent(ViewTaskActivity.this, MainActivity.class)
                        .putExtra("TASK", task)
                        .putExtra("ISUPDATE", true),
                432);
    }
}