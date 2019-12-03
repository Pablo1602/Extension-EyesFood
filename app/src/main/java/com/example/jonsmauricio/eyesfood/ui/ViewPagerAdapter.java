package com.example.jonsmauricio.eyesfood.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.FoodImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by JonásMauricio on 02-11-2017.
 */

public class ViewPagerAdapter extends PagerAdapter{

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<FoodImage> imagenes;
    final String baseFotoAlimento = EyesFoodApi.BASE_URL+"img/uploads/";

    public ViewPagerAdapter(Context context, ArrayList<FoodImage> lista) {
        this.context = context;
        this.imagenes = lista;
    }

    @Override
    public int getCount() {
        return imagenes.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.content_view_pager, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imagen_extendida);

        Picasso.with(imageView.getContext())
                .load(baseFotoAlimento + imagenes.get(position).getPath())
                .into(imageView);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
