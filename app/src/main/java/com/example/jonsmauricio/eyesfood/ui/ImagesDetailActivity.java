package com.example.jonsmauricio.eyesfood.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;
import com.example.jonsmauricio.eyesfood.data.api.model.FoodImage;
import com.example.jonsmauricio.eyesfood.data.api.model.Product;

import java.util.ArrayList;

public class ImagesDetailActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout sliderDotsPanel;
    private int dotsCount;
    private ImageView[] dots;
    private int indiceActual;
    ArrayList<FoodImage> listaImagenes = new ArrayList<FoodImage>();
    private Food Alimento;
    private int MeGusta;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.vpViewPager);
        sliderDotsPanel = (LinearLayout) findViewById((R.id.llSliderDotsPanel));

        Intent i = getIntent();
        Bundle args = i.getBundleExtra("BUNDLE");
        listaImagenes = (ArrayList<FoodImage>) args.getSerializable("Imagenes");
        Bundle b = i.getExtras();
        if(b != null){
            indiceActual = (int) b.get("indice");
            Alimento = (Food) b.get("Alimento");
            product = (Product) b.get("Product");
            MeGusta = (int) b.get("MeGusta");
            setTitle(product.getProduct_name());
        }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, listaImagenes);
        viewPager.setAdapter(viewPagerAdapter);
        dotsCount = viewPagerAdapter.getCount();
        dots = new ImageView[dotsCount];
        int indice;
        for(indice=0; indice<dotsCount; indice++){
            dots[indice] = new ImageView(this);
            dots[indice].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);
            sliderDotsPanel.addView(dots[indice], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int i;
                for(i=0; i<dotsCount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(indiceActual);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, ImagesActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("Imagenes",listaImagenes);
                intent.putExtra("BUNDLE",args);

                intent.putExtra("Alimento", Alimento);
                intent.putExtra("Product", product);
                intent.putExtra("MeGusta", MeGusta);
                startActivity(intent);
                return(true);
        }

        return(super.onOptionsItemSelected(item));
    }
}
