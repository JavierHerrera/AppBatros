package appbatros.solutions.com.mx.appbatros;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import appbatros.solutions.com.mx.appbatros.adapters.AdapterHorarios;
import appbatros.solutions.com.mx.appbatros.interfaces.MyInterfaceActivityHorarios;
import appbatros.solutions.com.mx.appbatros.objetos.Pasajeros;
import appbatros.solutions.com.mx.appbatros.objetos.Salidas;

public class ActivityHorarios extends AppCompatActivity implements MyInterfaceActivityHorarios {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private List items = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);

        cargarCabecera();

        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.recicladorCamionFila1);
        recycler.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(lManager);

        ////DATOS DE PRUEBA
        items.add(new Salidas("11:00 AM", "3:30 PM", "EXPRESS ","12" ,"230"));
        items.add(new Salidas("11:00 AM", "5:00 PM", "NORMAL ","7" ,"100"));
        items.add(new Salidas("2:00 PM", "5:30 PM", "NORMAL ","12" ,"70"));
        items.add(new Salidas("9:00 AM", "2:20 PM", "EXPRESS ","26" ,"135"));
        items.add(new Salidas("13:00 PM", "13:30 PM", "NORMAL ","40" ,"90"));

        adapter = new AdapterHorarios(items, this);
        recycler.setAdapter(adapter);
    }

    @Override
    public void foo() {
        Intent intent = new Intent(this,ActivityCamion.class);
        startActivity(intent);
    }

    public void cargarCabecera(){

        TextView dia,mes,ano;
        dia = (TextView) findViewById(R.id.textView_diaHorarios);
        mes = (TextView) findViewById(R.id.textView_mesHorarios);
        ano = (TextView) findViewById(R.id.textView_anoHorarios);

        dia.setText(Pasajeros.getFechaSalidaD());
        mes.setText(Pasajeros.getFechaSalidaM());
        ano.setText(Pasajeros.getFechaSalidaY());
    }
}
