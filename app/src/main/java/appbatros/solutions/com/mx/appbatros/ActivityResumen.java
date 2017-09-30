package appbatros.solutions.com.mx.appbatros;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import appbatros.solutions.com.mx.appbatros.DB.HistorialDB;
import appbatros.solutions.com.mx.appbatros.extras.FormatoHorasFechas;
import appbatros.solutions.com.mx.appbatros.extras.SingleToast;
import appbatros.solutions.com.mx.appbatros.objetos.Pasajero;
import appbatros.solutions.com.mx.appbatros.objetos.Viaje;

public class ActivityResumen extends AppCompatActivity  {
    final String TAG = "ResumenLog";

    // DB
    HistorialDB myDB;
    Cursor cursor;
    private RequestQueue requestQueue;

    CheckBox aceptarCompra,aceptarTerminos;

    ArrayList<String> asientosOcupados = new ArrayList<>();
    String elAsientoOcupado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        cargarActionBar();

        //Elementos del resumen
        TextView origen,destino,diasemana, fechaSalida,horaSalida, importeTotal,subtotal,iva;

        //Inizializar elementos del resumen
        origen = (TextView) findViewById(R.id.textView_origen_resumen);
        destino = (TextView) findViewById(R.id.textView_destino_resumen);
        diasemana = (TextView) findViewById(R.id.textView_diaSemana_resumen);
        fechaSalida = (TextView) findViewById(R.id.textView_fechaSalida_resumen);
        horaSalida = (TextView) findViewById(R.id.textView_horaSalida_resumen);
        importeTotal = (TextView) findViewById(R.id.textView_totalImporte_resumen);
        subtotal = (TextView) findViewById(R.id.textView_subtotalImporte_resumen);
        iva = (TextView) findViewById(R.id.textView_iva_resumen);

        //Elementos de los datos de pasajeros
        LinearLayout layoutPasajero1,layoutPasajero2,layoutPasajero3,layoutPasajero4;
        TextView nombrePasajero1,tipoPasajero1,asientoPasajero1,importePasajero1;
        TextView nombrePasajero2,tipoPasajero2,asientoPasajero2,importePasajero2;
        TextView nombrePasajero3,tipoPasajero3,asientoPasajero3,importePasajero3;
        TextView nombrePasajero4,tipoPasajero4,asientoPasajero4,importePasajero4;

        layoutPasajero1 = (LinearLayout) findViewById(R.id.layout_pasajero1_resumen);
        layoutPasajero2 = (LinearLayout) findViewById(R.id.layout_pasajero2_resumen);
        layoutPasajero3 = (LinearLayout) findViewById(R.id.layout_pasajero3_resumen);
        layoutPasajero4 = (LinearLayout) findViewById(R.id.layout_pasajero4_resumen);


        nombrePasajero1 = (TextView) findViewById(R.id.textView_pasajero1Nombre_resumen);
        nombrePasajero2 = (TextView) findViewById(R.id.textView_pasajero2Nombre_resumen);
        nombrePasajero3 = (TextView) findViewById(R.id.textView_pasajero3Nombre_resumen);
        nombrePasajero4 = (TextView) findViewById(R.id.textView_pasajero4Nombre_resumen);

        tipoPasajero1 = (TextView) findViewById(R.id.textView_pasajero1Tipo_resumen);
        tipoPasajero2 = (TextView) findViewById(R.id.textView_pasajero2Tipo_resumen);
        tipoPasajero3 = (TextView) findViewById(R.id.textView_pasajero3Tipo_resumen);
        tipoPasajero4 = (TextView) findViewById(R.id.textView_pasajero4Tipo_resumen);

        asientoPasajero1 = (TextView) findViewById(R.id.textView_pasajero1NumeroAsiento_resumen);
        asientoPasajero2 = (TextView) findViewById(R.id.textView_pasajero2NumeroAsiento_resumen);
        asientoPasajero3 = (TextView) findViewById(R.id.textView_pasajero3NumeroAsiento_resumen);
        asientoPasajero4 = (TextView) findViewById(R.id.textView_pasajero4NumeroAsiento_resumen);

        importePasajero1 = (TextView) findViewById(R.id.textView_pasajero1Importe_resumen);
        importePasajero2 = (TextView) findViewById(R.id.textView_pasajero2Importe_resumen);
        importePasajero3 = (TextView) findViewById(R.id.textView_pasajero3Importe_resumen);
        importePasajero4 = (TextView) findViewById(R.id.textView_pasajero4Importe_resumen);


