package appbatros.solutions.com.mx.appbatros;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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

import java.util.HashMap;
import java.util.Map;

import appbatros.solutions.com.mx.appbatros.extras.SingleToast;
import appbatros.solutions.com.mx.appbatros.objetos.Viaje;

public class ActivityOxxo extends AppCompatActivity {

    final String TAG = "Referencia Oxxo",
            urlServidor = "http://198.199.102.31:4000/api/oxxo/cargo/";

    EditText nombre,correo,telefono;
    String refOxxo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oxxo);

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

        RequestQueue requestQueuePago =  Volley.newRequestQueue(getApplicationContext());

        requestQueuePago.add(new StringRequest(Request.Method.POST,
                "http://198.199.102.31:4000/api/oxxo/cargo/",

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("Log:", "Rerspuesta de pago " + response);
                        SingleToast.show(ActivityOxxo.this, "Se genero referencia", Toast.LENGTH_LONG);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            refOxxo =(jsonObject.getJSONObject("respuesta").getString("reference"));
                            MostrarDialogo();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("Log:", "Error al guardar referencia " + refOxxo);

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



                params.put("pasajero", Viaje.pasajeroArrayList.get(1).getNombre());  //d del cliente con formato cus_2hBBx84ua6qT8Hxwx
                params.put("correo", "javierfirebase1@gmail.com");  //importe de la compra - Ejemplo 30000 para cobrar $300.00
                params.put("telefono", "6623514665");  //descripcion de la compra
                params.put("descripcion", "["+Viaje.pasajeroArrayList.get(1).getReferencia()+"]");  //se genera al registrar tarjeta src_2hBFq5Mk2Q6hYc7Zqparams.put("monto", Viaje.getImporteTotal()+"00");  //se genera al registrar tarjeta src_2hBFq5Mk2Q6hYc7Zq
                params.put("monto", Viaje.getImporteTotal()+"00");  //se genera al registrar tarjeta src_2hBFq5Mk2Q6hYc7Zq


                Log.d("Log:","OXXO "+nombre + " "+ correo.getText()+ " " + telefono.getText() + " "+ Viaje.pasajeroArrayList.get(1).getReferencia());
                return params;
            }
        }).setTag(TAG);
    }

    private void MostrarDialogo() {

        final Dialog dialogMain;

        dialogMain = new Dialog(ActivityOxxo.this);
        dialogMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMain.setContentView(R.layout.dialogo_referencia_oxxo);

        Button aceptar = dialogMain.findViewById(R.id.botonAceptarDialogoOxxo);
        TextView referencia= dialogMain.findViewById(R.id.tv_referenciaOxxo);
        referencia.setText(refOxxo);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMain.dismiss();

                Intent intent = new Intent(ActivityOxxo.this, ActivityHistorial.class);
                startActivity(intent);
            }
        });
        dialogMain.show();
    }

}
