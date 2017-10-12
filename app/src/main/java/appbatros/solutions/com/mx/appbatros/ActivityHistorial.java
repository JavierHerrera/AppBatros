package appbatros.solutions.com.mx.appbatros;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import appbatros.solutions.com.mx.appbatros.DB.HistorialDB;
import appbatros.solutions.com.mx.appbatros.adapters.AdapterHistorial;
import appbatros.solutions.com.mx.appbatros.objetos.Historial;

public class ActivityHistorial extends AppCompatActivity {

    private List items = new ArrayList();

    private String referenciaOxxo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        //Si recibe un pago tipo oxxo, toma la referencia y muestra un mensaje
        boolean pagoOxxo = false;
        try {
            Intent parentIntent = getIntent();
            pagoOxxo = parentIntent.getBooleanExtra("PagoOxxo", false);
            referenciaOxxo = parentIntent.getStringExtra("ReferenciaOxxo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        cargarActionBar();

        //Iniciar el adaptador y recycle
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recicladorHistorial);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(lManager);
        HistorialDB myDB = new HistorialDB(this);;

        Cursor cursor = myDB.selectRecords();
        if (cursor != null && cursor.moveToFirst()){
            if (!cursor.isNull(0)) {
                String id = cursor.getString(0);
                String nombre = cursor.getString(1);
                String tipoPasajero = cursor.getString(2);
                String asiento = cursor.getString(3);
                String importe = cursor.getString(4);
                String origen = cursor.getString(5);
                String destino = cursor.getString(6);
                String fecha = cursor.getString(7);
                String hora = cursor.getString(8);
                String tipoPago = cursor.getString(9);
                String referencia = cursor.getString(10);
                String status = cursor.getString(11);

                items.add(new Historial(id,nombre,tipoPasajero,asiento,importe,origen,destino,fecha,hora,tipoPago,referencia,status));
            }
            while (cursor.moveToNext()) {

                 String id = cursor.getString(0);
                 String nombre = cursor.getString(1);
                 String tipoPasajero = cursor.getString(2);
                 String asiento = cursor.getString(3);
                 String importe = cursor.getString(4);
                 String origen = cursor.getString(5);
                 String destino = cursor.getString(6);
                 String fecha = cursor.getString(7);
                 String hora = cursor.getString(8);
                 String tipoPago = cursor.getString(9);
                 String referencia = cursor.getString(10);
                 String status = cursor.getString(11);

                items.add(new Historial(id,nombre,tipoPasajero,asiento,importe,origen,destino,fecha,hora,tipoPago,referencia,status));
            }
        }

        //Validar si el pago fue por oxxo
        if (pagoOxxo){
            mostrarDialogoOxxo();
        }

        RecyclerView.Adapter adapter = new AdapterHistorial(items);
        recycler.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ActivityMain.class);
        startActivity(intent);

    }

    private void cargarActionBar() {

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text_titulo);
        mTitleTextView.setText("Historial");

        ImageView backButton = mCustomView.findViewById(R.id.imageView_Back);
        backButton.setImageResource(R.drawable.icon_back);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    private void mostrarDialogoOxxo() {

        final Dialog dialogMain;

        dialogMain = new Dialog(ActivityHistorial.this);
        dialogMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMain.setContentView(R.layout.dialogo_referencia_oxxo);

        Button aceptar = dialogMain.findViewById(R.id.botonAceptarDialogoOxxo);
        TextView referencia= dialogMain.findViewById(R.id.tv_referenciaOxxo);
        referencia.setText(referenciaOxxo);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMain.dismiss();

            }
        });
        dialogMain.show();
    }
}
