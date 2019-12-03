package com.example.jonsmauricio.eyesfood.ui;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.FoodImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JonásMauricio on 02-11-2017.
 */

public class ImagesAdapter extends BaseAdapter {
    private ArrayList<FoodImage> foodImagesList;
    //final String baseFotoAlimento = EyesFoodApi.BASE_URL+"img/uploads/";
    private Context context;

    public ImagesAdapter(Context context, ArrayList<FoodImage> lista) {
        this.context = context;
        this.foodImagesList = lista;
    }

    @Override
    public int getCount() {
        return foodImagesList.size();
    }

    @Override
    public FoodImage getItem(int position) {
        return foodImagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return foodImagesList.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_image, viewGroup, false);
        }

        ImageView imagenAlimento = view.findViewById(R.id.ivGridImage);
        TextView footer = view.findViewById(R.id.tvImageFooter);

        final FoodImage item = getItem(position);
        Picasso.with(imagenAlimento.getContext())
                .load(item.getPath())
                .resize(800,800)
                .into(imagenAlimento);
        footer.setText(item.getFecha());

        return view;
    }

}