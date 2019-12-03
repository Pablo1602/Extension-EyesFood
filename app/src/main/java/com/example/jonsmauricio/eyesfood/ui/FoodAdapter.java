package com.example.jonsmauricio.eyesfood.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.model.FoodStore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter extends ArrayAdapter<FoodStore> {

    public FoodAdapter(Context context, List<FoodStore> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.item_row,
                    parent,
                    false);
        }

        // Referencias UI.
        ImageView avatar = convertView.findViewById(R.id.iv_history_image);
        TextView name = convertView.findViewById(R.id.tv_history_name);
        TextView date = convertView.findViewById(R.id.tv_history_date);
        RatingBar rating = convertView.findViewById(R.id.rbHistoryRating);
        /*TextView phone = convertView.findViewById(R.id.tvStoresPhone);
        RatingBar ratingBar = convertView.findViewById(R.id.rbStoresRating);*/

        // Alimento Actual
        FoodStore currentFood = getItem(position);

        // Setup.
        //Cargo avatar de usuario actual
        //Log.d("foto","La foto es: "+currentStore.getPhoto());
        Picasso.with(getContext()).load(currentFood.getPhoto()).into(avatar);

        name.setText(currentFood.getFoodName());
        rating.setRating(currentFood.getFoodHazard());
        date.setText("Precio: "+currentFood.getPrice());
        //name.setText(currentFood.getWeb());
        //phone.setText(currentStore.getPhone());

        return convertView;
    }
}
