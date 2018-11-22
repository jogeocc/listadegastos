package com.example.jgchan.examenu2;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AgregarActivity extends AppCompatActivity {

    EditText concepto,fecha,importe; //damos de alta los controles en este caso los edittext para que el usuario ingrese sus datos
    Button btnAgregar; //El boton ingresar el cual es el que va a ejecutar la ccion al momento de darle clic
    SQLiteDatabase db = null; // el SQLiteDatabase que nos permitirá hacer la conexion a la base de datos


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        //inflamos cada control de nuestra pantalla
        concepto = (EditText)findViewById(R.id.edtConcepto);
        fecha = (EditText)findViewById(R.id.edtFecha);
        importe = (EditText)findViewById(R.id.edtImporte);
        btnAgregar=(Button)findViewById(R.id.btnAgregar);


        //al boton agregar le seteamos el evento setOnclickListener para que escuche cuando es pulsado
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si es pulsado ejecuta el metodo agregar.
                agregar();

            }
        });
    }


    //El metodo agregar este metodo te permite obtener los datos de los controles y ejecutar la consulta para insertarlos
    //A la base de datos

    private void agregar() {

        String Snombre,Simporte,Sfecha; //Creamos 3 variables de tipo cadena ya que son 3 datos que le pedimos al usuario


        //Recuperamos estos datos de cada control
            Snombre = concepto.getText().toString();
            Simporte = importe.getText().toString();
            Sfecha= fecha.getText().toString();
        //------------------------------------


        //Creamos la conexion a la base de datos
            db = this.openOrCreateDatabase("examenU2.db", MODE_PRIVATE, null);

        //Creamos el insertar muy importante si son datos de tipo cadena en el inserte deben contener '' si son numeros no llevan
        //Ejemplo VALUES( '12-06-2018','coche',450000) es por eso en la concatenacion tienen '"+sDato+"'
        String insertar="INSERT INTO gastos(fecha,concepto,importe)" +
                " VALUES ('"+Sfecha+"','"+Snombre+"',"+Simporte+");";

        db.execSQL(insertar); //EJECUTAMOS LA SENTENCIA
        db.close(); //CERRAMOS BD


        //CREAMOS UN INTENT QUE haga saber a la primera activity que termino exitosamente y se agrego para ello
        Intent intentVacio = getIntent(); //Creamos un intent vacio
        setResult(RESULT_OK,intentVacio); // de ese intent le decimos que termino correctmente con RESULT_OK
        finish(); //y finalizamos.


        //mensaje("El gasto '"+Snombre+"' se agrego con éxito ");

    }

    public void mensaje(String respuesta){
        AlertDialog.Builder builder = new AlertDialog.Builder(AgregarActivity.this);
        builder.setTitle("¡Advertencia!")
                .setMessage(respuesta)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intentVacio = getIntent();
                        setResult(RESULT_OK,intentVacio);
                        finish();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
