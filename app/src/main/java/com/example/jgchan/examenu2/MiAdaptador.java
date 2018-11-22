package com.example.jgchan.examenu2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by jgchan on 16/04/18.
 */

public class MiAdaptador extends CursorAdapter {
    private LayoutInflater inflater; //CREAMOS UN LAYOUTINFLATER ESTO NOS PERMITIRÁ DAR DE ALTA LOS CONTROLES QUE ESTAN EN CADA RENGLON
    private TextView tvConcepto, tvFecha, tvImporte; //CREAMOS LOS TEXVIEW LOS CUALES SE ENCONTRARÁN EN CADA RENGLON

    public MiAdaptador(Context context, Cursor c) {
        super(context, c,false);
    } //EL CONSTRUCTOR DEL ADAPTADOR POR DEFECTO.


    //METODO QUE TE PERMITE CREAR UN NUEVO ELEMENTO DEL RENGLON
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View vista=null; //CREAMOS UNA VISTA

        //CREAMOS UN SERVICIO LayoutInflater PARA INFLAR UN CONTROL XML, EN ESTE CASO EL ARCHIVO renglon.xml
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Y A LA VISTA LE ASGINAMOS ESE ARCHIVO PARA QUE SE PUEDA MANIPULAR
        //PARA CREAR EL ARCHIVO renglon.xml SELECCIONAS LA CARPETA res LE DAS CLIC DERECHO, NEW > XML > Layout xml file
        vista = inflater.inflate(R.layout.renglon,null);

        //NOTA para ver el archivo renglon.xml dale clic derecho en la palabra morada y GO TO > Declaretion.

        return vista; //Retorna la vista que se asigno
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //ACCEDER A LOS CAMPOS,  INFLAMOS LOS CONTROLES CON AYUDA DEL OBJETO VIEW
        tvFecha = (TextView) view.findViewById(R.id.tvFecha);
        tvConcepto = (TextView) view.findViewById(R.id.tvNombre);
        tvImporte = (TextView) view.findViewById(R.id.tvPrecio);

        //Y EN CADA CONTROL LES SETEAMOS LOS DATOS CON LA AYDUA DEL CURSOR DONDE EL cursor.getColumnIndex() DENTRO DE LOS PARENTESIS
        // ES EL NOMBRE DE CADA COLUMNA EN LA TABLA DE TU BASE DE DATOS EL CUAL obtendremos el VALOR QUE LE QUEREMOS ASIGNAR

        tvFecha.setText(cursor.getString(cursor.getColumnIndex("fecha")));
        tvConcepto.setText(cursor.getString(cursor.getColumnIndex("concepto")));
        tvImporte.setText(cursor.getString(cursor.getColumnIndex("importe")));

    }


}
