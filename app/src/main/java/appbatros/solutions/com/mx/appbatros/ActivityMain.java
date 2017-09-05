package appbatros.solutions.com.mx.appbatros;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.Calendar;

import appbatros.solutions.com.mx.appbatros.objetos.Pasajero;
import appbatros.solutions.com.mx.appbatros.objetos.Pasajeros;

public class ActivityMain extends AppCompatActivity implements
        View.OnClickListener {

    //CALENDARIO
    private EditText txtDate;
    private Button btnDatePicker;
    private int mYear; private int mMonth; private int mDay;

    private Dialog dialog1;
    private Spinner origen, destino;
    private int adultos,ninos,estudiantes,insen ,totalPasajeros;
    private TextView textViewNumeroAdultos,textViewNumeroNinos,textViewNumeroEstudiantes,textViewNumeroInsen;
    private DateFormat fechaSalida;

    private LinearLayout layoutPasajero1, layoutPasajero2, layoutPasajero3, layoutPasajero4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Se inicializan pasajeros
        Pasajeros.pasajero1 = new Pasajero(null,0,0);
        Pasajeros.pasajero2 = new Pasajero(null,0,0);
        Pasajeros.pasajero3 = new Pasajero(null,0,0);
        Pasajeros.pasajero4 = new Pasajero(null,0,0);

        adultos = 0;
        ninos = 0;
        estudiantes = 0;
        insen = 0;
        totalPasajeros = 0;

        //CALENDARIO
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        txtDate=(EditText)findViewById(R.id.in_date);

        btnDatePicker.setOnClickListener(this);

        //Contenedores de los iconos
         layoutPasajero1 = (LinearLayout) findViewById(R.id.layout_pasajero1Main);
         layoutPasajero2 = (LinearLayout) findViewById(R.id.layout_pasajero2Main);
         layoutPasajero3 = (LinearLayout) findViewById(R.id.layout_pasajero3Main);
         layoutPasajero4 = (LinearLayout) findViewById(R.id.layout_pasajero4Main);

        //Spinners
        origen = (Spinner) findViewById(R.id.spinnerOrigenMain);
        destino = (Spinner) findViewById(R.id.spinnerDestinoMain);
    }

    public void seleccionarPasajeros(View view) {

        dialog1 = new Dialog(ActivityMain.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialogo_seleccion_pasajeros);

        Button botonAdultosMenos = (Button) dialog1.findViewById(R.id.botonAdultosMenos);
        Button botonAdultosMas = (Button) dialog1.findViewById(R.id.botonAdultosMas);
        Button botonNinosMenos = (Button) dialog1.findViewById(R.id.botonNinosMenos);
        Button botonNinosMas = (Button) dialog1.findViewById(R.id.botonNinosMas);
        Button botonEstudiantesMenos = (Button) dialog1.findViewById(R.id.botonEstudiantesMenos);
        Button botonEstudiantesMas = (Button) dialog1.findViewById(R.id.botonEstudiantesMas);
        Button botonInsenMenos = (Button) dialog1.findViewById(R.id.botonInsenMenos);
        Button botonInsenMas = (Button) dialog1.findViewById(R.id.botonInsenMas);
        Button botonCancelarDialogoPasajeros = (Button) dialog1.findViewById(R.id.botonCancelarDialogoPasajeros);
        Button botonAceptarDialogoPasajeros = (Button) dialog1.findViewById(R.id.botonAceptarDialogoPasajeros);

        textViewNumeroAdultos = (TextView) dialog1.findViewById(R.id.textViewnumeroAdultos);
        textViewNumeroNinos = (TextView) dialog1.findViewById(R.id.textViewnumeroNinos);
        textViewNumeroEstudiantes = (TextView) dialog1.findViewById(R.id.textViewnumeroEstudiantes);
        textViewNumeroInsen = (TextView) dialog1.findViewById(R.id.textViewnumeroInsen);

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

        dialog1.show();

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
                dialog1.dismiss();
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

                //Se cargan los datos de pasajeros
                cargarPasajeros(adultos,"adulto",R.drawable.icon_adulto,R.drawable.icon_adulto_seleccionado);
                cargarPasajeros(ninos,"nino",R.drawable.icon_ninos,R.drawable.icon_ninos_seleccionados);
                cargarPasajeros(estudiantes,"estudiante",R.drawable.icon_estudiante,R.drawable.icon_estudiante_seleccionado);
                cargarPasajeros(insen,"insen",R.drawable.icon_mayor,R.drawable.icon_mayor_seleccionado);

                //Cargar Iconos
                cargarIconosPasajeros();

                dialog1.dismiss();
                break;
        }

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.show();
        }
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

    public void goHorariosActivity(View view) throws ParseException {

        if (totalPasajeros == 0) {
            SingleToast.show(this, "Seleccione los pasajeros", Toast.LENGTH_LONG);
        }
        else{

            //Cargar origen, destino y fecha de pasajeros
            Pasajeros.setOrigen(origen.getSelectedItem().toString());
            Pasajeros.setDestino(destino.getSelectedItem().toString());
            Pasajeros.setFechaSalidaD(String.valueOf(mDay));
            Pasajeros.setFechaSalidaM(String.valueOf(getMonthForInt(mMonth)));
            Pasajeros.setFechaSalidaY(String.valueOf(mYear));

            //SingleToast.show(this,""+Pasajeros.getFechaSalidaD()+Pasajeros.getFechaSalidaM()+Pasajeros.getFechaSalidaY(), Toast.LENGTH_LONG);


            Intent intent = new Intent(this,ActivityHorarios.class);
            startActivity(intent);
        }
    }

    private void cargarPasajeros(int cantidad, String tipo, int icon, int icon_seleccionado) {

        for (int i = 0; i < cantidad; i++) {

            if (Pasajeros.pasajero1.getTipo() == null) {

                Pasajeros.pasajero1.setTipo(tipo);
                Pasajeros.pasajero1.setIcono(icon);
                Pasajeros.pasajero1.setIcon_seleccionado(icon_seleccionado);

            } else if (Pasajeros.pasajero2.getTipo() == null) {

                Pasajeros.pasajero2.setTipo(tipo);
                Pasajeros.pasajero2.setIcono(icon);
                Pasajeros.pasajero2.setIcon_seleccionado(icon_seleccionado);

           } else if (Pasajeros.pasajero3.getTipo() == null) {

                Pasajeros.pasajero3.setTipo(tipo);
                Pasajeros.pasajero3.setIcono(icon);
                Pasajeros.pasajero3.setIcon_seleccionado(icon_seleccionado);

            } else if (Pasajeros.pasajero4.getTipo() == null) {

                Pasajeros.pasajero4.setTipo(tipo);
                Pasajeros.pasajero4.setIcono(icon);
                Pasajeros.pasajero4.setIcon_seleccionado(icon_seleccionado);
            }
        }
    }

    private void cargarIconosPasajeros(){

        if (Pasajeros.pasajero1.getTipo() != null){
            ImageView imageView = (ImageView) findViewById(R.id.icon_pasajero1Main);

            layoutPasajero1.setVisibility(View.VISIBLE);
            imageView.setImageResource(Pasajeros.pasajero1.getIcono());
        }

        if (Pasajeros.pasajero2.getTipo() != null){
            ImageView imageView = (ImageView) findViewById(R.id.icon_pasajero2Main);

            layoutPasajero2.setVisibility(View.VISIBLE);
            imageView.setImageResource(Pasajeros.pasajero2.getIcono());
        }

        if (Pasajeros.pasajero3.getTipo() != null){
            ImageView imageView = (ImageView) findViewById(R.id.icon_pasajero3Main);

            layoutPasajero3.setVisibility(View.VISIBLE);
            imageView.setImageResource(Pasajeros.pasajero3.getIcono());
        }

        if (Pasajeros.pasajero4.getTipo() != null){
            ImageView imageView = (ImageView) findViewById(R.id.icon_pasajero4Main);

            layoutPasajero4.setVisibility(View.VISIBLE);
            imageView.setImageResource(Pasajeros.pasajero4.getIcono());
        }
    }

    private void borrarPasajerosAnteriores(){

        //Se inicializa en null los datos de pasajero
        Pasajeros.pasajero1 = new Pasajero(null,0,0);
        Pasajeros.pasajero2 = new Pasajero(null,0,0);
        Pasajeros.pasajero3 = new Pasajero(null,0,0);
        Pasajeros.pasajero4 = new Pasajero(null,0,0);

        //Se resetea la visivilidad de iconos
        layoutPasajero1.setVisibility(View.GONE);
        layoutPasajero2.setVisibility(View.GONE);
        layoutPasajero3.setVisibility(View.GONE);
        layoutPasajero4.setVisibility(View.GONE);

    }

    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    String getDayForInt(int num) {
        String day = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] days = dfs.getWeekdays();
        if (num >= 0 && num <= 31 ) {
            day = days[num];
        }
        return day;
    }
}


