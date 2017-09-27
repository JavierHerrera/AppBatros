package appbatros.solutions.com.mx.appbatros.extras;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import appbatros.solutions.com.mx.appbatros.objetos.Viaje;

/**
 * Created by JH on 13/09/2017.
 */

public class FormatoHorasFechas {

    public String dosDigitosyAM_PM(String hora){

        //K:mm  para que aparesca en formato de 24h la "a" es para am pm
        String rawTimestamp = hora; // For example
        DateTimeFormatter inputFormatter = DateTimeFormat.forPattern("HHmm");
        DateTimeFormatter outputFormatter = DateTimeFormat.forPattern("hh:mm a");
        DateTime dateTime = inputFormatter.parseDateTime(rawTimestamp);
        String formattedTimestamp = outputFormatter.print(dateTime.getMillis());
        return formattedTimestamp;

    }

    public String sacarDiaSemana(int ano, int mes, int dia){

        Calendar cal = Calendar.getInstance();
        cal.set(ano,mes,dia);
        String diaSemana = String.valueOf(cal.getDisplayName(cal.DAY_OF_WEEK,Calendar.LONG, Locale.getDefault()));

        return diaSemana;
    }

    public String horadeHoyFormatoMilitar(){
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");

        return sdf.format(new Date());
    }

    public String validarHora(){

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        int mYear = Integer.parseInt(Viaje.getFechaSalidaYear());
        int mMonth = Integer.parseInt(Viaje.getFechaSalidaMes());
        int mDay = Integer.parseInt(Viaje.getFechaSalidaDiaNumero());


        Log.d("Log",""+year+mYear+month+mMonth+day+mDay);
        if (year == mYear && month == mMonth && day == mDay){
            return horadeHoyFormatoMilitar();


            }
        return "0000";
    }

    public String primeraLetraMayuscula(String string){
        return string.substring(0,1).toUpperCase() + string.substring(1);
    }
}