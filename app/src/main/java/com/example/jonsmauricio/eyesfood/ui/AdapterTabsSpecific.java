package com.example.jonsmauricio.eyesfood.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.model.Measure;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;

import java.util.List;

/**
 * Created by JonásMauricio on 28-11-2017.
 */

public class AdapterTabsSpecific extends ArrayAdapter<Measure>{

    public AdapterTabsSpecific(Context context, List<Measure> objects) {
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
                    R.layout.list_tab_specific,
                    parent,
                    false);
        }

        // Referencias UI.
        TextView measureName = convertView.findViewById(R.id.tvSpecificMeasureName);
        TextView measure = convertView.findViewById(R.id.tvSpecificMeasure);
        TextView date = convertView.findViewById(R.id.tvSpecificMeasureDate);

        if(position == 0){
            measureName.setText("Última hemoglobina glicolisada:");
            if (getItem(0).getId() > 0) {
                measure.setText(String.valueOf(getItem(0).getMeasure())+"%");
                date.setVisibility(View.VISIBLE);
                date.setText(getItem(0).getDate());
            } else {
                measure.setText("No has introducido este dato");
                date.setVisibility(View.GONE);
            }
        }
        else if(position == 1){
            measureName.setText("Última glucosa pre-prandial:");
            if (getItem(1).getId() > 0) {
                measure.setText(String.valueOf(getItem(1).getMeasure())+"mg/dl");
                date.setVisibility(View.VISIBLE);
                date.setText(getItem(1).getDate());
            } else {
                measure.setText("No has introducido este dato");
                date.setVisibility(View.GONE);
            }
        }
        else if(position == 2){
            measureName.setText("Última glucosa post-prandial:");
            if (getItem(2).getId() > 0) {
                measure.setText(String.valueOf(getItem(2).getMeasure())+"mg/dl");
                date.setVisibility(View.VISIBLE);
                date.setText(getItem(2).getDate());
            } else {
                measure.setText("No has introducido este dato");
                date.setVisibility(View.GONE);
            }
        }
        else if(position == 3){
            measureName.setText("Última presión arterial:");
            if (getItem(3).getId() > 0) {
                measure.setText(String.valueOf(getItem(3).getMeasure())+"mmHg");
                date.setVisibility(View.VISIBLE);
                date.setText(getItem(3).getDate());
            } else {
                measure.setText("No has introducido este dato");
                date.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
}
