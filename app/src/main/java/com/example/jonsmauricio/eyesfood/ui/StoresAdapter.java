package com.example.jonsmauricio.eyesfood.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Store;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoresAdapter extends ArrayAdapter<Store> {

    final String baseFotoTienda = EyesFoodApi.BASE_URL_ADMIN+"uploads/tiendas/";

    public StoresAdapter(Context context, List<Store> objects) {
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
                    R.layout.list_stores_item,
                    parent,
                    false);
        }

        // Referencias UI.
        ImageView avatar = convertView.findViewById(R.id.ivStoresAvatar);
        TextView name = convertView.findViewById(R.id.tvStoresName);
        TextView page = convertView.findViewById(R.id.tvStoresPage);
        TextView phone = convertView.findViewById(R.id.tvStoresPhone);
        RatingBar ratingBar = convertView.findViewById(R.id.rbStoresRating);

        // Tienda actual.
        Store currentStore = getItem(position);

        // Setup.
        //Cargo avatar de usuario actual
        //Log.d("foto","La foto es: "+currentStore.getPhoto());
        if (currentStore.getPhoto()!=null){
            Picasso.with(getContext()).load(baseFotoTienda + currentStore.getPhoto()).resize(800,800).into(avatar);
        }else{
            Picasso.with(getContext()).load(baseFotoTienda + "default.jpg").resize(800,800).into(avatar);
        }

        name.setText(currentStore.getName());
        //ratingBar.setRating(currentExpert.getReputation());
        page.setText(currentStore.getWeb());
        phone.setText(currentStore.getPhone());

        return convertView;
    }
}
