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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import appbatros.solutions.com.mx.appbatros.DB.ConektaDB;
import appbatros.solutions.com.mx.appbatros.extras.SingleToast;
import appbatros.solutions.com.mx.appbatros.objetos.Viaje;

public class ActivityRegsistrarConektaID extends AppCompatActivity {

    EditText nombre,correo,telefono;

    //Variables Requeridas para metodo POST
    private RequestQueue requestQueue;
    final String TAG = "ResumenLog";

    //Variables de contador
    TextView tiempo;
    private static final String FORMAT = "%02d:%02d";
    CountDownTimer contador;

    Dialog dialogSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regsistrar_conekta_id);

        crearDialogoSpinner();
        //5 minutos para finalziar la compra
        contador();

        cargarActionBar();

        nombre = (EditText) findViewById(R.id.et_nombre_RegistrarConektaID);
        correo = (EditText) findViewById(R.id.et_correo_RegistrarConektaID);
        telefono = (EditText) findViewById(R.id.et_telefono_RegistrarConektaID);

        requestQueue = Volley.newRequestQueue(this);
    }

    private void cargarActionBar() {

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text_titulo);
        mTitleTextView.setText("Registro ConektaID");


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

    public void validarDatosyGuardarlos(View view) {
        mostrarSpinner();
        mandarPOSTdePagoAprovado(String.valueOf(nombre.getText()), String.valueOf(correo.getText()), String.valueOf(telefono.getText()));


    }

    //METODO POST PARAR OBTENER ID TOKEN
    private void mandarPOSTdePagoAprovado(final String nombre, final String correo, final String telefono) {

        requestQueue.add(new StringRequest(Request.Method.POST,
                "http://198.199.102.31:4000/api/conekta/registro",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY", response);
                        quitarSpinnerBar();

                        try {

                            //Se obtiene el status, si es 1 guarda el id en DB local
                            JSONObject obj = new JSONObject(response);
                            String status = obj.getString("status");

                            if (Objects.equals(status, "1")){
                                //guardar en DB local los datos con el conekta_id
                                String conekta_id = obj.getString("conekta_id");
                                guardarDatosDB(conekta_id);

                                SingleToast.show(ActivityRegsistrarConektaID.this, "Datos guardados correctamente", Toast.LENGTH_LONG);

                                //Cambiar a registro de tarjeta

                                Intent intent = new Intent(ActivityRegsistrarConektaID.this, ActivityAgregarTarjeta.class);
                                startActivity(intent);

                            }else{
                                SingleToast.show(ActivityRegsistrarConektaID.this, "Datos incorrectos", Toast.LENGTH_LONG);
                            }


                        } catch (Throwable t) {
                            Log.e("VOLLEY", "Could not parse malformed JSON CONEKTA REGISTRO: \"" + response + "\"");
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

                params.put("nombre", nombre);                 //correo del cliente
                params.put("correo", correo);                 //telefono formato 6623404256
                params.put("telefono", telefono);               // telefono

/*              Datos Hardcore
                params.put("nombre", "nombr alguien");                 //correo del cliente
                params.put("correo", "alguien2@gmail.com");                 //telefono formato 6623404256
                params.put("telefono", "6623415660"); */                // telefono

                return params;
            }
        }).setTag(TAG);
    }

    public void guardarDatosDB(String conekta_id) {

        ConektaDB myDB = new ConektaDB(this);
        myDB.createRecords(
                myDB.selectRecords().getCount() + 1,    //ID
                String.valueOf(nombre.getText()),       //Nombre
                String.valueOf(correo.getText()),       //Correo
                String.valueOf(telefono.getText()),     //Telefono
                conekta_id);                            //conekta_id
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
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        contador.cancel();
        super.onBackPressed();
    }

    private void crearDialogoSpinner() {

        dialogSpinner = new Dialog(ActivityRegsistrarConektaID.this);
        dialogSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSpinner.setContentView(R.layout.dialog_spinner);
        dialogSpinner.setCanceledOnTouchOutside(false);
        dialogSpinner.setCancelable(false);
    }

    private void mostrarSpinner(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        dialogSpinner.show();

    }

    private void quitarSpinnerBar(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        dialogSpinner.cancel();
    }
}