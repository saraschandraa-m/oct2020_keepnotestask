package com.nexstacks.keepnotestask;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import model.Items;
import model.Task;

public class MainActivity extends AppCompatActivity {

    private EditText mEtTitle;
    private Button btnAddItem;
    private Button btnAddTask;
    private LinearLayout mLlDynamicLayout;
    private int itemID = 0;

    private ArrayList<Items> taskItems;

    private DatabaseHelper dbHelper;

    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEtTitle = findViewById(R.id.et_task_title);
        btnAddItem = findViewById(R.id.btn_add_item);
        btnAddTask = findViewById(R.id.btn_add_task);
        mLlDynamicLayout = findViewById(R.id.ll_dynamic_layouts);

        btnAddTask.setEnabled(false);

        taskItems = new ArrayList<>();
        dbHelper = new DatabaseHelper(MainActivity.this);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            isUpdate = data.getBoolean("ISUPDATE");
            Task updatedTask = (Task) data.getSerializable("TASK");


            if (isUpdate && updatedTask != null) {
                ArrayList<Items> updatedItems = Items.convertJSONArrayStringToArrayList(updatedTask.taskItems);
                mEtTitle.setText(updatedTask.taskTitle);
                for (int i = 0; i < updatedItems.size(); i++) {
                    View dView = LayoutInflater.from(MainActivity.this).inflate(R.layout.cell_item_entry, null);
                    final EditText mEtItem = dView.findViewById(R.id.et_item);
                    final ImageView mIvDone = dView.findViewById(R.id.iv_item_done);

                    mEtItem.setText(updatedItems.get(i).itemName);

                    mIvDone.setVisibility(View.INVISIBLE);
                    mLlDynamicLayout.addView(dView);

                    itemID = i + 1;

                    taskItems.add(updatedItems.get(i));


                }
            }
        }
    }

    public void onAddItemClicked(View view) {
        btnAddItem.setEnabled(false);

        itemID++;

        View dView = LayoutInflater.from(MainActivity.this).inflate(R.layout.cell_item_entry, null);
        final EditText mEtItem = dView.findViewById(R.id.et_item);
        final ImageView mIvDone = dView.findViewById(R.id.iv_item_done);

        mIvDone.setVisibility(View.INVISIBLE);

        mEtItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mIvDone.setVisibility(s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
            }
        });

        mIvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtItem.setEnabled(false);
                btnAddTask.setEnabled(true);
                btnAddItem.setEnabled(true);

                Items newItem = new Items();
                newItem.id = itemID;
                newItem.itemName = mEtItem.getText().toString();
                newItem.isChecked = false;

                taskItems.add(newItem);
            }
        });


        mLlDynamicLayout.addView(dView);
    }

    public void onAddTaskClicked(View view) {
        if (mEtTitle.getText().toString().isEmpty()) {
            return;
        }

        Task newTask = new Task();
        newTask.taskTitle = mEtTitle.getText().toString();
        newTask.taskItems = Items.convertArrayListToJSONArrayString(taskItems);

        if (!isUpdate) {
            dbHelper.insertDataToDatabase(dbHelper.getWritableDatabase(), newTask);
        } else {
            dbHelper.updateItemsInDatabase(dbHelper.getWritableDatabase(), newTask);
        }

        setResult(Activity.RESULT_OK);
        finish();
    }
}
