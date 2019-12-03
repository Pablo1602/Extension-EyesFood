package com.example.jonsmauricio.eyesfood.data.api.model;


public class SearchResult {
    private String codigo;
    private String nombre;

    public SearchResult(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString(){
        return nombre;
    }
}
