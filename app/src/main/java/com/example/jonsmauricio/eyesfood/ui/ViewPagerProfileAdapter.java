package com.example.jonsmauricio.eyesfood.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.model.Measure;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JonásMauricio on 01-12-2017.
 */

public class ViewPagerProfileAdapter extends PagerAdapter{

    private Context context;
    private LayoutInflater layoutInflater;
    private List<List<Measure>> listaDatos;

    public ViewPagerProfileAdapter(Context context, List<List<Measure>> listaDatos) {
        this.context = context;
        this.listaDatos = listaDatos;
    }

    @Override
    public int getCount() {
        return listaDatos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.content_view_pager_profile, null);
        GraphView graph = (GraphView) view.findViewById(R.id.gvProfileGraph);


            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                if(listaDatos.get(position).size()>0) {
                    int tamanoLista = listaDatos.get(position).size();
                    final DataPoint[] data = new DataPoint[tamanoLista];
                    for (int i = 0; i < tamanoLista; i++) {
                        data[i] = new DataPoint(sdf.parse(listaDatos.get(position).get(tamanoLista - i - 1).getDate()),
                                listaDatos.get(position).get(tamanoLista - i - 1).getMeasure());
                    }

                    // you can directly pass Date objects to DataPoint-Constructor
                    // this will convert the Date to double via Date#getTime()
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(data);

                    series.setDrawBackground(true);
                    series.setDrawDataPoints(true);
                    series.setDataPointsRadius(10);
                    series.setAnimated(true);

                    series.setOnDataPointTapListener(new OnDataPointTapListener() {
                        @Override
                        public void onTap(Series series, DataPointInterface dataPoint) {
                            Toast.makeText(context, String.valueOf(dataPoint.getY()), Toast.LENGTH_SHORT).show();
                        }
                    });

                    graph.addSeries(series);
                }

                // set date label formatter
                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context));
                graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 3 because of the space

                // set manual x bounds to have nice steps
                graph.getViewport().setXAxisBoundsManual(true);
                GridLabelRenderer grid = graph.getGridLabelRenderer();
                grid.setPadding(48);
                graph.setTitleTextSize(48);

                switch (position){
                    case 0:
                        graph.setTitle("Peso");
                        grid.setVerticalAxisTitle("Peso (kg)");
                        break;
                    case 1:
                        graph.setTitle("Grasa corporal");
                        grid.setVerticalAxisTitle("% de grasa");
                        break;
                    case 2:
                        graph.setTitle("Medida de cintura");
                        grid.setVerticalAxisTitle("Cintura (cm)");
                        break;
                    case 3:
                        graph.setTitle("A1C");
                        grid.setVerticalAxisTitle("% de hemoglobina glicolisada");
                        break;
                    case 4:
                        graph.setTitle("Glucosa preprandial");
                        grid.setVerticalAxisTitle("Glucosa (mg/dl)");
                        break;
                    case 5:
                        graph.setTitle("Glucosa postprandial");
                        grid.setVerticalAxisTitle("Glucosa (mg/dl)");
                        break;
                    case 6:
                        graph.setTitle("Presión arterial");
                        grid.setVerticalAxisTitle("Presión (mmHg)");
                        break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

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
