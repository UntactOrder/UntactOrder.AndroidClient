package io.github.untactorder.androidclient.order.order;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.untactorder.androidclient.R;

public class Order_MainActivity extends AppCompatActivity {
    TextView triangle;
    TextView finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categori_item);

         triangle = findViewById(R.id.direction);
         finish = findViewById(R.id.tx_finish);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView recyclerView1 = findViewById(R.id.recyclerView1);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView1.setLayoutManager(layoutManager1);
        final MenuAdapter adapter = new MenuAdapter();
        final Categori_Adapter categori_adapter=new Categori_Adapter();

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

    }

    public void onFinish(View view){
        if(view == finish){
            //창 넘어가기
        }
    }


}