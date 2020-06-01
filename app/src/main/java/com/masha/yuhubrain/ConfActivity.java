package com.masha.yuhubrain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.masha.yuhubrain.data.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ExpandableListView listView;
    private CheckBox confCheckBox;
    private Button confButton;
    private User user;
    private String password;
    private String[] groupsArray = new String[]{"Группа 1", "Группа 2", "Группа 3"};
    private String[] group1ElArray = new String[]{"Элемент 1", "Элемент 2", "Элемент 3"};
    private String[] group2ElArray = new String[]{"Элемент 1", "Элемент 2", "Элемент 3"};
    private String[] group3ElArray = new String[]{"Элемент 1", "Элемент 2", "Элемент 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf);
        user = (User) getIntent().getSerializableExtra("user");
        password = getIntent().getStringExtra("password");
        confCheckBox = findViewById(R.id.confCheckBox);
        confButton = findViewById(R.id.confRegButton);
        listView = findViewById(R.id.confexpListView);
        confButton.setOnClickListener(this);
        confCheckBox.setOnCheckedChangeListener(this);
        confButton.setEnabled(false);
        Map<String, String> map;
        ArrayList<Map<String, String>> groupDataList = new ArrayList<>();
        for(String group : groupsArray){
            map = new HashMap<>();
            map.put("groupName",group);
            groupDataList.add(map);
        }
        String groupFrom[] = new String[] {"groupName"};
        int[] groupTo = new int[] {android.R.id.text1};
        ArrayList<ArrayList<Map<String, String>>> childDataList = new ArrayList();
        ArrayList<Map<String, String>> childDataItemList = new ArrayList<>();
        for(String element : group1ElArray){
            map = new HashMap<>();
            map.put("elemName", element);
            childDataItemList.add(map);
        }
        childDataList.add(childDataItemList);
        childDataItemList = new ArrayList<>();
        for(String element : group2ElArray){
            map = new HashMap<>();
            map.put("elemName", element);
            childDataItemList.add(map);
        }
        childDataList.add(childDataItemList);
        childDataItemList = new ArrayList<>();
        for(String element : group3ElArray){
            map = new HashMap<>();
            map.put("elemName", element);
            childDataItemList.add(map);
        }
        childDataList.add(childDataItemList);
        String childFrom[] = new String[]{"elemName"};
        int childTo[] = new int[]{android.R.id.text1};
        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(this, groupDataList, android.R.layout.simple_expandable_list_item_1, groupFrom, groupTo, childDataList, android.R.layout.simple_list_item_1, childFrom, childTo);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("user",user);
        intent.putExtra("password",password);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        confButton.setEnabled(isChecked);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
