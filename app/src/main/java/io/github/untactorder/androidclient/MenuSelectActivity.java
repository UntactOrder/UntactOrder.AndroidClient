package io.github.untactorder.androidclient;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.untactorder.data.*;

import java.util.Map;


public class MenuSelectActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_select);

        RecyclerView menuListView = findViewById(R.id.recyclerView);
        RecyclerView menuGroupView = findViewById(R.id.recyclerView1);

        LinearLayoutManager menuListLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager menuGroupLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        menuListView.setLayoutManager(menuListLayoutManager);
        menuGroupView.setLayoutManager(menuGroupLayoutManager);

        menuListView.setAdapter(new MenuAdapter());
        menuGroupView.setAdapter(new MenuGroupAdapter());
    }
    public void onProceedOrderButtonClickde(View v) {
        Map<String, String> orderMap = MenuGroupAdapter.makeOrderMap();
        Intent menuIntent = new Intent(this,MainActivity.class);
        menuIntent.putExtra("orderMap", (Parcelable) orderMap);
        startActivity(menuIntent);
        super.finish();
    }
}
