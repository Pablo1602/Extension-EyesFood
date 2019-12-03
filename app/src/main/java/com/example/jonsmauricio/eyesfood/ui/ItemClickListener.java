package com.example.jonsmauricio.eyesfood.ui;

import android.view.View;

/*
    Interfaz para implementar el click en los items de un recycler view
*/
public interface ItemClickListener {
    void onClick(View view, int position);
}
