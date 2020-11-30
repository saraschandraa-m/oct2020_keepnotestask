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

import model.Task;

public class ViewTaskActivity extends AppCompatActivity {

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

    public void onAddNewTaskClicked(View view){
        startActivityForResult(new Intent(ViewTaskActivity.this, MainActivity.class), 913);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 913 && resultCode == Activity.RESULT_OK){
            setDataToRecycler();
        }
    }

    private void setDataToRecycler(){
        ArrayList<Task> tasks = dbHelper.getTasksFromDatabase(dbHelper.getReadableDatabase());

        TaskListAdapter adapter = new TaskListAdapter(ViewTaskActivity.this, tasks);
        mRcTasks.setAdapter(adapter);
    }
}