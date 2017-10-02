package appbatros.solutions.com.mx.appbatros;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import appbatros.solutions.com.mx.appbatros.DB.HistorialDB;
import appbatros.solutions.com.mx.appbatros.extras.SingleToast;
import appbatros.solutions.com.mx.appbatros.objetos.Viaje;

public class ActivityOxxo extends AppCompatActivity {

    final String TAG = "Referencia Oxxo",
            urlServidor = "http://198.199.102.31:4000/api/oxxo/cargo/";

    RequestQueue requestQueue;

    EditText nombre,correo,telefono;
    String refOxxo;

    //Variables de contador
    TextView tiempo;
    private static final String FORMAT = "%02d:%02d";
    CountDownTimer contador;

    //Dialogo de spinner
    Dialog dialogSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oxxo);

        crearDialogoSpinner();
        //Iniciar requestQueue
        requestQueue =  Volley.newRequestQueue(getApplicationContext());

        //5 minutos para finalziar la compra
        contador();

        cargarActionBar();

        nombre = (EditText) findViewById(R.id.et_nombre_oxxo);
        correo = (EditText) findViewById(R.id.et_correo_oxxo);
        telefono = (EditText) findViewById(R.id.et_telefono_oxxo);

    }

    private void cargarActionBar() {

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text_titulo);
        mTitleTextView.setText("Referencia Oxxo");

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

    public void gerenrarReferenccia(View view) {

        mostarSpinnerBar();

        requestQueue.add(new StringRequest(Request.Method.POST,
                "http://198.199.102.31:4000/api/oxxo/cargo/",

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "Respuesta = " + response);

                        quitarSpinnerBar();
                        SingleToast.show(ActivityOxxo.this, "Se genero referencia", Toast.LENGTH_LONG);

                        try {
                            JSONObject jsonObject = new JSONObject(response);


                            if (jsonObject.has("status")){
                                if (jsonObject.getInt("status") == 1){
                                    Log.d(TAG, "Status OK");

                                    quitarSpinnerBar();
                                    refOxxo =(jsonObject.getJSONObject("respuesta").getString("reference"));

                                    actualziarPagos();
                                    goHistorial();

                                } else {
                                    Log.e(TAG, "Status error!!!");
                                    quitarSpinnerBar();

                                    SingleToast.show(ActivityOxxo.this,"No se pudo realizar la referencia",Toast.LENGTH_LONG);
                                }
                            } else {
                                Log.d(TAG, "Sin status");
                                quitarSpinnerBar();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Error = " + e.getMessage());
                            quitarSpinnerBar();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log:", "Error en rerspuesta de pago " + error.toString());
                        SingleToast.show(ActivityOxxo.this, "Datos incorrectos", Toast.LENGTH_LONG);

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

/*               params.put("pasajero", Viaje.pasajeroArrayList.get(1).getNombre());  //d del cliente con formato cus_2hBBx84ua6qT8Hxwx
                params.put("correo", String.valueOf(correo.getText()));  //importe de la compra - Ejemplo 30000 para cobrar $300.00
                params.put("telefono", String.valueOf(telefono.getText()));  //descripcion de la compra
                params.put("descripcion", "["+Viaje.pasajeroArrayList.get(1).getReferencia()+"]");  //se genera al registrar tarjeta src_2hBFq5Mk2Q6hYc7Zqparams.put("monto", Viaje.getImporteTotal()+"00");  //se genera al registrar tarjeta src_2hBFq5Mk2Q6hYc7Zq
                params.put("monto", Viaje.getImporteTotal()+"00");  //se genera al registrar tarjeta src_2hBFq5Mk2Q6hYc7Zq*/


                params.put("pasajero", ""+nombre.getText());  //d del cliente con formato cus_2hBBx84ua6qT8Hxwx
                params.put("correo", ""+correo.getText());  //importe de la compra - Ejemplo 30000 para cobrar $300.00
                params.put("telefono", ""+telefono.getText());  //descripcion de la compra
                params.put("descripcion", generarDescripcionConBoletos());  //se genera al registrar tarjeta src_2hBFq5Mk2Q6hYc7Zqparams.put("monto", Viaje.getImporteTotal()+"00");  //se genera al registrar tarjeta src_2hBFq5Mk2Q6hYc7Zq
                params.put("monto", Viaje.getImporteTotal()+"00");  //se genera al registrar tarjeta src_2hBFq5Mk2Q6hYc7Zq


                Log.d("Log:","OXXO "+nombre.getText() + " "+ correo.getText()+ " " + telefono.getText() + " "+ Viaje.pasajeroArrayList.get(1).getReferencia());
                return params;
            }
        }).setTag(TAG);
    }

    private String generarDescripcionConBoletos(){

        switch (Viaje.getTotalPasajeros()) {
            case 1:
                return "[" + Viaje.pasajeroArrayList.get(1).getReferencia() + "]";

            case 2:
                return "[" + Viaje.pasajeroArrayList.get(1).getReferencia() + "," +
                        Viaje.pasajeroArrayList.get(2).getReferencia() + "]";

            case 3:
                return "[" + Viaje.pasajeroArrayList.get(1).getReferencia() + "," +
                        Viaje.pasajeroArrayList.get(2).getReferencia() + "," +
                        Viaje.pasajeroArrayList.get(3).getReferencia() + "]";

            case 4:
                return "[" +  Viaje.pasajeroArrayList.get(1).getReferencia() + "," +
                        Viaje.pasajeroArrayList.get(2).getReferencia() + "," +
                        Viaje.pasajeroArrayList.get(3).getReferencia() + "," +
                        Viaje.pasajeroArrayList.get(4).getReferencia() + "]";
        }
        return "[" + Viaje.pasajeroArrayList.get(1).getReferencia() + "]";
    }

    private void actualziarPagos() {

        for (int i = 1; i <=Viaje.getTotalPasajeros() ; i++) {
            aprobarPagoPasajero(Viaje.pasajeroArrayList.get(i).getReferencia(), "OxxoPay",i);
            Log.d("Log", "Pasajero Referencia" + Viaje.pasajeroArrayList.get(i).getReferencia());
        }
    }

    private void aprobarPagoPasajero(String referencia, String tipoPago, int numeroOperacion) {

        mandarPOSTdePagoAprovado(referencia,tipoPago, numeroOperacion);

        HistorialDB myDB = new HistorialDB(this);
        myDB.actualizarPagoOxxo(referencia , tipoPago, refOxxo);

    }

    private void mandarPOSTdePagoAprovado(final String id, final String tipoPago, int total) {
        requestQueue.add(new StringRequest(Request.Method.POST,
                "http://198.199.102.31:4000/api/buses/boleto/update",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY", response);
                        quitarSpinnerBar();
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

                params.put("tipopago",tipoPago);  //tipo de pago (Tarjeta, Paypal, OxxoPay)
                params.put("id",id);  //id del boleto que se obtiene cuando este se aparta

                return params;
            }
        }).setTag(TAG);
    }

    private void goHistorial(){

        contador.cancel();
        Intent intent = new Intent(ActivityOxxo.this,ActivityHistorial.class);
        intent.putExtra("PagoOxxo",true);
        intent.putExtra("ReferenciaOxxo",refOxxo);

        startActivity(intent);
    }

    private void contador() {

        tiempo =(TextView)findViewById(R.id.tv_tiempo);
        contador = new CountDownTimer(Viaje.getTiempo(), 1000) {                     //geriye sayma

            public void onTick(long millisUntilFinished) {

                tiempo.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                Viaje.setTiempo(TimeUnit.MILLISECONDS.toMillis(millisUntilFinished));
            }

            public void onFinish() {
                goHome();
            }
        }.start();
    }

    public void goHome() {
        Intent intent = new Intent(this, ActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        contador.cancel();
        super.onBackPressed();
    }

    private void crearDialogoSpinner() {

        dialogSpinner = new Dialog(ActivityOxxo.this);
        dialogSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSpinner.setContentView(R.layout.dialog_spinner);
        dialogSpinner.setCanceledOnTouchOutside(false);
        dialogSpinner.setCancelable(false);
    }

    private void mostarSpinnerBar(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        dialogSpinner.show();

    }

    private void quitarSpinnerBar(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        dialogSpinner.cancel();
    }
}
