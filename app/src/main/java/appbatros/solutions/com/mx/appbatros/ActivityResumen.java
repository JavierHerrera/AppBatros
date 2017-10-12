package appbatros.solutions.com.mx.appbatros;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import appbatros.solutions.com.mx.appbatros.DB.HistorialDB;
import appbatros.solutions.com.mx.appbatros.adapters.AdapterPasajerosResumen;
import appbatros.solutions.com.mx.appbatros.extras.FormatoHorasFechas;
import appbatros.solutions.com.mx.appbatros.extras.SingleToast;
import appbatros.solutions.com.mx.appbatros.objetos.ListaViajes;
import appbatros.solutions.com.mx.appbatros.objetos.Pasajero;
import appbatros.solutions.com.mx.appbatros.objetos.ResumenDelPasajero;
import appbatros.solutions.com.mx.appbatros.objetos.Viaje;


public class ActivityResumen extends AppCompatActivity {
    final String TAG = "ResumenLog";

    private Boolean redondo;

    //Se utilzia para indicar si el asiento esta ocupado en la ida o viajeIda
    private Boolean viajeIda = true;

    // DB
    HistorialDB myDB;
    Cursor cursor;
    private RequestQueue requestQueue;

    CheckBox aceptarCompra, aceptarTerminos;

    ArrayList<String> asientosOcupados;
    String elAsientoOcupado;

    //Elementos del recycle
    List itemsIda = new ArrayList();
    List itemsRegreso = new ArrayList();

    RecyclerView recyclerIda;
    RecyclerView recyclerRegreso;

    RecyclerView.LayoutManager lManagerIda;
    RecyclerView.LayoutManager lManagerRegreso;

    RecyclerView.Adapter adapterIda;
    RecyclerView.Adapter adapterRegreso;

    //Dialogo de spinner
    Dialog dialogSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        redondo = ListaViajes.viajeIda.redondo;

        crearDialogoSpinner();

        //Titulos de cabecera
        TextView cabeceraSuperior = (TextView) findViewById(R.id.tv_datosPasajerosIda_resumen);
        LinearLayout cabeceraInferior = (LinearLayout) findViewById(R.id.Layout_datosPasajerosRegreso_resumen);
        cabeceraInferior.setVisibility(View.GONE);

        //Se ajustan el tamaño del os linear
        LinearLayout linearIda = (LinearLayout) findViewById(R.id.ly_contenedorDePasajerosIda_resumen);
        ViewGroup.LayoutParams params = linearIda.getLayoutParams();
        int linearSize = 140 * ListaViajes.viajeIda.getTotalPasajeros();
        params.height = linearSize;
        linearIda.setLayoutParams(params);

        LinearLayout linearRegreso = (LinearLayout) findViewById(R.id.ly_contenedorDePasajerosRegreso_resumen);
        linearRegreso.setLayoutParams(params);
        if (!redondo){linearRegreso.setVisibility(View.GONE);
        }else {linearRegreso.setVisibility(View.VISIBLE);
        }

        //Se cargan los datos de los viajes de ida y vuelta
        cargarDatosPasajerosIda();
        if (redondo) {
            cabeceraInferior.setVisibility(View.VISIBLE);
            cargarDatosPasajerosRegreso();
        }

        Log.d("Log", "Tamaño linear " + linearSize);
        cargarActionBar();

        //Elementos del resumen
        TextView origen, destino, diasemana, fechaSalida, horaSalida, importeTotal, subtotal, iva, tipoViaje;

        //Inizializar elementos del resumen
        origen = (TextView) findViewById(R.id.textView_origen_resumen);
        destino = (TextView) findViewById(R.id.textView_destino_resumen);
        diasemana = (TextView) findViewById(R.id.textView_diaSemana_resumen);
        fechaSalida = (TextView) findViewById(R.id.textView_fechaSalida_resumen);
        horaSalida = (TextView) findViewById(R.id.textView_horaSalida_resumen);
        importeTotal = (TextView) findViewById(R.id.textView_totalImporte_resumen);
        subtotal = (TextView) findViewById(R.id.textView_subtotalImporte_resumen);
        iva = (TextView) findViewById(R.id.textView_iva_resumen);
        tipoViaje = (TextView) findViewById(R.id.tv_tipoViaje_resumen);

