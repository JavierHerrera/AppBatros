package appbatros.solutions.com.mx.appbatros;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import appbatros.solutions.com.mx.appbatros.adapters.AdapterAsiento;
import appbatros.solutions.com.mx.appbatros.extras.SingleToast;
import appbatros.solutions.com.mx.appbatros.interfaces.MyInterfaceActivityCamion;
import appbatros.solutions.com.mx.appbatros.objetos.Asiento;
import appbatros.solutions.com.mx.appbatros.objetos.Pasajero;
import appbatros.solutions.com.mx.appbatros.objetos.Viaje;

public class ActivityCamion extends AppCompatActivity implements MyInterfaceActivityCamion{

    private Dialog dialog_agregar, dialog_eliminar;
    List<Asiento> itemsIzq = new ArrayList<>();
    List<Asiento> itemsDer = new ArrayList<>();

    AdapterAsiento adapter1;
    AdapterAsiento adapter2;

    //Lista de asientos ocupados
    ArrayList<String> ocupados;

    //Elementos del dialogo
    ImageView iconPasajero1,iconPasajero2,iconPasajero3,iconPasajero4;
    LinearLayout layoutPasajero1Dialog, layoutPasajero2Dialog, layoutPasajero3Dialog, layoutPasajero4Dialog;
    int asientosElejidos;

    //Se valida el boolean para que aparescan los datos del pasajero en la parte superior
    Boolean seleccionado_p1,seleccionado_p2,seleccionado_p3,seleccionado_p4;

    //Se utiliza para ver cual asiento se va a cargar
    int numeroDeAsientoSeleccionado;

    //Se copia le objeto para cargar los datos en la parte superior(resumen)
    Pasajero pasajero_seleccionado;

    //Elementos de los asientos seleccionados
    LinearLayout layout_asiento1, layout_asiento2, layout_asiento3, layout_asiento4;
    ImageView icon_asiento1, icon_asiento2, icon_asiento3,icon_asiento4;
    TextView nombre_asiento1, nombre_asiento2, nombre_asiento3, nombre_asiento4;
    TextView numero_asiento1, numero_asiento2, numero_asiento3, numero_asiento4;
    EditText editText_nombre;