        //Cargar datos generales
        origen.setText(Viaje.getOrigen());
        destino.setText(Viaje.getDestino());
        diasemana.setText(Viaje.getFechaDiaSemana()+ " ");
        fechaSalida.setText(Viaje.getFechaSalidaDiaNumero()+ " " + Viaje.getFechaSalidaMesNombre());
        horaSalida.setText(Viaje.getHoraSalidaFormato12());
        subtotal.setText("$"+Math.round(Viaje.getImporteTotal()*.83));
        iva.setText("$"+Math.round(Viaje.getImporteTotal()*.17));
        importeTotal.setText("$"+(String.valueOf(Viaje.getImporteTotal())));

        cargarDatosPasajero(Viaje.pasajeroArrayList.get(1), layoutPasajero1,nombrePasajero1,tipoPasajero1,asientoPasajero1,importePasajero1);
        cargarDatosPasajero(Viaje.pasajeroArrayList.get(2), layoutPasajero2, nombrePasajero2,tipoPasajero2,asientoPasajero2,importePasajero2);
        cargarDatosPasajero(Viaje.pasajeroArrayList.get(3), layoutPasajero3, nombrePasajero3,tipoPasajero3,asientoPasajero3,importePasajero3);
        cargarDatosPasajero(Viaje.pasajeroArrayList.get(4), layoutPasajero4, nombrePasajero4,tipoPasajero4,asientoPasajero4,importePasajero4);

        //INICIAR BD
        myDB = new HistorialDB(this);

        //Clase de formatos
        FormatoHorasFechas formato = new FormatoHorasFechas();

        requestQueue = Volley.newRequestQueue(this);

        //Checkbox de terminos y pago. Valida que los os esten activos para habiltiar el boton de Confirmar
        aceptarCompra = (CheckBox) findViewById(R.id.checkb_aceptarcompra_resumen);
        aceptarTerminos = (CheckBox) findViewById(R.id.checkb_aceptoterminos_resumen);

        aceptarCompra.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                validarDatosActivarBoton();

            }
        });

        aceptarTerminos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                validarDatosActivarBoton();

            }
        });

        validarDatosActivarBoton();

        asientosOcupados.add("0");

    }

    private void cargarActionBar() {

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text_titulo);
        mTitleTextView.setText("Resumen de viaje");


        ImageView backButton = mCustomView.findViewById(R.id.imageView_Back);
        backButton.setImageResource(R.drawable.icon_back);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageButton homeButton = mCustomView.findViewById(R.id.imageView_Home);
        homeButton.setImageResource(R.drawable.icon_home);
        homeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                goHome();
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

    }

    private void cargarDatosPasajero(Pasajero pasajero, LinearLayout layoutPasajero, TextView nombre, TextView tipo, TextView numAsiento, TextView importe){

        if (pasajero.getTipo() != null){
            layoutPasajero.setVisibility(View.VISIBLE);
            nombre.setText(pasajero.getNombre());

            //Colocar Ñ y poner la priemra en mayuscula
            FormatoHorasFechas formato = new FormatoHorasFechas();

            String priemraMayusculas = formato.primeraLetraMayuscula(pasajero.getTipo());
            if (priemraMayusculas.equals("Nino")){
                tipo.setText("Niño");
            }else {tipo.setText(priemraMayusculas);}

            numAsiento.setText(String.valueOf(pasajero.getNumero_asiento()));
            String importeSigno = "$" + String.valueOf(pasajero.getImporte());
            importe.setText(importeSigno);
        }else{ layoutPasajero.setVisibility(View.GONE);}

    }

    public void cambiarActivityMetodoPago(View view) {

        //Se verifica si hay pasajeros con descuento y se muestra el dialogo para apartar los voletos

        if (verificarDescuento()) {
            mostrarDialogo();
        }else {
            bloquearAcciones();
            solicitarAsientosDisponiblesAPI();}
    }

    //Valida si los asientos siguen disponible consultando de nuevo la disponibilidad
    public void solicitarAsientosDisponiblesAPI() {

        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = crearUrlApi();

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("Log:", "Array "+ response );

                        //Crea un array de los asientos ocupados
                        //Si esta ocupado alguno devuelve la nueva lista a la ActivityCamion regresando al usuario
                        for (int i = 0; i < response.length(); i++){

                            try {
                                //Verifica que no esten repeditos y los guarda en asientosOcupados
                                  if ( noRepetidos(response.get(i).toString())) {
                                       asientosOcupados.add(String.valueOf(response.get(i)));
                                   }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                habilitarAcciones();
                            }
                        }

                        //Imprime la lista de asientos(TEST)
                       for (int i = 0; i < asientosOcupados.size(); i++){
                        Log.d("Log:", "Los asientos ocupados son " + asientosOcupados.get(i));
                           habilitarAcciones();
                        }

                        //Verdrifica asientos elegidos de todos los pasajeros y  los ocupados
                        if (compararAsientos()) {
                            vovlerCamionPorAsientoOcupado();
                        } else {
                            apartarAsientoDeTodosPasajeros();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log:", url + error);
                        habilitarAcciones();

                    }
                }
        );
