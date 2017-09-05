package appbatros.solutions.com.mx.appbatros.objetos;

public class Salidas {

    private String horarioSalida;
    private String horarioLlegada;
    private String tipoCamion;
    private String acientosDisponibles;
    private String precio;

    public Salidas(String horarioSalida,String horarioLlegada,String tipocamion,String acientosdisponibles,  String precio){
        this.horarioSalida = horarioSalida;
        this.horarioLlegada = horarioLlegada;
        this.tipoCamion = tipocamion;
        this.acientosDisponibles = acientosdisponibles;

        this.precio = precio;

    }

    public String getHorarioSalida() {
        return horarioSalida;
    }

    public String getHorarioLlegada() {
        return horarioLlegada;
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
}
