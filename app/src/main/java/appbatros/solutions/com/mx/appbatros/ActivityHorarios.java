package appbatros.solutions.com.mx.appbatros;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import appbatros.solutions.com.mx.appbatros.adapters.AdapterHorarios;
import appbatros.solutions.com.mx.appbatros.extras.FormatoHorasFechas;
import appbatros.solutions.com.mx.appbatros.interfaces.MyInterfaceActivityHorarios;
import appbatros.solutions.com.mx.appbatros.objetos.Pasajero;
import appbatros.solutions.com.mx.appbatros.objetos.Salidas;
import appbatros.solutions.com.mx.appbatros.objetos.Viaje;

public class ActivityHorarios extends AppCompatActivity implements MyInterfaceActivityHorarios {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private List items = new ArrayList();

    int tarifaAdulto, tarifaExtudiante, tarifaInsen, tarifaMenor, tarifaNormalTotal;
    int tarifaExpressAdulto, tarifaExpressExtudiante, tarifaExpressInsen, tarifaExpressMenor,tarifaExpressTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);

        cargarActionBar();
        cargarCabecera();

        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.recicladorCamionFila1);
        recycler.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(lManager);

        pedirCorridasCamionAPI();

        adapter = new AdapterHorarios(items, this);
        recycler.setAdapter(adapter);

        //Inicializar tarifas
        tarifaAdulto = 0;
        tarifaExtudiante = 0;
        tarifaInsen = 0;
        tarifaMenor = 0;
        tarifaNormalTotal = 0;

        tarifaExpressAdulto = 0;
        tarifaExpressExtudiante = 0;
        tarifaExpressInsen = 0;
        tarifaExpressMenor = 0;
        tarifaExpressTotal = 0;
    }

    private void cargarActionBar() {

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text_titulo);

        mTitleTextView.setText("Horarios");

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

    public void pedirCorridasCamionAPI() {

        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = crearConsultaApi();
        Log.d("Log", ""+crearConsultaApi());

// prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Log", response.toString());

                        try {
                            JSONArray jsonArrayCorridas = response.getJSONArray("corridas");
                            JSONArray jsonArrayTarifas = response.getJSONArray("tarifas");

                            //Obtener Precios de tarigas Express y Normal
                            for (int i = 0; i < jsonArrayTarifas.length(); i++) {

                                if (i==0) {
                                    String tarifa = jsonArrayTarifas.getJSONObject(i).getString("tarifa");
                                    tarifaAdulto = Integer.parseInt(jsonArrayTarifas.getJSONObject(i).getString("normal"));
                                    tarifaExtudiante = Integer.parseInt(jsonArrayTarifas.getJSONObject(i).getString("estudiante"));
                                    tarifaInsen = Integer.parseInt(jsonArrayTarifas.getJSONObject(i).getString("insen"));
                                    tarifaMenor = Integer.parseInt(jsonArrayTarifas.getJSONObject(i).getString("menores"));

                                    mostrarTarigaNormalTotal();
                                }else{

                                    String tarifa = jsonArrayTarifas.getJSONObject(i).getString("tarifa");
                                    tarifaExpressAdulto = Integer.parseInt(jsonArrayTarifas.getJSONObject(i).getString("normal"));
                                    tarifaExpressExtudiante = Integer.parseInt(jsonArrayTarifas.getJSONObject(i).getString("estudiante"));
                                    tarifaExpressInsen = Integer.parseInt(jsonArrayTarifas.getJSONObject(i).getString("insen"));
                                    tarifaExpressMenor = Integer.parseInt(jsonArrayTarifas.getJSONObject(i).getString("menores"));

                                    mostrarTarifaExpressTotal();
                                }
                            }

                            //Datos de viaje

                            for (int i = 0; i < jsonArrayCorridas.length(); i++) {

                                JSONArray jsonArrayAsientos = jsonArrayCorridas.getJSONObject(i).getJSONArray("asientos");

                                //Se llena array de asientos ocupados
                                ArrayList<String> ocupados = new ArrayList<String>();
                                for (int j = 0; j < jsonArrayAsientos.length(); j++) {
                                    ocupados.add(String.valueOf(jsonArrayAsientos.get(j)));
                                }

                                String corrida = jsonArrayCorridas.getJSONObject(i).getString("corrida");
                                String hora = jsonArrayCorridas.getJSONObject(i).getString("hora");
                                String tipo = jsonArrayCorridas.getJSONObject(i).getString("express");
                                String asientosDisponibles= "0";
                                String importe = "";

                                //Se cambia formato de horas
                                FormatoHorasFechas formatoHorasFechas = new FormatoHorasFechas();
                                //hora = formatoHorasFechas.dosDigitosyAM_PM(hora);

                                //Revisar si es express o normal
                                if (Objects.equals(tipo, "V")){
                                    tipo = "Express";
                                    importe = String.valueOf(tarifaExpressTotal);
                                }else{
                                    tipo = "Normal";
                                    importe = String.valueOf(tarifaNormalTotal);                                }

                                asientosDisponibles = String.valueOf((42 - jsonArrayAsientos.length()));

                                items.add(new Salidas(hora , tipo ,asientosDisponibles ,importe, ocupados, corrida));

                                adapter.notifyDataSetChanged();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log:", url);

                    }
                }
        );
