package io.github.untactorder.androidclient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.untactorder.R;
import io.github.untactorder.data.*;

import java.io.Serializable;
import java.util.Map;


public class MenuSelectActivity extends AppCompatActivity {
    private static String TAG = "MenuSelectActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_select);

        RecyclerView menuListView = findViewById(R.id.recyclerView);
        RecyclerView menuGroupView = findViewById(R.id.recyclerView1);

        LinearLayoutManager menuListLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager menuGroupLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        menuListView.setLayoutManager(menuListLayoutManager);
        menuGroupView.setLayoutManager(menuGroupLayoutManager);

        MenuAdapter menuAdapter = new MenuAdapter();
        menuListView.setAdapter(menuAdapter);
        menuGroupView.setAdapter(new MenuGroupAdapter(menuAdapter));
    }

    public void onProceedOrderButtonClicked(View v) {
        Log.d(TAG, "onProceedOrderButtonClicked");
        Map<String, String> orderMap = MenuGroupAdapter.makeOrderMap();
        Intent menuIntent = new Intent(this, MainActivity2.class);
        menuIntent.putExtra("orderMap", (Serializable) orderMap);
        setResult(RESULT_OK, menuIntent);
        super.finish();
    }
}
