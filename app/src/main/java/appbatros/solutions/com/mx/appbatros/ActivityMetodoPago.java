package appbatros.solutions.com.mx.appbatros;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import appbatros.solutions.com.mx.appbatros.DB.ConektaDB;
import appbatros.solutions.com.mx.appbatros.DB.HistorialDB;
import appbatros.solutions.com.mx.appbatros.objetos.Viaje;

//import de paypal


public class ActivityMetodoPago extends AppCompatActivity {



    //VARIABLES DE PAYPAL
    // private static final String TAG = "paymentdemoblog";
    /**
     * - Set to PaymentActivity.ENVIRONMENT_PRODUCTION to move real money.
     *
     * - Set to PaymentActivity.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    // private static final String CONFIG_ENVIRONMENT =
    // PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    // note that these credentials will differ between live & sandbox
    // environments.
    private static final String CONFIG_CLIENT_ID = "AbBg9nfZz-ckcpBKUNuYYQk-9dqulHtmIvejqq49Xp4YT7cnryPRbawzGXRI5FdRU3AmxYRfhPMdQIvJ";


    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

    private static PayPalConfiguration config  = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            .acceptCreditCards(false)
            // the following are only used in PayPalFuturePaymentActivity.
            .merchantName("Hipster Store")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    PayPalPayment thingToBuy;
    //TERMINAN VARIABLES DE PAYPAL

    //Variables Requeridas para metodo POST
    private RequestQueue requestQueue;
    final String TAG = "ResumenLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metodo_pago);

       cargarActionBar();
        requestQueue = Volley.newRequestQueue(this);

    }

    private void cargarActionBar() {

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text_titulo);
        mTitleTextView.setText("Metodos de pago");

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

    }

    //METODOS DE PAYPAL
    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(ActivityMetodoPago.this,
                PayPalFuturePaymentActivity.class);

        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode,  int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        System.out.println(confirm.toJSONObject().toString(4));
                        System.out.println(confirm.getPayment().toJSONObject()
                                .toString(4));

                        Toast.makeText(getApplicationContext(), "Pago Aceptado",
                                Toast.LENGTH_LONG).show();

                        //Mandar confirmacion de pago realizado de cada boleto
                        for (int i = 1; i <=Viaje.getTotalPasajeros() ; i++) {
                            aprobarPago(Viaje.pasajeroArrayList.get(i).getReferencia(), "Paypal",i);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                System.out
                        .println("An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth = data
                        .getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject()
                                .toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(getApplicationContext(),
                                "Future Payment code received from PayPal",
                                Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample",
                                "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

    }

    public void onFuturePaymentPurchasePressed(View pressed) {
        // Get the Application Correlation ID from the SDK
        String correlationId = PayPalConfiguration
                .getApplicationCorrelationId(this);

        Log.i("FuturePaymentExample", "Application Correlation ID: "
                + correlationId);

        // TODO: Send correlationId and transaction details to your server for
        // processing with
        // PayPal...
        Toast.makeText(getApplicationContext(),
                "App Correlation ID received from SDK", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    public void cambiarActivityPaypal(View view) {

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        thingToBuy = new PayPalPayment(new BigDecimal(Viaje.getImporteTotal()), "USD",
                "Total", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent1 = new Intent(ActivityMetodoPago.this,
                PaymentActivity.class);

        intent1.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        intent1.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        startActivityForResult(intent1, REQUEST_CODE_PAYMENT);

    }

    //Probar pago y registrando del tipo del mismo
    private void aprobarPago(String referencia, String tipoPago, int numeroOperacion) {

        mandarPOSTdePagoAprovado(referencia,tipoPago, numeroOperacion);

        HistorialDB  myDB = new HistorialDB(this);
        myDB.actualizarPago(referencia , tipoPago);

    }

    private void mandarPOSTdePagoAprovado(final String id, final String tipoPago, int total) {

        requestQueue.add(new StringRequest(Request.Method.POST,
                "http://198.199.102.31:4000/api/buses/boleto/update",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY", response);
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
                params.put("id",id);  //id del boleto que se obtiene cuando este se aparta

                return params;
            }
        }).setTag(TAG);
    }

    @Override
    protected void onStop() {
        if (requestQueue != null) requestQueue.cancelAll(TAG);
        super.onStop();
    }

    //METODO DE PAGO CON TARJETA CREDIDTO/DEBITO
    public void cambiarActivityAgregarTarjeta(View view) {



        ConektaDB myDB = new ConektaDB(this);
        Cursor cursor = myDB.selectRecords() ;

        if (cursor.getCount() > 0)
        {
            Intent intent = new Intent(this, ActivityAgregarTarjeta.class);
            startActivity(intent);
            Log.d("cursor", ""+cursor.getCount());

        }
        else {
            Intent intent = new Intent(this, ActivityRegsistrarConektaID.class);
            startActivity(intent);
            Log.d("cursor", ""+cursor.getCount());

        }

    }

    public void goHome() {

        Intent intent = new Intent(this, ActivityMain.class);
        startActivity(intent);
    }

    public void cambiarActivityOxxo(View view) {
        Intent intent = new Intent(this, ActivityOxxo.class);
        startActivity(intent);
    }
}