// add it to the RequestQueue
        queue.add(jsonArrayRequest);
    }

    private boolean compararAsientos(){

        for (int i= 1; i<= Viaje.getTotalPasajeros(); i++){

            for (int j=0; j<asientosOcupados.size();j++){

                if (String.valueOf(Viaje.pasajeroArrayList.get(i).getNumero_asiento()) == asientosOcupados.get(j)){

                    //Mostrar mensaje con el numero de asiento ocupado
                    elAsientoOcupado = asientosOcupados.get(j);
                    SingleToast.show(ActivityResumen.this, "El asiento "+elAsientoOcupado+" ya fue comprado, favor de elegir otro", Toast.LENGTH_LONG);

                    return true;
                }
            }
        } return false;
    }

    private boolean noRepetidos(String numero){

        for (int i = 0; i < asientosOcupados.size(); i++){

            if ( numero == asientosOcupados.get(i)){return false;}
        }
        return true;
    }

    private String crearUrlApi(){
        //"http://198.199.102.31:4000/api/buses/boleto/:corrida/:fecha/:hora";
        FormatoHorasFechas formatoHorasFechas = new FormatoHorasFechas();
        String TEST= "http://198.199.102.31:4000/api/buses/boleto/disponibles/"+
                Viaje.getCorrida()+"/"+
                Viaje.getFechaSalidaYear()+"-"+
                Viaje.getFechaSalidaMes()+"-"+
                Viaje.getFechaSalidaDiaNumero()+"/"+
                Viaje.getHoraSalidaFormato24Militar();

        Log.d("Log:", "http://198.199.102.31:4000/api/buses/boleto/disponibles/:corrida/:fecha/:hora");
        Log.d("Log:", ""+TEST);

        return TEST;
    }

    //Metodo POST CON VOLEY APARTAR 1 ASIENTO
    private  void apartarAsientoPOST(final Pasajero pasajero, final int total){
        requestQueue.add(new StringRequest(Request.Method.POST,
                "http://198.199.102.31:4000/api/buses/boleto",
                new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);

                try {

                    //Se obtiene la referencia del boleto y se guarda en datos del pasajero
                    JSONObject obj = new JSONObject(response);
                    String ref =(obj.getJSONObject("MYSQL").getString("insertId"));

                    Viaje.pasajeroArrayList.get(total).setReferencia(ref);

                    guardarPasajeroEnDB(pasajero);

                    Log.d("Referencias", "1 "+Viaje.pasajeroArrayList.get(1).getReferencia());
                    Log.d("Referencias", "2 "+Viaje.pasajeroArrayList.get(2).getReferencia());
                    Log.d("Referencias", "3 "+Viaje.pasajeroArrayList.get(3).getReferencia());
                    Log.d("Referencias", "4 "+Viaje.pasajeroArrayList.get(4).getReferencia());

                    //Se cambia de activiti cuando termina de generar las referencias
                    if (total == Viaje.getTotalPasajeros())
                    {
                        habilitarAcciones();
                        goMetododepago();
                    }

                } catch (Throwable t) {
                    Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                }
            }
        },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                Calendar c = Calendar.getInstance();
                String mYear = String.valueOf(c.get(Calendar.YEAR));
                String mMonth = String.valueOf(c.get(Calendar.MONTH));
                String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
                String mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
                String mMinute = String.valueOf(c.get(Calendar.MINUTE));
                String mSecond = String.valueOf(c.get(Calendar.SECOND));

                //Agregar 0 a horas, minutos  y segundossi tienen un digito
                if( mHour.length() == 1){
                    mHour = "0" + mHour;
                }

                if( mMinute.length() == 1){
                    mMinute = "0" + mMinute;
                }

                if( mSecond.length() == 1){
                    mSecond = "0" + mSecond;
                }

                params.put("fecha",Viaje.getFechaSalidaYear()+"-"+Viaje.getFechaSalidaMes()+"-"+Viaje.getFechaSalidaDiaNumero());  //debe ser gual a finicio yyyy-mm-dd
                params.put("finicio",Viaje.getFechaSalidaYear()+"-"+Viaje.getFechaSalidaMes()+"-"+Viaje.getFechaSalidaDiaNumero());  //fecha en la que se realizara el viaje yyyy-mm-dd
                params.put("finicio2", "");  //si tiene correo se pone fecha del dia, sino se pone como 2014-12-31
                params.put("ventafecha",  mYear+"-"+mMonth+"-"+mDay);
                params.put("ventahora", mHour+":"+mMinute+":"+mSecond); //hora en la que se realizo la compra  HH:MM:SS 24Hrs
                params.put("pasajero", pasajero.getNombre());  //nombre del pasajero
                params.put("correo", "");  //correo del pasajero
                params.put("corrida", Viaje.getCorrida());  //numero de corrida
                params.put("origen", Viaje.getOrigenSiglas());   //origen seleccionado con denominacion 3 letras (HMO)
                params.put("destino", Viaje.getDestinoSiglas());   //destino seleccionado con denominacion 3 letras (HMO)
                params.put("asiento", pasajero.getNumero_asiento()+"");  //numero de asiento seleccionado por el cliente
                params.put("hora", Viaje.getHoraSalidaFormato24Militar());  //hora de salida del camion HHMM
                params.put("importe", ""+pasajero.getImporte());
                params.put("tipopago", "");
                params.put("direip", "");
                params.put("tarifad", pasajero.getTipo());

                Log.d("HORA"," "+mHour+":"+mMinute+":"+mSecond);
                return params;



            }
        }).setTag(TAG);
    }

    //Se crea una BD para pasar el ID de referencia obtenido del metodo apartado de boletos
    private void guardarPasajeroEnDB(Pasajero pasajero){

        myDB.createRecords(
                myDB.selectRecords().getCount()+1,                                  //id
               pasajero.getNombre(),                                       //nombre
                pasajero.getTipo(),                                          //tipoEstudiante
                String.valueOf(pasajero.getNumero_asiento()),               //Asiento
                String.valueOf(pasajero.getImporte()),                      //Importe
                Viaje.getOrigen(),                                                 //Origen
                Viaje.getDestino(),                                                //Destino
                Viaje.getFechaSalidaDiaNumero()+ " de "+ Viaje.getFechaSalidaMesNombre()+" "+Viaje.getFechaSalidaYear(),    //Fecha Salida
                Viaje.getHoraSalidaFormato12(),                                             //Hora
                "No se finalizó  la compra",                                                          //Tipo de pago
                pasajero.getReferencia(),                                    //Referencia
                "");                                                      //Status de pago
    }

    @Override
    protected void onStop() {
        if (requestQueue != null) requestQueue.cancelAll(TAG);
        super.onStop();
    }

    private void mostrarDialogo() {

        final Dialog dialogMain;

        dialogMain = new Dialog(ActivityResumen.this);
        dialogMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMain.setContentView(R.layout.dialogo_informativo);

        Button aceptar = dialogMain.findViewById(R.id.btn_aceptar_dialogoInformativo);
        TextView texto= dialogMain.findViewById(R.id.tv_texto_dialogoInformativo);
        texto.setText("Usted va a comprar un boleto con descuento, favor de presnetar identificación que respalde dicho descuento.");


        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                solicitarAsientosDisponiblesAPI();
                dialogMain.dismiss();


            }
        });
        dialogMain.show();
    }

    private boolean verificarDescuento(){
        for(int i = 1 ; i<=Viaje.getTotalPasajeros(); i++){
            if ((Viaje.pasajeroArrayList.get(i).getTipo() == "nino") ||
                    (Viaje.pasajeroArrayList.get(i).getTipo() == "estudiante") ||
                    (Viaje.pasajeroArrayList.get(i).getTipo() == "insen")){
                return true;
            }
        } return false;
    }

    private void apartarAsientoDeTodosPasajeros() {

        for (int i = 1; i <= Viaje.getTotalPasajeros(); i++) {
            apartarAsientoPOST(Viaje.pasajeroArrayList.get(i), i);
        }
    }

    private void goMetododepago() {
        Viaje.setTiempo(30000);
        Intent intent = new Intent(ActivityResumen.this, ActivityMetodoPago.class);
        startActivity(intent);
    }

    public void goHome() {

        Intent intent = new Intent(this, ActivityMain.class);
        startActivity(intent);
    }

    private void validarDatosActivarBoton(){

        Button botonContinuarActivo = (Button) findViewById(R.id.btn_confirmarActivo_resumen);
        Button botonContinuarInactivo = (Button) findViewById(R.id.btn_confirmarInactivo_resumen);

        if ( aceptarCompra.isChecked() && aceptarTerminos.isChecked()){
            botonContinuarActivo.setVisibility(View.VISIBLE);
            botonContinuarInactivo.setVisibility(View.GONE);
        }
        else {
            botonContinuarActivo.setVisibility(View.GONE);
            botonContinuarInactivo.setVisibility(View.VISIBLE);
        }
    }

    public void vovlerCamionPorAsientoOcupado() {

        Intent intent = new Intent(this, ActivityCamion.class);
        intent.putExtra("Ocupados",asientosOcupados);
        startActivity(intent);
    }

    public void solicitarAceptarTerminos(View view) {

        SingleToast.show(this, "Para continuar acepte terminos y condiciones", Toast.LENGTH_LONG);

    }

    private void bloquearAcciones(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void habilitarAcciones(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}