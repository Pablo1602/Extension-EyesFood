package com.example.jonsmauricio.eyesfood.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.model.Measure;

import java.util.List;

public class AdapterTabsEdit extends ArrayAdapter<Measure>{

    public AdapterTabsEdit(Context context, List<Measure> objects) {
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
                    R.layout.list_edit_measure_item,
                    parent,
                    false);
        }

        // Referencias UI.
        TextView measure = convertView.findViewById(R.id.tvEditMeasure);
        TextView date = convertView.findViewById(R.id.tvEditMeasureDate);

        // TODO: 30-11-2017 Poner la unidad de medida
        measure.setText(String.valueOf(getItem(position).getMeasure()));
        date.setText(getItem(position).getDate());

        return convertView;
    }
}