    //Boton para ir a la siguietne activity
    Button botonContinuar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camion);

        Intent parentIntent = getIntent();
        ocupados = parentIntent.getStringArrayListExtra("Ocupados");

        cargarActionBar();
        cargarCabecera();
        cargarAsientos();

        asientosElejidos = 0;

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

        //Elementos de la cabecera donde aparecen lso asientos
        layout_asiento1 = (LinearLayout) findViewById(R.id.layout_Asiento1);
        layout_asiento2 = (LinearLayout) findViewById(R.id.layout_Asiento2);
        layout_asiento3 = (LinearLayout) findViewById(R.id.layout_Asiento3);
        layout_asiento4 = (LinearLayout) findViewById(R.id.layout_Asiento4);

        icon_asiento1 = (ImageView) findViewById(R.id.imageView_IconAsiento1);
        icon_asiento2 = (ImageView) findViewById(R.id.imageView_IconAsiento2);
        icon_asiento3 = (ImageView) findViewById(R.id.imageView_IconAsiento3);
        icon_asiento4 = (ImageView) findViewById(R.id.imageView_IconAsiento4);

        nombre_asiento1 = (TextView) findViewById(R.id.imageview_NombreAsiento1);
        nombre_asiento2 = (TextView) findViewById(R.id.imageview_NombreAsiento2);
        nombre_asiento3 = (TextView) findViewById(R.id.imageview_NombreAsiento3);
        nombre_asiento4 = (TextView) findViewById(R.id.imageview_NombreAsiento4);

        numero_asiento1 = (TextView) findViewById(R.id.textView_NumeroAsiento1);
        numero_asiento2 = (TextView) findViewById(R.id.textView_NumeroAsiento2);
        numero_asiento3 = (TextView) findViewById(R.id.textView_NumeroAsiento3);
        numero_asiento4 = (TextView) findViewById(R.id.textView_NumeroAsiento4);

        botonContinuar = (Button) findViewById(R.id.button_buscarActivo_main);

    }

    private void cargarActionBar() {

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text_titulo);

        if (Viaje.getTotalPasajeros() == 1) {
            mTitleTextView.setText("Seleccione asiento");
        }else{
            mTitleTextView.setText("Seleccione asientos");
        }


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

    public void cargarCabecera(){

        TextView origen, destino, hora,dia,mes;

        origen = (TextView) findViewById(R.id.textVie_origen_camion);
        destino = (TextView) findViewById(R.id.textView_destino_camion);
        hora = (TextView) findViewById(R.id.textView_horaCamion);
        dia = (TextView) findViewById(R.id.textView_diaCamion);
        mes = (TextView) findViewById(R.id.textView_mesCamion);

        origen.setText(Viaje.getOrigen());
        destino.setText(Viaje.getDestino());
        hora.setText(Viaje.getHoraSalidaFormato12());
        dia.setText(Viaje.getFechaSalidaDiaNumero());
        mes.setText(Viaje.getFechaSalidaMes());

    }

    @Override
    public void adapterCamion(Asiento asiento, int position) {
        clickAsiento(asiento,position);
    }

    private void clickAsiento(final Asiento asiento, final int position) {

        //Validar asiento disponible y que falten pasajeros por otmar asiento
        if (Objects.equals(asiento.getStatus(), "disponible")) {

            if (asientosElejidos < Viaje.getTotalPasajeros()) {

                //Abrir Dialogo
                dialog_agregar = new Dialog(ActivityCamion.this);
                dialog_agregar.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog_agregar.setContentView(R.layout.dialogo_agregar_asiento);

                numeroDeAsientoSeleccionado = 0;

                editText_nombre = dialog_agregar.findViewById(R.id.editText_NombrePasajeroDialogo);

                //Iniciazializar imagen de icono y texto
                layoutPasajero1Dialog = dialog_agregar.findViewById(R.id.layout_pasajero1Camion);
                layoutPasajero2Dialog = dialog_agregar.findViewById(R.id.layout_pasajero2Camion);
                layoutPasajero3Dialog = dialog_agregar.findViewById(R.id.layout_pasajero3Camion);
                layoutPasajero4Dialog = dialog_agregar.findViewById(R.id.layout_pasajero4Camion);

                iconPasajero1 = dialog_agregar.findViewById(R.id.icon_pasajero1Camion);
                iconPasajero2 = dialog_agregar.findViewById(R.id.icon_pasajero2Camion);
                iconPasajero3 = dialog_agregar.findViewById(R.id.icon_pasajero3Camion);
                iconPasajero4 = dialog_agregar.findViewById(R.id.icon_pasajero4Camion);

                TextView textViewPasajero1 = dialog_agregar.findViewById(R.id.textView_pasajero1Camion);
                TextView textViewPasajero2 = dialog_agregar.findViewById(R.id.textView_pasajero2Camion);
                TextView textViewPasajero3 = dialog_agregar.findViewById(R.id.textView_pasajero3Camion);
                TextView textViewPasajero4 = dialog_agregar.findViewById(R.id.textView_pasajero4Camion);

                //Inicializar botones de aceptar y cancelar
                Button aceptar = dialog_agregar.findViewById(R.id.botonAceptarDialogoAsientos);
                Button cancelar = dialog_agregar.findViewById(R.id.botonCancelarDialogoAsientos);

                //Cargar Iconos disponibles de cada pasajero
                cargarIconos(Viaje.pasajeroArrayList.get(1), layoutPasajero1Dialog, iconPasajero1, textViewPasajero1, seleccionado_p1);
                cargarIconos(Viaje.pasajeroArrayList.get(2), layoutPasajero2Dialog, iconPasajero2, textViewPasajero2, seleccionado_p2);
                cargarIconos(Viaje.pasajeroArrayList.get(3), layoutPasajero3Dialog, iconPasajero3, textViewPasajero3, seleccionado_p3);
                cargarIconos(Viaje.pasajeroArrayList.get(4), layoutPasajero4Dialog, iconPasajero4, textViewPasajero4, seleccionado_p4);

                cambiarColorIconoAzulTipoPasajeroAutomatico();

                TextView tipoPasajeroDialogo = (TextView) dialog_agregar.findViewById(R.id.textView_nombrePasajero_dialogoAgregar);
                tipoPasajeroDialogo.setText("Ingrese el nombre del "+pasajero_seleccionado.getTipo().replace("nino", "niño"));

                //Se muestra el dialogo
                dialog_agregar.show();

                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_agregar.dismiss();
                    }
                });

                mostrarTeclado(editText_nombre);

                aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (editText_nombre.length() > 0) {

                            //Cambiar color a de asiento(azul)
                            asiento.setImagen(R.drawable.asiento_seleccionado);
                            actualizarVista(asiento, position);
                            asiento.setStatus("seleccionado");
                            cargarDatosPasajeroEnResumen(pasajero_seleccionado, asiento.getNumeroAsiento());
                            asientosElejidos = asientosElejidos + 1;
                            aparecerBotonContinuar();

                            dialog_agregar.dismiss();

                        }else{
                            SingleToast.show(ActivityCamion.this,"Ingrece el nombre del pasajero",Toast.LENGTH_SHORT);

                        }

                    }
                });
            }else {
                SingleToast.show(this,"Ya seleccionaste todos tus asientos",Toast.LENGTH_SHORT);
            }

        }else if(Objects.equals(asiento.getStatus(), "ocupado")){

            SingleToast.show(this,"El asiento "+ asiento.getNumeroAsiento()+ " esta ocupado",Toast.LENGTH_SHORT);
        }
        else if(Objects.equals(asiento.getStatus(), "seleccionado")){

            //Abrir Dialogo
            dialog_eliminar = new Dialog(ActivityCamion.this);
            dialog_eliminar.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog_eliminar.setContentView(R.layout.dialogo_eliminar_asiento);

            dialog_eliminar.show();

            Button aceptar = (Button) dialog_eliminar.findViewById(R.id.botonAceptarEliminarAsiento);
            Button cancelar = (Button) dialog_eliminar.findViewById(R.id.botonCaneclarEliminarAsiento);


            cancelar.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialog_eliminar.dismiss();
                }
            });

            aceptar.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //Cambiar color a de asiento(verde)
                    borrarAsiento(asiento, asiento.getNumeroAsiento(),position);


                    dialog_eliminar.dismiss();
                }
            });

            dialog_eliminar.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {ocultarTeclado();
                }
            });
        }
    }

    private  List<Asiento> lado(int numAsiento) {
        if (numAsiento % 4 == 0 || numAsiento % 4 == 1) {
            return itemsIzq;
        } else {
            return itemsDer;
        }
    }

    private void cargarDatosPasajeroEnResumen(Pasajero pasajero_seleccionado, int numAsiento) {

        if (numeroDeAsientoSeleccionado == 1 && !seleccionado_p1){

            layout_asiento1.setVisibility(View.VISIBLE);
            nombre_asiento1.setText(editText_nombre.getText());
            Viaje.pasajeroArrayList.get(1).setNombre(String.valueOf(editText_nombre.getText()));
            icon_asiento1.setImageResource(pasajero_seleccionado.getIcon_seleccionado());
            numero_asiento1.setText(String.valueOf(numAsiento));
            Viaje.pasajeroArrayList.get(1).setNumero_asiento(numAsiento);

            seleccionado_p1 = true;
        }
        else if (numeroDeAsientoSeleccionado == 2 && !seleccionado_p2){

            layout_asiento2.setVisibility(View.VISIBLE);
            nombre_asiento2.setText(editText_nombre.getText());
            Viaje.pasajeroArrayList.get(2).setNombre(String.valueOf(editText_nombre.getText()));
            icon_asiento2.setImageResource(pasajero_seleccionado.getIcon_seleccionado());
            numero_asiento2.setText(String.valueOf(numAsiento));
            Viaje.pasajeroArrayList.get(2).setNumero_asiento(numAsiento);

            seleccionado_p2 = true;
        }
        else if (numeroDeAsientoSeleccionado == 3 && !seleccionado_p3){

            layout_asiento3.setVisibility(View.VISIBLE);
            nombre_asiento3.setText(editText_nombre.getText());
            Viaje.pasajeroArrayList.get(3).setNombre(String.valueOf(editText_nombre.getText()));
            icon_asiento3.setImageResource(pasajero_seleccionado.getIcon_seleccionado());
            numero_asiento3.setText(String.valueOf(numAsiento));
            Viaje.pasajeroArrayList.get(3).setNumero_asiento(numAsiento);

            seleccionado_p3 = true;
        }
        else if (numeroDeAsientoSeleccionado == 4 && !seleccionado_p4){

            layout_asiento4.setVisibility(View.VISIBLE);
            nombre_asiento4.setText(editText_nombre.getText());
            Viaje.pasajeroArrayList.get(4).setNombre(String.valueOf(editText_nombre.getText()));
            icon_asiento4.setImageResource(pasajero_seleccionado.getIcon_seleccionado());
            numero_asiento4.setText(String.valueOf(numAsiento));
            Viaje.pasajeroArrayList.get(4).setNumero_asiento(numAsiento);

            seleccionado_p4 = true;
        }
    }

    private void cargarIconos(Pasajero pasajero, LinearLayout layout, ImageView icon, TextView tipo, Boolean seleccionado) {

        if(pasajero.getTipo() != null && !seleccionado){
            layout.setVisibility(View.VISIBLE);
            icon.setImageResource(pasajero.getIcono());
            tipo.setText(pasajero.getTipo().replace("nino", "niño"));
        }
    }

    private void cambiarColorIconoAzulTipoPasajeroAutomatico() {

        if (Viaje.pasajeroArrayList.get(1).getTipo() != null && !seleccionado_p1) {

            iconPasajero1.setImageResource(Viaje.pasajeroArrayList.get(1).getIcon_seleccionado());
            pasajero_seleccionado = Viaje.pasajeroArrayList.get(1);
            numeroDeAsientoSeleccionado = 1;

        }
        else if (Viaje.pasajeroArrayList.get(2).getTipo() != null && !seleccionado_p2) {

            iconPasajero2.setImageResource(Viaje.pasajeroArrayList.get(2).getIcon_seleccionado());
            pasajero_seleccionado = Viaje.pasajeroArrayList.get(2);
            numeroDeAsientoSeleccionado = 2;
        }
        else if (Viaje.pasajeroArrayList.get(3).getTipo() != null && !seleccionado_p3) {

            iconPasajero3.setImageResource(Viaje.pasajeroArrayList.get(3).getIcon_seleccionado());
            pasajero_seleccionado = Viaje.pasajeroArrayList.get(3);
            numeroDeAsientoSeleccionado = 3;
        }
        else if (Viaje.pasajeroArrayList.get(4).getTipo() != null && !seleccionado_p4) {

            iconPasajero4.setImageResource(Viaje.pasajeroArrayList.get(4).getIcon_seleccionado());
            pasajero_seleccionado = Viaje.pasajeroArrayList.get(4);
            numeroDeAsientoSeleccionado = 4;        }
    }

    private void actualizarVista(Asiento asiento, int position){
        int numA = asiento.getNumeroAsiento() - 1;
        lado(numA).remove(position);
        lado(numA).add(position,asiento);
        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
    }

    private void cargarAsientos(){
        for (int i = 0; i < 42; i++) {

            if(!ocuparAsiento(i+1)){

                //Agregar tele
                if (i+1 == 1 || i+1 ==4  || i+1 == 13 || i+1 == 28 || i+1 == 37) {
                    lado(i).add(new Asiento(R.drawable.asiento_disponible, i + 1, "disponible", R.drawable.tv));
                }else{
                    lado(i).add(new Asiento(R.drawable.asiento_disponible, i + 1, "disponible", R.drawable.no_tv));
                }

            }else{

                //Agregar tele
                if (i+1 == 1 || i+1 ==4  || i+1 == 13 || i+1 == 28 || i+1 == 37) {
                    lado(i).add(new Asiento(R.drawable.asiento_ocupado, i + 1, "ocupado", R.drawable.tv));
                }else{
                    lado(i).add(new Asiento(R.drawable.asiento_ocupado, i + 1, "ocupado", R.drawable.no_tv));
                }
            }
        }
    }

    private void borrarAsiento(Asiento asiento, int numeroAsiento, int position){

        asiento.setImagen(R.drawable.asiento_disponible);
        asiento.setStatus("disponible");
        actualizarVista(asiento,position);
        asientosElejidos = asientosElejidos -1;

        aparecerBotonContinuar();

        if (Viaje.pasajeroArrayList.get(1).getPasajeroPorNumeroAsiento(numeroAsiento)){
            layout_asiento1.setVisibility(View.GONE);
            Viaje.pasajeroArrayList.get(1).setNumero_asiento(0);
            Viaje.pasajeroArrayList.get(1).setNombre("");
            seleccionado_p1 = false;
        }
        else if (Viaje.pasajeroArrayList.get(2).getPasajeroPorNumeroAsiento(numeroAsiento)){
            layout_asiento2.setVisibility(View.GONE);
            Viaje.pasajeroArrayList.get(2).setNumero_asiento(0);
            Viaje.pasajeroArrayList.get(2).setNombre("");
            seleccionado_p2 = false;

        }
        else if (Viaje.pasajeroArrayList.get(3).getPasajeroPorNumeroAsiento(numeroAsiento)){
            layout_asiento3.setVisibility(View.GONE);
            Viaje.pasajeroArrayList.get(3).setNumero_asiento(0);
            Viaje.pasajeroArrayList.get(3).setNombre("");
            seleccionado_p3 = false;

        }
        else if (Viaje.pasajeroArrayList.get(4).getPasajeroPorNumeroAsiento(numeroAsiento)){
            layout_asiento4.setVisibility(View.GONE);
            Viaje.pasajeroArrayList.get(4).setNumero_asiento(0);
            Viaje.pasajeroArrayList.get(4).setNombre("");
            seleccionado_p4 = false;
        }
    }

    private boolean ocuparAsiento(int numero){

        for (int i = 0; i < ocupados.size(); i++){

            if (numero == Integer.parseInt(ocupados.get(i))){
                return true;
            }

        }
        return false;
    }

    private void aparecerBotonContinuar(){

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView_camion);

        //Se agregan 60dp de margen bottom( pero se ponen 120 no se por que)
        //esto al aparecer el bonton para ajustar la pantalla y se vean los ultimos asientos
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) scrollView.getLayoutParams();
        params.bottomMargin = 120;

        if (asientosElejidos == Viaje.getTotalPasajeros()) {

            Animation animation = new TranslateAnimation(0, 0, 130, 0);
            animation.setDuration(700);
            animation.setInterpolator(new DecelerateInterpolator());
            botonContinuar.startAnimation(animation);
            botonContinuar.setVisibility(View.VISIBLE);
        }else{
            params.bottomMargin = 0;
            botonContinuar.setVisibility(View.GONE);
        }
    }

    public void cambiarActivityResumen(View view) {

        Intent intent = new Intent(this, ActivityResumen.class);
        startActivity(intent);
    }

    public void goHome() {

        Intent intent = new Intent(this, ActivityMain.class);
        startActivity(intent);
    }



    private void mostrarTeclado(EditText editText) {
        try {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(InputMethodManager.SHOW_FORCED,
                            InputMethodManager.HIDE_IMPLICIT_ONLY);
            if (editText != null) editText.requestFocus();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void ocultarTeclado() {
        try {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            if ((getCurrentFocus() != null) && (getCurrentFocus().getWindowToken() != null)) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
