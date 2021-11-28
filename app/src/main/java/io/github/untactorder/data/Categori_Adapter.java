package io.github.untactorder.data;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.github.untactorder.androidclient.R;

import java.util.ArrayList;


public class Categori_Adapter extends RecyclerView.Adapter<Categori_Adapter.ViewHolder> implements OnCategoriClickListener {

    ArrayList<Categori> items = new ArrayList<>();

    OnCategoriClickListener listener;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.activity_categori_item, viewGroup,false);
        return new ViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Categori item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Categori item){
        items.add(item);
    }

    public Categori getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Categori item) {
        items.set(position, item);
    }

    public void setOnItemClickListener(OnCategoriClickListener listener) {
        this.listener = listener;
    }
    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        Drawable TRIANGLE_REVERSED = null;
        Drawable TRIANGLE = null;
        TextView categori;
        TextView direction;

        public ViewHolder(View view, final OnCategoriClickListener listner){
            super(view);

            categori = view.findViewById(R.id.categori);
            direction = view.findViewById(R.id.direction);

            TRIANGLE = ContextCompat.getDrawable(view.getContext(), R.drawable.shape_triangle);
            TRIANGLE_REVERSED = ContextCompat.getDrawable(view.getContext(), R.drawable.shape_reverse_triangle);
            /*

            view.findViewById(R.id.cat).setOnClickListener(v -> {
                if (v.)
                v.setBackground(TRIANGLE_REVERSED);
            });*/
        }

        public void setItem(Categori item){
            categori.setText(item.getCategori());
            if (item.isSelected()) {
                direction.setBackground(TRIANGLE_REVERSED);
            } else {
                direction.setBackground(TRIANGLE);
            }
        }
    }
}
