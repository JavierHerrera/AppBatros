package appbatros.solutions.com.mx.appbatros;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import appbatros.solutions.com.mx.appbatros.DB.ConektaDB;
import appbatros.solutions.com.mx.appbatros.extras.SingleToast;
import appbatros.solutions.com.mx.appbatros.objetos.Viaje;
import io.conekta.conektasdk.Card;
import io.conekta.conektasdk.Conekta;
import io.conekta.conektasdk.Token;

public class ActivityAgregarTarjeta extends AppCompatActivity {

    final String TAG = "AgregarTarjetaLog",
            urlServidor = "http://198.199.102.31:4000/api/conekta/tarjeta";
    RequestQueue mRequestQueue, requestQueuePago, requestQueueRegistro;
    Token tokenConekta;
    EditText etTitularTarjeta, etNumeroTarjeta, etCodigoTarjeta, etMesTarjeta, etAnoTarjeta;
    TextView etTotal;
    String titularTarjeta = "", numeroTarjeta = "",
            codigoTarjeta = "", mesTarjeta = "", anoTarjeta = "";

    String clienteID; //Registro del cliente
    String conektaID; //Token de conekta
    String tarjetaID; //ID de la tarjeta seleccionada
    int numeroTarjetasAsociadas = 0;


    private ArrayList<String> idTarjetasArray = new ArrayList<>();
    private ArrayList<String> last4Array = new ArrayList<>();
    private ArrayList<String> tipoTarjetaArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarjeta);

        cargarActionBar();
        //Se verifica si hay tarjetas asociadas
        revisarTarjetasAosiadas();

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueueRegistro = Volley.newRequestQueue(getApplicationContext());
        requestQueuePago = Volley.newRequestQueue(getApplicationContext());

        clienteID = consultarClienteIdDB();

        // setConekta();

        etTotal = (TextView) findViewById(R.id.textView_total_agregarTarjeta);
        etTitularTarjeta = (EditText) findViewById(R.id.editTextTitularAgregarTarjeta);
        etNumeroTarjeta = (EditText) findViewById(R.id.editTextNumeroAgregarTarjeta);
        etCodigoTarjeta = (EditText) findViewById(R.id.editTextCodigoAgregarTarjeta);
        etMesTarjeta = (EditText) findViewById(R.id.editTextFechaMesAgregarTarjeta);
        etAnoTarjeta = (EditText) findViewById(R.id.editTextFechaAnoAgregarTarjeta);

        //Agregar Total
        etTotal.setText("Total: $"+ Viaje.getImporteTotal());
    }

    //Metodos para obetner tarjetas registradas o registrarlas
    public void revisarTarjetasAosiadas() {

        RequestQueue queue = Volley.newRequestQueue(this);

        final String url = "http://198.199.102.31:4000/api/conekta/tarjeta/" + consultarClienteIdDB();
        Log.d("Log", "" + url);

// prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Log:", "Busqueda de tarjetas registradas " + response.toString() + " URL" + url);

                        if (response.length() == 3) {

                            //Si status es 0 no se abre el menu de tarjetas seleccionadas y pasas al registro
                            Log.d("Log:", "No se encontro tarjetas asosiadas" + response.toString());

                        } else {
                            Log.d("Log:", "Se encontraron tarjetas asosiadas " + response.toString() + " Seleccionar tarjeta");
                            //Cambian flag de tarjetas asosiadas
                            numeroTarjetasAsociadas = 1;

                            //Cargar array de tarjetas
                            try {

                                int total = response.getJSONArray("tarjetas").length();

                                //Guardar los datos de tarjetas registradas
                                for (int i = 0; i < total; i++) {

                                    String id = response.getJSONArray("tarjetas").getJSONObject(i).getString("id");
                                    String last4 = response.getJSONArray("tarjetas").getJSONObject(i).getString("last4");
                                    String tipo = response.getJSONArray("tarjetas").getJSONObject(i).getString("brand");

                                    idTarjetasArray.add(id);
                                    last4Array.add(last4);
                                    tipoTarjetaArray.add(tipo);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //Muestra una lista de tarjetas registradas para seleccionar
                           // mostrarDialogoConTaretasAsociadas();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Vol", url);

                    }
                }
        );
// add it to the RequestQueue
        queue.add(getRequest);
    }

    //Muestra el dialogo con la lista de tarjetas
    private void mostrarDialogoConTaretasAsociadas() {

        final Dialog dialogMain;

        dialogMain = new Dialog(ActivityAgregarTarjeta.this);
        dialogMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMain.setContentView(R.layout.dialogo_seleccionar_tarjeta);

        Button aceptar = dialogMain.findViewById(R.id.botonAceptarDialogoTarjeta);
        Button cancelar = dialogMain.findViewById(R.id.botonCancelarDialogoTarjeta);

        //Cargar Tarjetas Asociadas
        for (int i = 0; i < idTarjetasArray.size(); i++) {


            //Se cargan las tarjetas asociadas
            final LinearLayout linearLayout = dialogMain.findViewById(R.id.linearLayoud_dialogTarjeta);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            LinearLayout.LayoutParams paramsLayout = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            final LinearLayout lineartarjeta = new LinearLayout(getApplicationContext());
            TextView tipoTarjeta = new TextView(getApplicationContext());
            TextView digitosTarjeta = new TextView(getApplicationContext());


            tipoTarjeta.setText(tipoTarjetaArray.get(i)+" - ");
            tipoTarjeta.setTextColor(Color.BLACK);
            tipoTarjeta.setTextSize(22);


            digitosTarjeta.setText(last4Array.get(i));
            digitosTarjeta.setTextColor(Color.BLACK);
            digitosTarjeta.setTextSize(22);


            lineartarjeta.addView(tipoTarjeta, params);
            lineartarjeta.addView(digitosTarjeta, params);
            //lineartarjeta.setTag(linearLayout.getChildCount());
            lineartarjeta.setTag(idTarjetasArray.get(i));

            lineartarjeta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.getTag();
                    Log.d("Log:", "Tag del linear " + String.valueOf(lineartarjeta.getTag()));

                    tarjetaID = String.valueOf(lineartarjeta.getTag());
                    dialogMain.dismiss();
                    realizarPago();

                }
            });

            linearLayout.addView(lineartarjeta, params);

        }
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMain.dismiss();

            }
        });


        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMain.dismiss();
            }
        });

        dialogMain.show();
    }

    //Boton que agrega tarjeta y Pagar
    public void AgregarTarjeta(View view) {
        crearConektaID();

    }

    //Crea conektaID para poder registrar posteriormente
    public void crearConektaID() {
        Activity activity = this;

        Conekta.setPublicKey(getString(R.string.conekta_credential));
        Conekta.collectDevice(activity); //Collect device

        titularTarjeta = String.valueOf(etTitularTarjeta.getText());
        numeroTarjeta = String.valueOf(etNumeroTarjeta.getText());
        codigoTarjeta = String.valueOf(etCodigoTarjeta.getText());
        mesTarjeta = String.valueOf(etMesTarjeta.getText());
        anoTarjeta = String.valueOf(etAnoTarjeta.getText());

        Card card = new Card("Josue Camara", "4242424242424242", "332", "11", "2017");
        // Card card = new Card(titularTarjeta, numeroTarjeta, codigoTarjeta, mesTarjeta, anoTarjeta);
        tokenConekta = new Token(activity);

        tokenConekta.onCreateTokenListener(new Token.CreateToken() {
            @Override
            public void onCreateTokenReady(JSONObject data) {
                try {
                    //TODO: Create charge
                    Log.d("Log:", "Respuesta de token " + data);
                    conektaID = data.getString("id");

                    registrarTarjeta();

                } catch (Exception err) {
                    //TODO: Handle error
                    Log.d("Log: ", " Error al obtener token" + err.toString());
                    SingleToast.show(ActivityAgregarTarjeta.this, "No se registro la tarjeta", Toast.LENGTH_LONG);

                }
            }
        });

        tokenConekta.create(card);//Create token
    }

    //Se registra la tarjeta con clienteID y conectaID
    public void registrarTarjeta() {

        requestQueueRegistro.add(new StringRequest(Request.Method.POST,
                "http://198.199.102.31:4000/api/conekta/tarjeta",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Log:", "Respuesta de registro de tarjeta " + response);

                        //Se cambia la repsuesta objetoJson para obtener tarjetaID
                        try {
                            JSONObject obj = new JSONObject(response);

                            //Si status es 1 se obitene tarjetaID y se realiza le pago
                            if (Objects.equals(obj.get("status").toString(), "1")) {
                                tarjetaID = obj.getJSONObject("respuesta").getString("id");
                                Log.i("Log:", "tarjetaID " + tarjetaID);
                                realizarPago();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log:", "Respuesta de error al registrar tarjeta " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("conektaid", clienteID);  //d del cliente con formato cus_2hBBx84ua6qT8Hxwx
                params.put("cardtoken", conektaID);  //fdsafdsfd

                Log.e("xx", clienteID + "      " + String.valueOf(tokenConekta));

                return params;
            }
        }).setTag(TAG);

    }

    //PAGO: se envian los datos y el monto para finalziar la venta
    private void realizarPago() {

        requestQueuePago.add(new StringRequest(Request.Method.POST,
                "http://198.199.102.31:4000/api/conekta/cargo",

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Log:", "Rerspuesta de pago " + response);
                        SingleToast.show(ActivityAgregarTarjeta.this, "Respuesta del Pago", Toast.LENGTH_LONG);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log:", "Error en rerspuesta de pago " + error.toString());
                        SingleToast.show(ActivityAgregarTarjeta.this, "Tarjeta Rechazada", Toast.LENGTH_LONG);

                        //Si no se proceso el pago te regresa a la de verificar registro
                        revisarTarjetasAosiadas(); //te abre lista de tarjetas para que asosies otra
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("conektaid", clienteID);  //d del cliente con formato cus_2hBBx84ua6qT8Hxwx
                params.put("monto", "200" + "00");  //importe de la compra - Ejemplo 30000 para cobrar $300.00
                params.put("descripcion", "Boleto autobus");  //descripcion de la compra
                params.put("cardid", "src_2hFKbdFqGBDpWwB2B");  //se genera al registrar tarjeta src_2hBFq5Mk2Q6hYc7Zq

                return params;
            }
        }).setTag(TAG);
    }

    //METODOS AUXILARES

    //Regresa ClienteID si es que tiene una registrada
    private String consultarClienteIdDB() {

        ConektaDB myDB = new ConektaDB(this);
        Cursor cursor = myDB.selectRecords();

        //el la columna # 4, cokekt_id
        return cursor.getString(4);
    }

    //AcctionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cargarActionBar() {

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text_titulo);
        mTitleTextView.setText("Registrar tarjeta");

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
}