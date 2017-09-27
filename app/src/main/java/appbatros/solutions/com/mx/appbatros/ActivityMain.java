package appbatros.solutions.com.mx.appbatros;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import appbatros.solutions.com.mx.appbatros.extras.FormatoHorasFechas;
import appbatros.solutions.com.mx.appbatros.extras.SingleToast;
import appbatros.solutions.com.mx.appbatros.objetos.Pasajero;
import appbatros.solutions.com.mx.appbatros.objetos.Viaje;

public class ActivityMain extends AppCompatActivity implements
        View.OnClickListener {

    //CALENDARIO
    private EditText fecha;
    private Button btnDatePicker;
    private int mYear, mMonth, mDay, diaSemana;

    //Elementos del dialogo
    private Dialog dialogMain;
    private Spinner origen, destino;
    private int adultos,ninos,estudiantes,insen ,totalPasajeros;
    private TextView textViewNumeroAdultos,textViewNumeroNinos,textViewNumeroEstudiantes,textViewNumeroInsen;

    //Elementos del boton de pasajeros
    private LinearLayout layoutPasajero1, layoutPasajero2, layoutPasajero3, layoutPasajero4;

    //Lista de ciudades
    String[] ciudades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FormatoHorasFechas formato = new FormatoHorasFechas();
        Log.d("HORA",""+formato.horadeHoyFormatoMilitar());

        cargarActionBar();

         //Se inicializan pasajeros
        Viaje.pasajeroArrayList.add(new Pasajero(null,0,0));//se queda en blanco
        Viaje.pasajeroArrayList.add(new Pasajero(null,0,0));
        Viaje.pasajeroArrayList.add(new Pasajero(null,0,0));
        Viaje.pasajeroArrayList.add(new Pasajero(null,0,0));
        Viaje.pasajeroArrayList.add(new Pasajero(null,0,0));


        for (int i = 1; i <= 4; i++) {
            Log.d("Array",""+Viaje.pasajeroArrayList.get(i).getTipo());
        }

        adultos = 0;
        ninos = 0;
        estudiantes = 0;
        insen = 0;
        totalPasajeros = 0;

        //CALENDARIO
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        fecha =(EditText)findViewById(R.id.in_date);
        fecha.setText("");

        btnDatePicker.setOnClickListener(this);

        //Contenedores de los iconos
         layoutPasajero1 = (LinearLayout) findViewById(R.id.layout_pasajero1Main);
         layoutPasajero2 = (LinearLayout) findViewById(R.id.layout_pasajero2Main);
         layoutPasajero3 = (LinearLayout) findViewById(R.id.layout_pasajero3Main);
         layoutPasajero4 = (LinearLayout) findViewById(R.id.layout_pasajero4Main);

        //Inicializar array de ciudades
        ciudades = new String[]{"Seleccionar","HERMOSILLO","CABORCA","PUERTO PEÑASCO","NOGALES", "SANTANA",
                "MAGDALENA","LA Y","HUATABAMPO","ETCHOJOA","NAVOJOA","ALAMOS",
                "OBREGON", "GUAYMAS","EMPALME"};

        //Se crea spinner con hint
        crearSpinnerOrigen();
        crearSpinnerDestino();

        origen = (Spinner) findViewById(R.id.spinnerOrigenMain);
        destino = (Spinner) findViewById(R.id.spinnerDestinoMain);

        //Se valida la activacion de boton buscar
        validarDatosActivarBoton();
    }

    private void cargarActionBar() {

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView =  mCustomView.findViewById(R.id.title_text_titulo);

        mTitleTextView.setText("ALBATROS");

        ImageView backButton = mCustomView.findViewById(R.id.imageView_Back);
        backButton.setImageResource(R.drawable.icon_back);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageButton historialButton = mCustomView.findViewById(R.id.imageView_Home);
        historialButton.setImageResource(R.drawable.icon_historial);
        historialButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityMain.this, ActivityHistorial.class);
                startActivity(intent);
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

    }

    private void crearSpinnerOrigen() {

        // Get reference of widgets from XML layout
       origen = (Spinner) findViewById(R.id.spinnerOrigenMain);

        final List<String> ciudadesList = new ArrayList<>(Arrays.asList(ciudades));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,ciudadesList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        origen.setAdapter(spinnerArrayAdapter);

        origen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    validarDatosActivarBoton();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void crearSpinnerDestino() {

        // Get reference of widgets from XML layout
        destino = (Spinner) findViewById(R.id.spinnerDestinoMain);

        final List<String> plantsList = new ArrayList<>(Arrays.asList(ciudades));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,plantsList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        destino.setAdapter(spinnerArrayAdapter);

        destino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    validarDatosActivarBoton();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void seleccionarPasajeros(View view) {

        dialogMain = new Dialog(ActivityMain.this);
        dialogMain.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMain.setContentView(R.layout.dialogo_seleccion_pasajeros);

        Button botonAdultosMenos = (Button) dialogMain.findViewById(R.id.botonAdultosMenos);
        Button botonAdultosMas = (Button) dialogMain.findViewById(R.id.botonAdultosMas);
        Button botonNinosMenos = (Button) dialogMain.findViewById(R.id.botonNinosMenos);
        Button botonNinosMas = (Button) dialogMain.findViewById(R.id.botonNinosMas);
        Button botonEstudiantesMenos = (Button) dialogMain.findViewById(R.id.botonEstudiantesMenos);
        Button botonEstudiantesMas = (Button) dialogMain.findViewById(R.id.botonEstudiantesMas);
        Button botonInsenMenos = (Button) dialogMain.findViewById(R.id.botonInsenMenos);
        Button botonInsenMas = (Button) dialogMain.findViewById(R.id.botonInsenMas);
        Button botonCancelarDialogoPasajeros = (Button) dialogMain.findViewById(R.id.botonCancelarDialogoPasajeros);
        Button botonAceptarDialogoPasajeros = (Button) dialogMain.findViewById(R.id.botonAceptarDialogoPasajeros);

        textViewNumeroAdultos = (TextView) dialogMain.findViewById(R.id.textViewnumeroAdultos);
        textViewNumeroNinos = (TextView) dialogMain.findViewById(R.id.textViewnumeroNinos);
        textViewNumeroEstudiantes = (TextView) dialogMain.findViewById(R.id.textViewnumeroEstudiantes);
        textViewNumeroInsen = (TextView) dialogMain.findViewById(R.id.textViewnumeroInsen);

        //Iniciar valores de pasajeros
        textViewNumeroAdultos.setText(String.valueOf(adultos));
        textViewNumeroNinos.setText(String.valueOf(ninos));
        textViewNumeroEstudiantes.setText(String.valueOf(estudiantes));
        textViewNumeroInsen.setText(String.valueOf(insen));

        botonAdultosMenos.setOnClickListener(this);
        botonAdultosMas.setOnClickListener(this);
        botonNinosMenos.setOnClickListener(this);
        botonNinosMas.setOnClickListener(this);
        botonEstudiantesMenos.setOnClickListener(this);
        botonEstudiantesMas.setOnClickListener(this);
        botonInsenMenos.setOnClickListener(this);
        botonInsenMas.setOnClickListener(this);
        botonCancelarDialogoPasajeros.setOnClickListener(this);
        botonAceptarDialogoPasajeros.setOnClickListener(this);

        dialogMain.show();

        }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.botonAdultosMenos:

               restarPasajero(textViewNumeroAdultos);
                break;

            case R.id.botonAdultosMas:

               sumarPasajero(textViewNumeroAdultos);
                break;

            case R.id.botonNinosMenos:

               restarPasajero(textViewNumeroNinos);
                break;

            case R.id.botonNinosMas:

                sumarPasajero(textViewNumeroNinos);
                break;

            case R.id.botonEstudiantesMenos:

                restarPasajero(textViewNumeroEstudiantes);
                break;

            case R.id.botonEstudiantesMas:

                sumarPasajero(textViewNumeroEstudiantes);
                break;

            case R.id.botonInsenMenos:

                restarPasajero(textViewNumeroInsen);
                break;

            case R.id.botonInsenMas:
                sumarPasajero(textViewNumeroInsen);
                break;

            case R.id.botonCancelarDialogoPasajeros:

                totalPasajeros = adultos + ninos + estudiantes + insen;
                validarDatosActivarBoton();
                dialogMain.dismiss();
                break;

            case R.id.botonAceptarDialogoPasajeros:

                // Se obtiene el numero y tipo de pasajeros
                adultos = Integer.parseInt(textViewNumeroAdultos.getText().toString());
                ninos = Integer.parseInt(textViewNumeroNinos.getText().toString());
                estudiantes = Integer.parseInt(textViewNumeroEstudiantes.getText().toString());
                insen = Integer.parseInt(textViewNumeroInsen.getText().toString());

                totalPasajeros = adultos + ninos + estudiantes + insen;

                //Se reinician los iconos y datos de pasajeros
                borrarPasajerosAnteriores();

                //Se cargan los datos de pasajeros dependiento el numero de tipos
                cargarPasajeros(adultos,"adulto",R.drawable.icon_adulto,R.drawable.icon_adulto_seleccionado);
                cargarPasajeros(ninos,"nino",R.drawable.icon_ninos,R.drawable.icon_ninos_seleccionados);
                cargarPasajeros(estudiantes,"estudiante",R.drawable.icon_estudiante,R.drawable.icon_estudiante_seleccionado);
                cargarPasajeros(insen,"insen",R.drawable.icon_mayor,R.drawable.icon_mayor_seleccionado);

                //Cargar Iconos
                cargarIconosPasajeros();

                validarDatosActivarBoton();
                dialogMain.dismiss();
                break;
        }

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            fecha.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            Viaje.setFechaSalidaDia(String.valueOf(dayOfMonth));
                            Viaje.setFechaSalidaMes(String.valueOf(monthOfYear + 1));
                            Viaje.setFechaSalidaYear(String.valueOf(year));
                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.show();
        }

        Log.d("iconos", ""+Viaje.pasajeroArrayList.get(1).getTipo()+
                Viaje.pasajeroArrayList.get(2).getTipo()+
                Viaje.pasajeroArrayList.get(3).getTipo()+
                Viaje.pasajeroArrayList.get(4).getTipo()
        );
    }

    private int sumarPasajero(TextView textView){

        int pasajero = Integer.parseInt(textView.getText().toString());
        if ( pasajero <4 && totalPasajeros <4  ){
            pasajero = pasajero + 1;
            textView.setText(String.valueOf(pasajero));

            totalPasajeros = totalPasajeros +1;
            return pasajero;
        }
        else{
            // La clase singletoas evita la acumulasion de mensajes
            SingleToast.show(this, "El maximo es 4 pasajeros por operacion", Toast.LENGTH_LONG);
            return pasajero;
        }
    }

    private int restarPasajero(TextView textView){

        int pasajero = Integer.parseInt(textView.getText().toString());
        if ( pasajero >0){
            pasajero = pasajero - 1;
            textView.setText(String.valueOf(pasajero));

            totalPasajeros = totalPasajeros -1;
            return pasajero;
        }
        else{return pasajero;}
    }

    //Verifica todos los pasajeros y les agrega un icono si no tienen
    private void cargarPasajeros(int cantidad, String tipo, int icon, int icon_seleccionado) {

        for (int i = 1; i <= cantidad; i++) {
            if (Viaje.pasajeroArrayList.get(1).getTipo() == null) {
                Viaje.pasajeroArrayList.get(1).setTipo(tipo);
                Viaje.pasajeroArrayList.get(1).setIcono(icon);
                Viaje.pasajeroArrayList.get(1).setIcon_seleccionado(icon_seleccionado);

            } else if (Viaje.pasajeroArrayList.get(2).getTipo() == null) {
                Viaje.pasajeroArrayList.get(2).setTipo(tipo);
                Viaje.pasajeroArrayList.get(2).setIcono(icon);
                Viaje.pasajeroArrayList.get(2).setIcon_seleccionado(icon_seleccionado);


            } else if (Viaje.pasajeroArrayList.get(3).getTipo() == null) {
                Viaje.pasajeroArrayList.get(3).setTipo(tipo);
                Viaje.pasajeroArrayList.get(3).setIcono(icon);
                Viaje.pasajeroArrayList.get(3).setIcon_seleccionado(icon_seleccionado);


            } else if (Viaje.pasajeroArrayList.get(4).getTipo() == null) {
                Viaje.pasajeroArrayList.get(4).setTipo(tipo);
                Viaje.pasajeroArrayList.get(4).setIcono(icon);
                Viaje.pasajeroArrayList.get(4).setIcon_seleccionado(icon_seleccionado);

            }
        }
    }

    private void cargarIconosPasajeros(){

        if (Viaje.pasajeroArrayList.get(1).getTipo() != null) {
            ImageView imageView = (ImageView) findViewById(R.id.icon_pasajero1Main);
            layoutPasajero1.setVisibility(View.VISIBLE);
            imageView.setImageResource(Viaje.pasajeroArrayList.get(1).getIcono());
        }
        if (Viaje.pasajeroArrayList.get(2).getTipo() != null) {
            ImageView imageView = (ImageView) findViewById(R.id.icon_pasajero2Main);
            layoutPasajero2.setVisibility(View.VISIBLE);
            imageView.setImageResource(Viaje.pasajeroArrayList.get(2).getIcono());
        }
        if (Viaje.pasajeroArrayList.get(3).getTipo() != null) {
            ImageView imageView = (ImageView) findViewById(R.id.icon_pasajero3Main);
            layoutPasajero3.setVisibility(View.VISIBLE);
            imageView.setImageResource(Viaje.pasajeroArrayList.get(3).getIcono());
        }
        if (Viaje.pasajeroArrayList.get(4).getTipo() != null) {
            ImageView imageView = (ImageView) findViewById(R.id.icon_pasajero4Main);
            layoutPasajero4.setVisibility(View.VISIBLE);
            imageView.setImageResource(Viaje.pasajeroArrayList.get(4).getIcono());
        }
    }

    private void borrarPasajerosAnteriores(){

        //Se inicializa en null los datos de pasajero y Se resetea la visivilidad de iconos


            Viaje.pasajeroArrayList.set(1,new Pasajero(null,0,0));
            Viaje.pasajeroArrayList.set(2,new Pasajero(null,0,0));
            Viaje.pasajeroArrayList.set(3,new Pasajero(null,0,0));
            Viaje.pasajeroArrayList.set(4,new Pasajero(null,0,0));

            layoutPasajero1.setVisibility(View.GONE);
            layoutPasajero2.setVisibility(View.GONE);
            layoutPasajero3.setVisibility(View.GONE);
            layoutPasajero4.setVisibility(View.GONE);


    }

    private void validarDatosActivarBoton(){

        Button botonContinuarActivo = (Button) findViewById(R.id.button_buscarActivo_main);
        Button botonContinuarInactivo = (Button) findViewById(R.id.button_buscarInactivo_main);

        if (    origen.getSelectedItem()!="Seleccionar" &&
                destino.getSelectedItem() !="Seleccionar" &&
                origen.getSelectedItem() != destino.getSelectedItem() &&
                !Objects.equals(String.valueOf(fecha.getText()), "") &&
                totalPasajeros > 0

                ) {
            botonContinuarActivo.setVisibility(View.VISIBLE);
            botonContinuarInactivo.setVisibility(View.GONE);
         }
         else {
            botonContinuarActivo.setVisibility(View.GONE);
            botonContinuarInactivo.setVisibility(View.VISIBLE);
        }
    }

    public void mostrarCamposIncompletos(View view) {

        if(origen.getSelectedItem() == "Seleccionar"){
            SingleToast.show(this, "Seleccione origen", Toast.LENGTH_SHORT);
        }

        else if(destino.getSelectedItem() == "Seleccionar"){
            SingleToast.show(this, "Seleccione destino", Toast.LENGTH_SHORT);
        }

        else if(origen.getSelectedItem() == destino.getSelectedItem()){
            SingleToast.show(this, "Origen y destino son iguales", Toast.LENGTH_SHORT);
        }

        else if (Objects.equals(String.valueOf(fecha.getText()), "")) {
            SingleToast.show(this, "Seleccione una fecha", Toast.LENGTH_SHORT);
        }

        else if (totalPasajeros == 0) {
            SingleToast.show(this, "Seleccione los pasajeros", Toast.LENGTH_SHORT);

        }
    }

    public void goHorariosActivity(View view) throws ParseException {

        //Cargar origen, destino y fecha de pasajeros
        Viaje.setOrigen(origen.getSelectedItem().toString());
        Viaje.setDestino(destino.getSelectedItem().toString());
        Viaje.setFechaDiaSemana(String.valueOf(diaSemana));
        Viaje.setTotalPasajeros(totalPasajeros);

        Intent intent = new Intent(ActivityMain.this, ActivityHorarios.class);
        startActivity(intent);
    }

}


