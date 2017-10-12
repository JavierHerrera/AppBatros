package appbatros.solutions.com.mx.appbatros.objetos;

import java.util.ArrayList;

import appbatros.solutions.com.mx.appbatros.extras.FormatoHorasFechas;

public class Salidas {

    private String horarioSalida;
    private String tipoCamion;
    private String acientosDisponibles;
    private String precio;
    private ArrayList<String> asientosOcupados;
    private String corrida;

    public Salidas(String horarioSalida, String tipocamion, String acientosdisponibles, String precio, ArrayList<String> asientosOcupados, String corrida){
        this.horarioSalida = horarioSalida;
        this.tipoCamion = tipocamion;
        this.acientosDisponibles = acientosdisponibles;
        this.asientosOcupados = asientosOcupados;
        this.corrida = corrida;
        this.precio = precio;

    }

    public String getHorarioSalidaMilitar() {
        return horarioSalida;
    }

    public  String getHorarioSalidaFormato24hr(){
        //Se cambia formato de horas
        FormatoHorasFechas formato = new FormatoHorasFechas();
        return formato.dosDigitosyAM_PM(getHorarioSalidaMilitar());
    }

    public String getTipoCamion() {
        return tipoCamion;
    }

    public String getAcientosDisponibles() {
        return acientosDisponibles;
    }

    public String getPrecio() {
        return precio;
    }

    public ArrayList<String> getAsientosOcupados() {
        return asientosOcupados;
    }

    public void setAsientosOcupados(ArrayList<String> asientosOcupados) {
        this.asientosOcupados = asientosOcupados;
    }

    public String getCorrida() {
        return corrida;
    }

    public void setCorrida(String corrida) {
        this.corrida = corrida;
    }
}
