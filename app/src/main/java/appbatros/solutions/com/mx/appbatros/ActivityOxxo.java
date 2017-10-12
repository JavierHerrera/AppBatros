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
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import appbatros.solutions.com.mx.appbatros.DB.HistorialDB;
import appbatros.solutions.com.mx.appbatros.extras.SingleToast;
import appbatros.solutions.com.mx.appbatros.objetos.ListaViajes;
import appbatros.solutions.com.mx.appbatros.objetos.Viaje;

public class ActivityOxxo extends AppCompatActivity {

    final String TAG = "Referencia Oxxo";
    String urlServidor;

    RequestQueue requestQueue;

    EditText nombre,correo,telefono;
    String refOxxo;

    //Variables de contador
    TextView tiempo;
    private static final String FORMAT = "%02d:%02d";
    CountDownTimer contador;

    //Dialogo de spinner
    Dialog dialogSpinner;

    Boolean redondo;
    Boolean viajeIda = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oxxo);

        redondo = ListaViajes.viajeIda.redondo;

        //Iniciaqr url
        urlServidor = getString(R.string.url_oxxo);

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

    public void validarCampos(View view) {

        if (nombre.getText().length() == 0){
            SingleToast.show(ActivityOxxo.this,"Ingrese el nombre",Toast.LENGTH_SHORT);

        }else if (correo.getText().length() == 0){
            SingleToast.show(ActivityOxxo.this,"Ingrese el correo",Toast.LENGTH_SHORT);

        }else if (telefono.getText().length() == 0){
            SingleToast.show(ActivityOxxo.this,"Ingrese el telefono",Toast.LENGTH_SHORT);
        }else{
            mostrarDialogo();
        }
    }

    private void mostrarDialogo() {

        final Dialog dialogMain;

        dialogMain = new Dialog(ActivityOxxo.this);
        dialogMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMain.setContentView(R.layout.dialogo_informativo);

        Button aceptar = dialogMain.findViewById(R.id.btn_aceptar_dialogoInformativo);
        Button cancelar = dialogMain.findViewById(R.id.btn_cancelar_dialogoInformativo);

        TextView texto= dialogMain.findViewById(R.id.tv_texto_dialogoInformativo);
        texto.setText("Se va a enviar un correo a "+ correo.getText() +" ¿Es correcto?");

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMain.dismiss();
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gerenrarReferencia();
                dialogMain.dismiss();
            }
        });
        dialogMain.show();
    }

    public void gerenrarReferencia() {

        mostarSpinnerBar();

        requestQueue.add(new StringRequest(Request.Method.POST,
                urlServidor,

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

                                    mandarPOSTdePagoAprovado(ListaViajes.viajeIda, 1);

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

                params.put("pasajero", ""+nombre.getText());
                params.put("correo", ""+correo.getText());
                params.put("telefono", ""+telefono.getText());
                params.put("descripcion", ""+generarDescripcionConBoletos());
                params.put("monto", ListaViajes.getImporteTotalViajes()+"00");

                Log.d("Log:","OXXO "+generarDescripcionConBoletos());
                return params;
            }
        }).setTag(TAG);
    }

    private ArrayList<String> generarDescripcionConBoletos(){

        ArrayList<String> boletos = new ArrayList<>();
        int total = ListaViajes.viajeIda.getTotalPasajeros();

        for (int i = 1; i <= total; i++) {
           boletos.add(ListaViajes.viajeIda.pasajeroArrayList.get(i).getReferencia());
        }
        if (redondo) {
            for (int i = 1; i <= total; i++) {
                boletos.add(ListaViajes.viajeRegreso.pasajeroArrayList.get(i).getReferencia());
            }
        }
      return boletos;
    }

    private void mandarPOSTdePagoAprovado(final Viaje viaje, final int i) {

        final String numeroBoleto = viaje.pasajeroArrayList.get(i).getReferencia() ;
        final String tipoPago = "Oxxo";
        Log.d("Log", "Referencia para actualziar pago "+ numeroBoleto);

        requestQueue.add(new StringRequest(Request.Method.POST,
                getString(R.string.url_pago_aprovado),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY", response);

                        HistorialDB myDB = new HistorialDB(ActivityOxxo.this);
                        myDB.actualizarPagoOxxo(numeroBoleto , tipoPago, refOxxo);

                        //Sal actualizar el pago dde todos los pasajeros y te manda a historial
                        if (i == viaje.getTotalPasajeros()){

                            if (redondo && viajeIda){
                                viajeIda = false;
                                mandarPOSTdePagoAprovado(ListaViajes.viajeRegreso, 1);
                            }else{
                                goHistorial();
                            }
                        }else{
                            int numero = i + 1;
                            mandarPOSTdePagoAprovado(viaje, numero);
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

                params.put("tipopago",tipoPago);  //tipo de pago (Tarjeta, Paypal, OxxoPay)
                params.put("id",numeroBoleto);  //id del boleto que se obtiene cuando este se aparta

                return params;
            }
        }).setTag(TAG);
    }

    private void goHistorial(){

        contador.cancel();
        Intent intent = new Intent(ActivityOxxo.this,ActivityHistorial.class);
        startActivity(intent);
    }

    private void contador() {

        tiempo =(TextView)findViewById(R.id.tv_tiempo);
        contador = new CountDownTimer(ListaViajes.viajeIda.getTiempo(), 100) {                     //geriye sayma

            public void onTick(long millisUntilFinished) {

                tiempo.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                ListaViajes.viajeIda.setTiempo(TimeUnit.MILLISECONDS.toMillis(millisUntilFinished));
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
