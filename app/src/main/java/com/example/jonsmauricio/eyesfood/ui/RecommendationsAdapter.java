package com.example.jonsmauricio.eyesfood.ui;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Additive;
import com.example.jonsmauricio.eyesfood.data.api.model.Recommendation;
import com.example.jonsmauricio.eyesfood.data.api.model.ShortFood;
import com.squareup.picasso.Picasso;

import java.util.List;


/*
    Clase encargada de llenar los card views
*/
public class RecommendationsAdapter extends RecyclerView.Adapter<RecommendationsAdapter.RecommendationsViewHolder> {

    private List<Recommendation> items;

    public class RecommendationsViewHolder extends RecyclerView.ViewHolder{
        // Campos respectivos de un item
        public TextView title;
        public TextView content;

        public RecommendationsViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.tvRecommendationsTitle);
            content = v.findViewById(R.id.tvRecommendationsContent);
        }
    }

    public RecommendationsAdapter(List<Recommendation> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RecommendationsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_recommendation, viewGroup, false);
        return new RecommendationsViewHolder(v);
    }

    //Llena los campos del cardview
    @Override
    public void onBindViewHolder(RecommendationsViewHolder viewHolder, int i) {
        viewHolder.title.append(String.valueOf(i+1));
        viewHolder.content.setText(items.get(i).getRecommendation());
    }
}