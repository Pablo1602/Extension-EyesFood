package com.example.jonsmauricio.eyesfood.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.model.AllergyList;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;

import java.util.ArrayList;
import java.util.List;


public class AdapterTabsAllergy extends ArrayAdapter<AllergyList>{

    public AdapterTabsAllergy(Context context, List<AllergyList> objects) {
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
                    R.layout.list_tab_allergy,
                    parent,
                    false);
        }

        // Referencias UI.
        TextView allergyName = convertView.findViewById(R.id.tvAllergyName);
        TextView allergy = convertView.findViewById(R.id.tvAllergy);


        AllergyList currentAllergy = getItem(position);
        allergyName.setText(currentAllergy.getAlergeno());
        if(currentAllergy.getEstado() == 0){
            allergy.setText("No");
        }
        else if(currentAllergy.getEstado() == 1){
            allergy.setText("Si");
        }
        else{
            allergy.setText("");
        }

        return convertView;
    }
}