// add it to the RequestQueue
        queue.add(getRequest);
    }

    private String crearConsultaApi(){
       //"http://198.199.102.31:3000/api/buses/0150/HMO/OBR/2017-09-14";
        FormatoHorasFechas formatoHorasFechas = new FormatoHorasFechas();
            String TEST= "http://198.199.102.31:4000/api/buses/"+
                    formatoHorasFechas.validarHora()+"/"+
                    Viaje.getOrigenSiglas()+"/"+
                    Viaje.getDestinoSiglas()+"/"+
                    Viaje.getFechaSalidaYear()+"-"+
                    Viaje.getFechaSalidaMes()+"-"+
                    Viaje.getFechaSalidaDiaNumero();

        Log.d("Log:", ""+TEST);

        return TEST;
    }

    @Override
    public void adapterHorarios(Salidas salidas) {
        setDatosdeViaje(salidas);

        mostrarDialogo(salidas);
    }

    private void setDatosdeViaje(Salidas salida){

        Viaje.setHoraSalida(salida.getHorarioSalidaMilitar());
        Viaje.setCorrida(salida.getCorrida());
        if(Objects.equals(salida.getTipoCamion(), "Normal")){
            sacarTarifaNormal();
        }else {
            sacarTarifaExpress();}



    }

    public void cargarCabecera(){

        TextView origen, destino, dia, mes;

        origen = (TextView) findViewById(R.id.textVie_origen_horarios);
        destino = (TextView) findViewById(R.id.textView_destino_horarios);
        dia = (TextView) findViewById(R.id.textView_diaHorarios);
        mes = (TextView) findViewById(R.id.textView_mesHorarios);

        origen.setText(Viaje.getOrigen());
        destino.setText(Viaje.getDestino());
        dia.setText(Viaje.getFechaSalidaDiaNumero());
        mes.setText(Viaje.getFechaSalidaMesNombre());
    }

    public void sacarTarifaExpress(){

        for (int i = 1; i <=Viaje.getTotalPasajeros() ; i++) {
            agregarImportePasajeros(Viaje.pasajeroArrayList.get(i),tarifaExpressAdulto,tarifaExpressExtudiante,tarifaExpressInsen,tarifaExpressMenor);
        }
    }

    public void sacarTarifaNormal(){
        for (int i = 1; i <=Viaje.getTotalPasajeros() ; i++) {
            agregarImportePasajeros(Viaje.pasajeroArrayList.get(i),tarifaAdulto,tarifaExtudiante,tarifaInsen,tarifaMenor);
        }
    }

    public void agregarImportePasajeros(Pasajero pasajero, int tarifaAdulto, int tarifaExtudiante, int tarifaInsen, int tarifaMenor){

        if (Objects.equals(pasajero.getTipo(), "adulto")){pasajero.setImporte(tarifaAdulto);}
        if (Objects.equals(pasajero.getTipo(), "estudiante")){pasajero.setImporte(tarifaExtudiante);}
        if (Objects.equals(pasajero.getTipo(), "nino")){pasajero.setImporte(tarifaMenor);}
        if (Objects.equals(pasajero.getTipo(), "insen")){pasajero.setImporte(tarifaInsen);}

    }

    public void mostrarTarigaNormalTotal(){
        tarifaNormalTotal =
                tarifaAdulto * Viaje.getTotalAdultos() +
                tarifaExtudiante * Viaje.getTotalEstudiantes() +
                tarifaMenor * Viaje.getTotalMenores() +
                tarifaInsen * Viaje.getTotalInsen();
    }

    public void mostrarTarifaExpressTotal(){
        tarifaExpressTotal =
                tarifaExpressAdulto * Viaje.getTotalAdultos() +
                tarifaExpressExtudiante * Viaje.getTotalEstudiantes() +
                tarifaExpressMenor * Viaje.getTotalMenores() +
                tarifaExpressInsen * Viaje.getTotalInsen();
    }

    private void mostrarDialogo(final Salidas salidas) {

        final Dialog dialogMain;

        dialogMain = new Dialog(ActivityHorarios.this);
        dialogMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMain.setContentView(R.layout.dialogo_informativo);

        Button aceptar = dialogMain.findViewById(R.id.btn_aceptar_dialogoInformativo);
        aceptar.setText("Correcto");

        Button cancelar = dialogMain.findViewById(R.id.btn_cancelar_dialogoInformativo);

        TextView texto= dialogMain.findViewById(R.id.tv_texto_dialogoInformativo);
        texto.setText("Elegiste un viaje de "+Viaje.getOrigen()+" a "+Viaje.getDestino()+
                " para el dia "+Viaje.getFechaDiaSemana()+" "+Viaje.getHoraSalidaFormato12()+", ¿es correcto?");

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMain.dismiss();
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityHorarios.this,ActivityCamion.class);
                intent.putExtra("Ocupados", salidas.getAsientosOcupados());
                startActivity(intent);

                dialogMain.dismiss();

            }
        });
        dialogMain.show();
    }

}
