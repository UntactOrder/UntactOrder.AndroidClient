package io.github.untactorder.androidclient;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.untactorder.data.*;

import java.util.ArrayList;
import java.util.List;

public class MenuSelectActivity extends AppCompatActivity {

    List<List> menuGroup = new ArrayList<List>();

    TextView triangle;
    TextView finish;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        triangle = findViewById(R.id.direction);
        finish = findViewById(R.id.tx_finish);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView recyclerView1 = findViewById(R.id.recyclerView1);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView1.setLayoutManager(layoutManager1);

        MenuAdapter adapter = new MenuAdapter();
        adapter.setOnItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void onItemClick(MenuAdapter.ViewHolder holder, View view, int position) {


            }
        });
        Categori_Adapter categori_adapter=new Categori_Adapter();

        recyclerView.setAdapter(adapter);
        recyclerView1.setAdapter(categori_adapter);

        /*
        ArrayList<Menu> arr = new ArrayList();
        for (Menu menu: arr) {
            adapter.addItem(menu);
        }
        arr.forEach(adapter::addItem);
*/
        categori_adapter.addItem(new Categori("추천"));
        categori_adapter.addItem(new Categori("파스타"));
        categori_adapter.addItem(new Categori("리조또"));
        categori_adapter.addItem(new Categori("스테이크"));
        categori_adapter.addItem(new Categori("음료"));
        adapter.addItem(new Menu("봉골레 파스타","12,000"));
        adapter.addItem(new Menu("새우 베이컨 필라프","13,500"));
        adapter.addItem(new Menu("마르게리따 피자","12,000"));


        Intent intent;


    }

    public void save_select_menu(){

    }


    public void finish() {


    }
}
