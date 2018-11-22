package com.example.jgchan.examenu2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase; //IMPORTAR LAS LIBRERIAS SQLite PARA GESTIONAR LAS BD
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db = null; //CREAR UN OBJETO SQLite PARA PODER HACER LA CONEXION Y MANIPULAR LA BASE DE DATOS
    Cursor cursor = null,cursor2=null; //CURSORES, PERMITEN RECUPERAR LA INFORMACION DE UNA CONSULA Y APUNTAR A LOS REGISTROS QUE ESTA CONTENGA
    ListView lista; //LISTVIEW ES EL CONTROL LO QUE NOS PERMITE DESPLEGAR EN PANTALLA LA LISTA DE ITEM'S AL USUARIO.
    TextView letrero; //TEXVIEW QUE NOS PERMITIRÁ MOSTRAR EL TOTAL DE LOS IMPORTES
    String total=""; //CADENA LA CUAL TENDRA GUARDADA EL IMPORTE TOTAL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista = (ListView)findViewById(R.id.lvDatos); //INFLAMOS EL CONTROL LISTA
        letrero=(TextView)findViewById(R.id.tvLetrero); //INFLAMOS EL CONTROL LETRERO

        //¿QUE ES INFLAR? ES EL TERMINO QUE SE LE DA CUANDO A UNA VARIABLE DE TU CODIGO SE LE ASIGNA UN CONTROL DE TU PANTALLA XML
        //ESTO TE PERMITE MANIPULAR AL CONTROL COMO SI FUERA UN OBJETO DE JAVA.


        //CON LA AYUDA DEL OBJETO db CREAMOS LA BASE DE DATO examenU2.db ESTO PERMITE CREAR Y CONECTAR A LA BASE DE DATOS
        db = this.openOrCreateDatabase("examenU2.db", MODE_PRIVATE, null);


        //CREAMOS UNA CADENA LA CUAL CONTIENE LA SINTAXIS SQL PARA CREAR UNA TABLA LLAMADA gastos
        String crearTabla="CREATE TABLE IF NOT EXISTS gastos (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," + //ESTE CAMPO ES OBLIGATORIO PARA RELLENAR LAS LISTAS
                " fecha TEXT," +
                " concepto TEXT," +
                " importe DOUBLE);";

        db.execSQL(crearTabla); //SE EJECUTA LA SINTAXTIS PARA CREAR LA BASE DE DATOS


        refrescarLista();// LLAMAMOS UN METODO EL CUAL SE ENCARGARÁ DE RECUPERAR TODOS LOS REGISTROS DE LA TABLA Y LOS DESPLEGARÁ EN LA LISTA

        db.close(); //CERRAMOS LA CONEXION
    }


    //METODO PARA ASIGNAR UN MENU A LA ACTIVITY
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //PARA crear un archivo menu es seleccionar la carpeta res > New > Android Resourse File > ahi en name es menu y en resource type es Menu
        getMenuInflater().inflate(R.menu.menu, menu); //R.menu.menu INDICA QUE EN LA CARPETA MENU ADENTRO EXISTE UN ARCHIVO MENU el cual tiene las opciones o items de las acciones que podria hacer el usuario.
        return true; //retorna true para identificar que si tiene un menu

        //NOTA : Si quieres ver el archivo menu inmediatamente colocate en la palabra menu de color morado y clic derecho Selecciona
        //GO TO > Declaretion.


    }

    //ESTE METODO TE PERMITIRÁ IDENTIFICAR QUE OPCION DE TU MENU FUE PULSADA, EN ESTE CASO TU MENU SOLO TIENE UNA OPCION (ITEM),
    //EN ESTE CASO SU ID ES agregar POR LO CUAL EL CON AYUDA DE UN SWITCH-CASE EVALUAS SI EL ITEM QUE SE PULSO ES IGUAL AL
    //ID DE TU MENU QUE SE PULSO.

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.agregar: //SI ES IGUAL AL ID DE TU MENU ENTONCES

                Intent i = new Intent(this,AgregarActivity.class); //CREA UNA NUEVA INTENCION
                //LA NUEVA INTENCION ES new (this DONDE EL this es un contexto, el contexto es quien lo esta llamando, en este caso el this simboliza que es esta clase
                //DESPUES SIGUE ,AgregarActivity.class); DONDE EL AgregarActivity.class ES LA CLASE QUE VA HACER LLAMADA POR ESA INTENCION. IMPORTANTE QUE YA DEBE ESTAR CREADA ESA ACTIVITY
                //PARA CREAR UNA ACTIVITY ES SELECCIONAR LA CARPETA app, clic derecho NEW > activity > Empty Activity


                startActivityForResult(i,1); // LANZA LA INTENCION CON ESPERA DE UN RESULTADO ES POR ESO EL METODO startActivityForResult
                //startActivityForResult se caracteriza por startActivityForResult(i donde i es la intencion que se creo antes y despues seguido de un numero
                //este numero te permite identificar que numero de id le asignaste a la nueva intencion
                break;

        }

        return true;
    }


    //ESTE METODO TE PERMITE IDENTIFICAR QUE ACTIVIDADES YA TERMINARON Y SI SE OBTUVO UN RESULTADO DESEADO
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //EN ESTE CASO SE EVALUA SI LA ACTIVITY QUE TERMINO ES LA QUE TIENE COMO ID 1 Y SI EL RESULTADO QUE SE OBTUVO
        //FUE EL CORRECTO, SI ES ASI ENTONCES EJECUTA EL METODO REFRESCAR LISTA
        if(requestCode == 1 && resultCode == RESULT_OK){
            refrescarLista(); //LLAMAMOS AL METODO REFRESCAR
        }
    }

    //METODO REFRESCAR
    //ESTE METODO SE ENCARGARA DE RELLAR LA LISTA
    private void refrescarLista() {

        total=""; //PRIMER PASO RESETEAMOS NUESTRA CADENA Que GUARDA EL VALOR DEL IMPORTE

        db = this.openOrCreateDatabase("examenU2.db", MODE_PRIVATE, null); //HACEMOS LA CONEXION A LA BD

        cursor = db.rawQuery("SELECT * FROM gastos;",null); //HACEMOS LA CONSULTA SELECT * FROM gastos PARA RECUPERAR TODOS LOS GASTOS

        lista.setAdapter(new MiAdaptador(this, cursor)); //A la lista LISTVIEW le pasamos un adaptador el cual se encargará de recibir el cursor con la informacion
                                                                // Se encargará de darle formato a cada renglon y rellenará la lista para que se puede visualizar.

        cursor2 = db.rawQuery("SELECT sum(importe) FROM gastos;",null); //PARA RECUPERAR EL IMPORTE TOTAL, CREAMOS UNA SEGUNDA CONSULTA
                                                                                        // EL CUAL CON EL METODO SUM() RECUPERAMOS EL TOTAL DE CADA IMPORTE DE TODOS LOS GASTOS
        //RECORDAR QUE LOS CURSORES GUARDAR LOS REGISTROS QUE SE RECUPERARN EN CADA CONSULTA.


        //DEL CURSOR 2 NOS MOVEMOS A SU PRIMER REGISTRO, Y SI NO ES NULO
        if(cursor2.moveToFirst())
            total += cursor2.getDouble(0); //ENTONCES OBTENEMOS EL VALOR DEL IMPORTE QUE SE UBICA EN LA PRIMERA POSICION
        else // SI ES NULO ENTONCES
            total += -1; // A NUESTRA CADENA QUE GUARDA EL IMPORTE LE PONEMOS -1

        cursor2.close(); //CERRAMOS EL CURSOR

        letrero.setText("$"+total); // DESPLEGAMOS EL IMPORTE EN EL TEXVIEW PARA QUE EL USUARIO LO VEO

        db.close(); //CERRAMOS LA BASE DE DATOS.
    }

}
