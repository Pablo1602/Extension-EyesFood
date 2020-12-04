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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JonásMauricio on 28-11-2017.
 */

public class AdapterTabsGeneral extends ArrayAdapter<Measure>{

    public AdapterTabsGeneral(Context context, List<Measure> objects) {
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
                    R.layout.list_tab_general,
                    parent,
                    false);
        }

        // Referencias UI.
        TextView measureName = convertView.findViewById(R.id.tvGeneralMeasureName);
        TextView measure = convertView.findViewById(R.id.tvGeneralMeasure);
        TextView date = convertView.findViewById(R.id.tvGeneralMeasureDate);

        if(position == 0){
            measureName.setText("Estatura:");
            //if(SessionPrefs.get(getContext()).getUserHeight().equals("0")){
            if(SessionPrefs.get(getContext()).getUserHeight() == null){
                measure.setText("Dato no introducido");
            }
            else{
                measure.setText(SessionPrefs.get(getContext()).getUserHeight()+"cm");
            }
            date.setVisibility(View.GONE);
        }
        else if(position == 1){
            measureName.setText("Último peso:");
            if (getItem(position).getId() > 0) {
                measure.setText(String.valueOf(getItem(position).getMeasure())+"kg");
                date.setVisibility(View.VISIBLE);
                date.setText(getItem(position).getDate());
            } else {
                measure.setText("Dato no introducido");
                date.setVisibility(View.GONE);
            }
        }
        else if(position == 2){
            measureName.setText("Último IMC:");
            Log.d("TAB","getItem:"+getItem(1).getId());
            Log.d("TAB","getUserHeight:"+SessionPrefs.get(getContext()).getUserHeight());
            if (getItem(1).getId() > 0 && SessionPrefs.get(getContext()).getUserHeight() != null && !SessionPrefs.get(getContext()).getUserHeight().equals("")) {
                String IMC = calculateIMC(SessionPrefs.get(getContext()).getUserHeight(), getItem(1).getMeasure());
                measure.setText(IMC);
                date.setVisibility(View.VISIBLE);
                date.setText(getItem(1).getDate());
            } else {
                measure.setText("No has introducido los datos necesarios");
                date.setVisibility(View.GONE);
            }
        }
        else if(position == 3){
            measureName.setText("Último % de grasa:");
            if (getItem(position).getId() > 0) {
                measure.setText(String.valueOf(getItem(position).getMeasure())+"%");
                date.setVisibility(View.VISIBLE);
                date.setText(getItem(position).getDate());
            } else {
                measure.setText("Dato no introducido");
                date.setVisibility(View.GONE);
            }
        }
        else if(position == 4){
            measureName.setText("Última medida de cintura:");
            if (getItem(position).getId() > 0) {
                measure.setText(String.valueOf(getItem(position).getMeasure())+"cm");
                date.setVisibility(View.VISIBLE);
                date.setText(getItem(position).getDate());
            } else {
                measure.setText("Dato no introducido");
                date.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    private String calculateIMC(String height, float weight){
        String resultado = "";

        if(height != null || height.equals("")){
            return resultado;
        }

        float fHeight = Float.parseFloat(height);

        float IMC = (weight / (fHeight * fHeight))*10000;

        if(IMC < 16){
            return resultado = String.format("%.2f", IMC) + " (Infrapeso: Delgadez severa)";
        }
        else if(IMC >= 16 && IMC < 17){
            return resultado = String.format("%.2f", IMC) + " (Infrapeso: Delgadez moderada)";
        }
        else if(IMC >= 17 && IMC < 18.50){
            return resultado = String.format("%.2f", IMC) + " (Infrapeso: Delgadez aceptable)";
        }
        else if(IMC >= 18.50 && IMC < 25){
            return resultado = String.format("%.2f", IMC) + " (Peso normal)";
        }
        else if(IMC >= 25 && IMC < 30){
            return resultado = String.format("%.2f", IMC) + " (Sobrepeso)";
        }
        else if(IMC >= 30 && IMC < 35){
            return resultado = String.format("%.2f", IMC) + " (Obeso: Tipo I)";
        }
        else if(IMC >= 35 && IMC < 40){
            return resultado = String.format("%.2f", IMC) + " (Obeso: Tipo II)";
        }
        else if(IMC >= 40){
            return resultado = String.format("%.2f", IMC) + " (Obeso: Tipo III)";
        }

        return resultado;
    }
}
