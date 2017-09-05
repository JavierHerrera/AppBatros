package appbatros.solutions.com.mx.appbatros;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import appbatros.solutions.com.mx.appbatros.adapters.AdapterAsiento;
import appbatros.solutions.com.mx.appbatros.interfaces.MyInterfaceActivityCamion;
import appbatros.solutions.com.mx.appbatros.objetos.Asiento;
import appbatros.solutions.com.mx.appbatros.objetos.Pasajero;
import appbatros.solutions.com.mx.appbatros.objetos.Pasajeros;

public class ActivityCamion extends AppCompatActivity implements MyInterfaceActivityCamion {

    private Dialog dialog;
    List<Asiento> itemsIzq = new ArrayList<>();
    List<Asiento> itemsDer = new ArrayList<>();

    AdapterAsiento adapter1;
    AdapterAsiento adapter2;

    ImageView iconPasajero1,iconPasajero2,iconPasajero3,iconPasajero4;
    LinearLayout layoutPasajero1,layoutPasajero2,layoutPasajero3,layoutPasajero4;

    //Se valida el boolean para que aparescan los datos del pasajero en la parte superior
    Boolean seleccionado_p1,seleccionado_p2,seleccionado_p3,seleccionado_p4;

    //Se copia le objeto para cargar los datos en la parte superior(resumen)
    Pasajero pasajero_seleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camion);

        //Cargar Asientos izquerda y derecha
        for (int i = 0; i < 42; i++) {
                lado(i).add(new Asiento(R.drawable.asiento_libre2, i + 1,"disponible"));
        }

        RecyclerView recycler1 = (RecyclerView) findViewById(R.id.recicladorAsientos1);
        RecyclerView recycler2 = (RecyclerView) findViewById(R.id.recicladorAsientos2);
        recycler1.setNestedScrollingEnabled(false);
        recycler1.setHasFixedSize(true);
        recycler2.setNestedScrollingEnabled(false);
        recycler2.setHasFixedSize(true);

        GridLayoutManager mGridLayoutManager1 = new GridLayoutManager(getApplicationContext(), 2);
        GridLayoutManager mGridLayoutManager2 = new GridLayoutManager(getApplicationContext(), 2);
        recycler1.setLayoutManager(mGridLayoutManager1);
        recycler2.setLayoutManager(mGridLayoutManager2);

        adapter1 = new AdapterAsiento(itemsIzq, this);
        adapter2 = new AdapterAsiento(itemsDer, this);

        recycler1.setAdapter(adapter1);
        recycler2.setAdapter(adapter2);

        seleccionado_p1 = false;
        seleccionado_p2 = false;
        seleccionado_p3 = false;
        seleccionado_p4 = false;
    }

    @Override
    public void foo(Asiento asiento, int position) {
        callbackclick(asiento,position);
    }

    private void callbackclick(final Asiento asiento, final int position) {

        //Validar asiento disponible
        if (Objects.equals(asiento.getStatus(), "disponible")){

            //Abrir Dialogo
            dialog = new Dialog(ActivityCamion.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialogo_seleccion_asientos);

            //Iniciazializar imagen de icono y texto
            layoutPasajero1 = dialog.findViewById(R.id.layout_pasajero1Camion);
            layoutPasajero2 = dialog.findViewById(R.id.layout_pasajero2Camion);
            layoutPasajero3 = dialog.findViewById(R.id.layout_pasajero3Camion);
            layoutPasajero4 = dialog.findViewById(R.id.layout_pasajero4Camion);

            iconPasajero1 = dialog.findViewById(R.id.icon_pasajero1Camion);
            iconPasajero2 = dialog.findViewById(R.id.icon_pasajero2Camion);
            iconPasajero3 = dialog.findViewById(R.id.icon_pasajero3Camion);
            iconPasajero4 = dialog.findViewById(R.id.icon_pasajero4Camion);

            TextView textViewPasajero1 = dialog.findViewById(R.id.textView_pasajero1Camion);
            TextView textViewPasajero2 = dialog.findViewById(R.id.textView_pasajero2Camion);
            TextView textViewPasajero3 = dialog.findViewById(R.id.textView_pasajero3Camion);
            TextView textViewPasajero4 = dialog.findViewById(R.id.textView_pasajero4Camion);

            //Inicializar botones de aceptar y cancelar
            Button aceptar =  dialog.findViewById(R.id.botonAceptarDialogoAsientos);
            Button cancelar =  dialog.findViewById(R.id.botonCancelarDialogoAsientos);

            //Cargar Iconos disponibles de cada pasajero
            cargarIconos(Pasajeros.pasajero1, layoutPasajero1, iconPasajero1, textViewPasajero1);
            cargarIconos(Pasajeros.pasajero2, layoutPasajero2, iconPasajero2, textViewPasajero2);
            cargarIconos(Pasajeros.pasajero3, layoutPasajero3, iconPasajero3, textViewPasajero3);
            cargarIconos(Pasajeros.pasajero4, layoutPasajero4, iconPasajero4, textViewPasajero4);

            //Se muestra el dialogo
            dialog.show();

            layoutPasajero1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cambiarColorIconoAzulPasajero();
                    iconPasajero1.setImageResource(Pasajeros.pasajero1.getIcon_seleccionado());
                    pasajero_seleccionado = Pasajeros.pasajero1;
                    pasajero_seleccionado = Pasajeros.pasajero1;
                }
            });

            iconPasajero2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cambiarColorIconoAzulPasajero();
                    iconPasajero2.setImageResource(Pasajeros.pasajero2.getIcon_seleccionado());
                    pasajero_seleccionado = Pasajeros.pasajero1;
                    pasajero_seleccionado = Pasajeros.pasajero1;
                }
            });

            iconPasajero3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cambiarColorIconoAzulPasajero();
                    iconPasajero3.setImageResource(Pasajeros.pasajero3.getIcon_seleccionado());
                    pasajero_seleccionado = Pasajeros.pasajero1;
                    pasajero_seleccionado = Pasajeros.pasajero1;
                    }
            });

            iconPasajero4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cambiarColorIconoAzulPasajero();
                    iconPasajero4.setImageResource(Pasajeros.pasajero4.getIcon_seleccionado());
                    pasajero_seleccionado = Pasajeros.pasajero1;
                    pasajero_seleccionado = Pasajeros.pasajero1;
                }
            });

            cancelar.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialog.dismiss();
                }
            });

            aceptar.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //Cambiar color a seleccionado(azul)
                    asiento.setImagen(R.drawable.asiento_seleccionado2);
                    actualizarVista(asiento,position);
                    asiento.setStatus("seleccionado");
                    cargarDatosPasajero(pasajero_seleccionado);
                    dialog.dismiss();
                }
            });


        }else if(Objects.equals(asiento.getStatus(), "ocupado")){

            SingleToast.show(this,"Asiento "+ asiento.getNumeroAsiento()+ " esta ocupado",Toast.LENGTH_SHORT);
        }
        else if(Objects.equals(asiento.getStatus(), "seleccionado")){

            SingleToast.show(this,"Asiento "+ asiento.getNumeroAsiento()+ " seleccionado",Toast.LENGTH_SHORT);
        }
    }

    private void cargarDatosPasajero(Pasajero pasajero_seleccionado) {
    }

    private void cambiarColorIconoAzulPasajero(){

        iconPasajero1.setImageResource(Pasajeros.pasajero1.getIcono());
        iconPasajero2.setImageResource(Pasajeros.pasajero2.getIcono());
        iconPasajero3.setImageResource(Pasajeros.pasajero3.getIcono());
        iconPasajero4.setImageResource(Pasajeros.pasajero4.getIcono());
    }

    private void cargarIconos(Pasajero pasajero, LinearLayout layout, ImageView icon, TextView tipo) {

        if(pasajero.getTipo() != null){
            layout.setVisibility(View.VISIBLE);
            icon.setImageResource(pasajero.getIcono());
            tipo.setText(pasajero.getTipo());
        }
    }

    private void actualizarVista(Asiento asiento, int position){
        int numA = asiento.getNumeroAsiento() - 1;
        lado(numA).remove(position);
        lado(numA).add(position,asiento);
        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
    }

    private  List<Asiento> lado(int numAsiento) {
        if (numAsiento % 4 == 0 || numAsiento % 4 == 1) {
            return itemsIzq;
        } else {
            return itemsDer;
        }
    }


}