        //Cargar datos generales
        origen.setText(ListaViajes.viajeIda.getOrigen());
        destino.setText(ListaViajes.viajeIda.getDestino());
        diasemana.setText(ListaViajes.viajeIda.getFechaDiaSemana() + " ");
        fechaSalida.setText(ListaViajes.viajeIda.getFechaSalidaDiaNumero() + " " + ListaViajes.viajeIda.getFechaSalidaMesNombre());
        horaSalida.setText(ListaViajes.viajeIda.getHoraSalidaFormato12());

        int total = ListaViajes.getImporteTotalViajes();

        subtotal.setText(String.format(Locale.getDefault(), "$%.2f", total * .83));
        iva.setText(String.format(Locale.getDefault(), "$%.2f", total * .17));
        importeTotal.setText(String.format(Locale.getDefault(), "$%.2f", total * 1.0f));

        if (redondo) {
            tipoViaje.setText(" Viaje redondo");
        } else {
            tipoViaje.setText("");
        }

        //INICIAR BD
        myDB = new HistorialDB(this);

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
    }

    private void cargarDatosPasajerosIda() {

        for (int i = 1; i <= ListaViajes.viajeIda.getTotalPasajeros(); i++) {

            String nombre = ListaViajes.viajeIda.pasajeroArrayList.get(i).getNombre();
            String tipo = ListaViajes.viajeIda.pasajeroArrayList.get(i).getTipo();
            String asiento = String.valueOf(ListaViajes.viajeIda.pasajeroArrayList.get(i).getNumero_asiento());
            String importe = String.valueOf(ListaViajes.viajeIda.pasajeroArrayList.get(i).getImporte());

            itemsIda.add(new ResumenDelPasajero(nombre, tipo, asiento, importe));

            recyclerIda = (RecyclerView) findViewById(R.id.reciclador_Ida_resumen);
            recyclerIda.setHasFixedSize(true);
            recyclerIda.setNestedScrollingEnabled(false);

            // Usar un administrador para LinearLayout
            lManagerIda = new LinearLayoutManager(getApplicationContext());
            recyclerIda.setLayoutManager(lManagerIda);

            adapterIda = new AdapterPasajerosResumen(itemsIda);
            recyclerIda.setAdapter(adapterIda);

        }
    }

    private void cargarDatosPasajerosRegreso() {

        for (int i = 1; i <= ListaViajes.viajeRegreso.getTotalPasajeros(); i++) {

            String nombre = ListaViajes.viajeRegreso.pasajeroArrayList.get(i).getNombre();
            String tipo = ListaViajes.viajeRegreso.pasajeroArrayList.get(i).getTipo();
            String asiento = String.valueOf(ListaViajes.viajeRegreso.pasajeroArrayList.get(i).getNumero_asiento());
            String importe = String.valueOf(ListaViajes.viajeRegreso.pasajeroArrayList.get(i).getImporte());

            itemsRegreso.add(new ResumenDelPasajero(nombre, tipo, asiento, importe));

            recyclerRegreso = (RecyclerView) findViewById(R.id.reciclador_Regreso_resumen);
            recyclerRegreso.setHasFixedSize(false);
            recyclerRegreso.setNestedScrollingEnabled(false);

            // Usar un administrador para LinearLayout
            lManagerRegreso = new LinearLayoutManager(getApplicationContext());
            recyclerRegreso.setLayoutManager(lManagerRegreso);

            adapterRegreso = new AdapterPasajerosResumen(itemsRegreso);
            recyclerRegreso.setAdapter(adapterRegreso);

        }
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

    public void cambiarActivityMetodoPago(View view) {
        //Se verifica si hay pasajeros con descuento y se muestra el dialogo para apartar los voletos
        if (verificarDescuento()) {
            mostrarDialogo();
        } else {
            mostarSpinnerBar();
            solicitarAsientosDisponiblesAPI(ListaViajes.viajeIda);
        }
    }

    private boolean verificarDescuento() {
        for (int i = 1; i <= ListaViajes.viajeIda.getTotalPasajeros(); i++) {
            if ((ListaViajes.viajeIda.pasajeroArrayList.get(i).getTipo() == "nino") ||
                    (ListaViajes.viajeIda.pasajeroArrayList.get(i).getTipo() == "estudiante") ||
                    (ListaViajes.viajeIda.pasajeroArrayList.get(i).getTipo() == "insen")) {
                return true;
            }
        }
        return false;
    }

    private void mostrarDialogo() {

        final Dialog dialogMain;

        dialogMain = new Dialog(ActivityResumen.this);
        dialogMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMain.setContentView(R.layout.dialogo_informativo);

        Button aceptar = dialogMain.findViewById(R.id.btn_aceptar_dialogoInformativo);
        TextView texto = dialogMain.findViewById(R.id.tv_texto_dialogoInformativo);
        texto.setText("Usted va a comprar un boleto con descuento, favor de presnetar identificación que respalde dicho descuento.");


        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostarSpinnerBar();
                solicitarAsientosDisponiblesAPI(ListaViajes.viajeIda);
                dialogMain.dismiss();
            }
        });
        dialogMain.show();
    }

    //Valida si los asientos siguen disponible consultando de nuevo la disponibilidad
    public void solicitarAsientosDisponiblesAPI(final Viaje viaje) {

        //Inicializar asientosOcupados
        asientosOcupados = new ArrayList<>();
        asientosOcupados.add("99");

        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = crearUrlApi(viaje);

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("Log:", "Array " + response);

                        //Crea un array de los asientos ocupados
                        //Si esta ocupado alguno devuelve la nueva lista a la ActivityCamion regresando al usuario
                        for (int i = 0; i < response.length(); i++) {

                            try {
                                //Verifica que no esten repeditos y los guarda en asientosOcupados
                                if (noRepetidos(response.get(i).toString())) {
                                    asientosOcupados.add(String.valueOf(response.get(i)));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                quitarSpinnerBar();
                            }
                        }

                        //Verdrifica asientos elegidos de todos los pasajeros y  los ocupados
                        //Dependiendo del viaje te regresa a la activity camion para elegir Ida o Vuelta
                        if (compararAsientos(viaje)) {
                            vovlerCamionPorAsientoOcupado();
                        } else if (redondo && viajeIda) {
                            viajeIda = false;
                            solicitarAsientosDisponiblesAPI(ListaViajes.viajeRegreso);

                        } else {
                            viajeIda = true;
                            apartarPOSTIda(1);
                            Log.d("Log:", "Se pasa a apartar Asientos");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log:", url + error);
                        quitarSpinnerBar();

                    }
                }
        );
// add it to the RequestQueue
        queue.add(jsonArrayRequest);
    }

    private String crearUrlApi(Viaje viaje) {
        //"http://198.199.102.31:4000/api/buses/boleto/:corrida/:fecha/:hora";
        FormatoHorasFechas formatoHorasFechas = new FormatoHorasFechas();
        String url = "http://198.199.102.31:4000/api/buses/boleto/disponibles/" +
                viaje.getCorrida() + "/" +
                viaje.getFechaSalidaYear() + "-" +
                viaje.getFechaSalidaMes() + "-" +
                viaje.getFechaSalidaDiaNumero() + "/" +
                viaje.getHoraSalidaFormato24Militar();

        Log.d("Log:", "http://198.199.102.31:4000/api/buses/boleto/disponibles/:corrida/:fecha/:hora");
        Log.d("Log:", "" + url);

        return url;
    }

    private boolean noRepetidos(String numero) {

        for (int i = 0; i < asientosOcupados.size(); i++) {

            if (numero == asientosOcupados.get(i)) {
                return false;
            }
        }
        return true;
    }

    private boolean compararAsientos(Viaje viaje) {

        for (int i = 1; i <= viaje.getTotalPasajeros(); i++) {

            for (int j = 0; j < asientosOcupados.size(); j++) {

                if (Objects.equals(String.valueOf(viaje.pasajeroArrayList.get(i).getNumero_asiento()), asientosOcupados.get(j))) {

                    //Se carga la nueva lista de asientos
                    viaje.salidas.setAsientosOcupados(asientosOcupados);

                    //Mostrar mensaje con el numero de asiento ocupado
                    elAsientoOcupado = asientosOcupados.get(j);

                    if (viajeIda) {
                        SingleToast.show(ActivityResumen.this, "El asiento ida " + elAsientoOcupado + " ya fue comprado, favor de elegir otro", Toast.LENGTH_LONG);
                    } else {
                        SingleToast.show(ActivityResumen.this, "El asiento vuelta " + elAsientoOcupado + " ya fue comprado, favor de elegir otro", Toast.LENGTH_LONG);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void vovlerCamionPorAsientoOcupado() {
        //Se regresa al Camion y se carga la nueva lista de asientos ocupados para que se muestre
        //Se inicializan los asientos segun el viaje en el que estan ocupados y actualiza los asientos ocupados

        if (viajeIda) {
            ListaViajes.viajeIda.pasajeroArrayList.get(1).setNumero_asiento(0);
            ListaViajes.viajeIda.salidas.setAsientosOcupados(asientosOcupados);

        }else {
            ListaViajes.viajeRegreso.pasajeroArrayList.get(1).setNumero_asiento(0);
            ListaViajes.viajeRegreso.salidas.setAsientosOcupados(asientosOcupados);
        }

        quitarSpinnerBar();
        Intent intent = new Intent(this, ActivityCamion.class);
        startActivity(intent);
    }

    //Metodo POST CON VOLEY APARTAR 1 ASIENTO
    private void apartarPOSTIda(final int i) {
        final Pasajero pasajero = ListaViajes.viajeIda.pasajeroArrayList.get(i);


        requestQueue.add(new StringRequest(Request.Method.POST,
                "http://198.199.102.31:4000/api/buses/boleto",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY", response);

                        try {

                            //Se obtiene la referencia del boleto y se guarda en datos del pasajero
                            JSONObject obj = new JSONObject(response);
                            String ref = (obj.getJSONObject("MYSQL").getString("insertId"));

                            ListaViajes.viajeIda.pasajeroArrayList.get(i).setReferencia(ref);

                            guardarPasajeroEnDB(ListaViajes.viajeIda, pasajero);

                            Log.d("Referencias", "1 " + ListaViajes.viajeIda.pasajeroArrayList.get(1).getReferencia());
                            Log.d("Referencias", "2 " + ListaViajes.viajeIda.pasajeroArrayList.get(2).getReferencia());
                            Log.d("Referencias", "3 " + ListaViajes.viajeIda.pasajeroArrayList.get(3).getReferencia());
                            Log.d("Referencias", "4 " + ListaViajes.viajeIda.pasajeroArrayList.get(4).getReferencia());

                            //Se cambia de activiti cuando termina de generar las referencias
                            Log.d("Log:", "Total " + i + " y " + ListaViajes.viajeIda.getTotalPasajeros());
                            if (i == ListaViajes.viajeIda.getTotalPasajeros()) {


                                if (redondo && viajeIda) {
                                    Log.d("Log:", "repeterir para viaje regreso");
                                    viajeIda = false;
                                    apartarPOSTRegreso(1);
                                } else {
                                    Log.d("Log:", "Go metodo pagos");
                                    goMetododepago();
                                }
                            } else {
                                int numero = i + 1;
                                apartarPOSTIda(numero);
                            }

                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                            quitarSpinnerBar();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", error.toString());
                        quitarSpinnerBar();
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
                if (mHour.length() == 1) {
                    mHour = "0" + mHour;
                }

                if (mMinute.length() == 1) {
                    mMinute = "0" + mMinute;
                }

                if (mSecond.length() == 1) {
                    mSecond = "0" + mSecond;
                }

                params.put("fecha", ListaViajes.viajeIda.getFechaSalidaYear() + "-" + ListaViajes.viajeIda.getFechaSalidaMes() + "-" + ListaViajes.viajeIda.getFechaSalidaDiaNumero());  //debe ser gual a finicio yyyy-mm-dd
                params.put("finicio", ListaViajes.viajeIda.getFechaSalidaYear() + "-" + ListaViajes.viajeIda.getFechaSalidaMes() + "-" + ListaViajes.viajeIda.getFechaSalidaDiaNumero());  //fecha en la que se realizara el ListaViajes.viajeIda yyyy-mm-dd
                params.put("finicio2", "");  //si tiene correo se pone fecha del dia, sino se pone como 2014-12-31
                params.put("ventafecha", mYear + "-" + mMonth + "-" + mDay);
                params.put("ventahora", mHour + ":" + mMinute + ":" + mSecond); //hora en la que se realizo la compra  HH:MM:SS 24Hrs
                params.put("pasajero", pasajero.getNombre());  //nombre del pasajero
                params.put("correo", "");  //correo del pasajero
                params.put("corrida", ListaViajes.viajeIda.getCorrida());  //numero de corrida
                params.put("origen", ListaViajes.viajeIda.getOrigenSiglas());   //origen seleccionado con denominacion 3 letras (HMO)
                params.put("destino", ListaViajes.viajeIda.getDestinoSiglas());   //destino seleccionado con denominacion 3 letras (HMO)
                params.put("asiento", pasajero.getNumero_asiento() + "");  //numero de asiento seleccionado por el cliente
                params.put("hora", ListaViajes.viajeIda.getHoraSalidaFormato24Militar());  //hora de salida del camion HHMM
                params.put("importe", "" + pasajero.getImporte());
                params.put("tipopago", "");
                params.put("direip", "");
                params.put("tarifad", pasajero.getTipo());

                Log.d("HORA", " " + mHour + ":" + mMinute + ":" + mSecond);
                return params;


            }
        }).setTag(TAG);
    }

    private void apartarPOSTRegreso(final int i) {
        final Pasajero pasajero = ListaViajes.viajeRegreso.pasajeroArrayList.get(i);


        requestQueue.add(new StringRequest(Request.Method.POST,
                "http://198.199.102.31:4000/api/buses/boleto",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY", response);

                        try {
                            //Se obtiene la referencia del boleto y se guarda en datos del pasajero
                            JSONObject obj = new JSONObject(response);
                            String ref = (obj.getJSONObject("MYSQL").getString("insertId"));

                            ListaViajes.viajeRegreso.pasajeroArrayList.get(i).setReferencia(ref);

                            guardarPasajeroEnDB(ListaViajes.viajeRegreso, pasajero);

                            Log.d("Referencias", "1 " + ListaViajes.viajeRegreso.pasajeroArrayList.get(1).getReferencia());
                            Log.d("Referencias", "2 " + ListaViajes.viajeRegreso.pasajeroArrayList.get(2).getReferencia());
                            Log.d("Referencias", "3 " + ListaViajes.viajeRegreso.pasajeroArrayList.get(3).getReferencia());
                            Log.d("Referencias", "4 " + ListaViajes.viajeRegreso.pasajeroArrayList.get(4).getReferencia());

                            //Se cambia de activiti cuando termina de generar las referencias
                            Log.d("Log:", "Total " + i + " y " + ListaViajes.viajeRegreso.getTotalPasajeros());
                            if (i == ListaViajes.viajeRegreso.getTotalPasajeros()) {

                                goMetododepago();
                            } else {
                                int numero = i + 1;
                                apartarPOSTRegreso(numero);
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
                if (mHour.length() == 1) {
                    mHour = "0" + mHour;
                }

                if (mMinute.length() == 1) {
                    mMinute = "0" + mMinute;
                }

                if (mSecond.length() == 1) {
                    mSecond = "0" + mSecond;
                }


                params.put("fecha", ListaViajes.viajeRegreso.getFechaSalidaYear() + "-" + ListaViajes.viajeRegreso.getFechaSalidaMes() + "-" + ListaViajes.viajeRegreso.getFechaSalidaDiaNumero());  //debe ser gual a finicio yyyy-mm-dd
                params.put("finicio", ListaViajes.viajeRegreso.getFechaSalidaYear() + "-" + ListaViajes.viajeRegreso.getFechaSalidaMes() + "-" + ListaViajes.viajeRegreso.getFechaSalidaDiaNumero());  //fecha en la que se realizara el ListaViajes.viajeRegreso yyyy-mm-dd
                params.put("finicio2", "");  //si tiene correo se pone fecha del dia, sino se pone como 2014-12-31
                params.put("ventafecha", mYear + "-" + mMonth + "-" + mDay);
                params.put("ventahora", mHour + ":" + mMinute + ":" + mSecond); //hora en la que se realizo la compra  HH:MM:SS 24Hrs
                params.put("pasajero", pasajero.getNombre());  //nombre del pasajero
                params.put("correo", "");  //correo del pasajero
                params.put("corrida", ListaViajes.viajeRegreso.getCorrida());  //numero de corrida
                params.put("origen", ListaViajes.viajeRegreso.getOrigenSiglas());   //origen seleccionado con denominacion 3 letras (HMO)
                params.put("destino", ListaViajes.viajeRegreso.getDestinoSiglas());   //destino seleccionado con denominacion 3 letras (HMO)
                params.put("asiento", pasajero.getNumero_asiento() + "");  //numero de asiento seleccionado por el cliente
                params.put("hora", ListaViajes.viajeRegreso.getHoraSalidaFormato24Militar());  //hora de salida del camion HHMM
                params.put("importe", "" + pasajero.getImporte());
                params.put("tipopago", "");
                params.put("direip", "");
                params.put("tarifad", pasajero.getTipo());

                Log.d("HORA", " " + mHour + ":" + mMinute + ":" + mSecond);
                return params;


            }
        }).setTag(TAG);
    }

    //Se crea una BD para pasar el ID de referencia obtenido del metodo apartado de boletos
    private void guardarPasajeroEnDB(Viaje viaje, Pasajero pasajero) {

        myDB.createRecords(
                myDB.selectRecords().getCount() + 1,                                  //id
                pasajero.getNombre(),                                       //nombre
                pasajero.getTipo(),                                          //tipoEstudiante
                String.valueOf(pasajero.getNumero_asiento()),               //Asiento
                String.valueOf(pasajero.getImporte()),                      //Importe
                viaje.getOrigen(),                                                 //Origen
                viaje.getDestino(),                                                //Destino
                viaje.getFechaSalidaDiaNumero() + " de " + viaje.getFechaSalidaMesNombre() + " " + viaje.getFechaSalidaYear(),    //Fecha Salida
                viaje.getHoraSalidaFormato12(),                                             //Hora
                "No se finalizó  la compra",                                  //Tipo de pago
                pasajero.getReferencia(),                                    //Referencia
                "");                                                      //Status de pago
    }

    private void goMetododepago() {
        quitarSpinnerBar();
        ListaViajes.viajeIda.setTiempo(300000);
        Log.d("Referencia", "cambiando de activity " + ListaViajes.viajeIda.pasajeroArrayList.get(1).getReferencia());
        Intent intent = new Intent(ActivityResumen.this, ActivityMetodoPago.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        if (requestQueue != null) requestQueue.cancelAll(TAG);
        super.onStop();
    }

    public void goHome() {
        Intent intent = new Intent(this, ActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void validarDatosActivarBoton() {

        Button botonContinuarActivo = (Button) findViewById(R.id.btn_confirmarActivo_resumen);
        Button botonContinuarInactivo = (Button) findViewById(R.id.btn_confirmarInactivo_resumen);

        if (aceptarCompra.isChecked() && aceptarTerminos.isChecked()) {
            botonContinuarActivo.setVisibility(View.VISIBLE);
            botonContinuarInactivo.setVisibility(View.GONE);
        } else {
            botonContinuarActivo.setVisibility(View.GONE);
            botonContinuarInactivo.setVisibility(View.VISIBLE);
        }
    }

    public void solicitarAceptarTerminos(View view) {

        SingleToast.show(this, "Para continuar acepte terminos y condiciones", Toast.LENGTH_LONG);

    }

    private void crearDialogoSpinner() {

        dialogSpinner = new Dialog(ActivityResumen.this);
        dialogSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSpinner.setContentView(R.layout.dialog_spinner);
        dialogSpinner.setCanceledOnTouchOutside(false);
        dialogSpinner.setCancelable(false);
    }

    private void mostarSpinnerBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        dialogSpinner.show();

    }

    private void quitarSpinnerBar() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        dialogSpinner.cancel();
    }
}