package appbatros.solutions.com.mx.appbatros.objetos;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Objects;

import appbatros.solutions.com.mx.appbatros.extras.FormatoHorasFechas;

public class Viaje {

   // public static Pasajero pasajero1,pasajero2,pasajero3,pasajero4;
    private static String origen,destino;
    private static String horaSalida,  fechaSalidaDia, fechaSalidaMes, fechaSalidaYear, fechaDiaSemana;
    private static String corrida;
    private static String idBoleto;
    private static int totalPasajeros;
    public static ArrayList<Pasajero> pasajeroArrayList = new ArrayList<>(4);
    private static long tiempo = 300000;

    public static int getTotalAdultos(){
        int total= 0;
        for (int i = 1; i <=totalPasajeros ; i++) {
            if(Objects.equals(pasajeroArrayList.get(i).getTipo(), "adulto")){ total = total + 1;}
        }
        return total;
    }

    public static int getTotalEstudiantes(){
        int total= 0;
        for (int i = 1; i <=totalPasajeros ; i++) {
            if(Objects.equals(pasajeroArrayList.get(i).getTipo(), "estudiante")){ total = total + 1;}
        }
        return total;
    }

    public static int getTotalMenores(){
        int total= 0;
        for (int i = 1; i <=totalPasajeros ; i++) {
            if(Objects.equals(pasajeroArrayList.get(i).getTipo(), "nino")){ total = total + 1;}
        }
        return total;
    }

    public static int getTotalInsen(){
        int total= 0;
        for (int i = 1; i <=totalPasajeros ; i++) {
            if(Objects.equals(pasajeroArrayList.get(i).getTipo(), "insen")){ total = total + 1;}
        }
        return total;
    }

    public static String getOrigen() {
        return origen;
    }

    public static String getOrigenSiglas() {

        String siglas[] = {"HMO","CAB","PPE","NOG","SAN","MAG","LAY","HUA","ETC","NAV","ALA","OBR","GUA","EMP"};
        String descripcion[] = {"HERMOSILLO","CABORCA","PUERTO PEÑASCO","NOGALES", "SANTANA",
                "MAGDALENA","LA Y","HUATABAMPO","ETCHOJOA","NAVOJOA","ALAMOS",
                "OBREGON", "GUAYMAS","EMPALME"};

        int i =0;
        while (!descripcion[i].equals(getOrigen()) && i <= descripcion.length){
            i++;
        }

        return siglas[i];
    }

    public static String getDestinoSiglas() {

        String siglas[] = {"HMO","CAB","PPE","NOG","SAN","MAG","LAY","HUA","ETC","NAV","ALA","OBR","GUA","EMP"};
        String descripcion[] = {"HERMOSILLO","CABORCA","PUERTO PEÑASCO","NOGALES", "SANTANA",
                "MAGDALENA","LA Y","HUATABAMPO","ETCHOJOA","NAVOJOA","ALAMOS",
                "OBREGON", "GUAYMAS","EMPALME"};

        int i =0;
        while (!descripcion[i].equals(getDestino()) && i <= descripcion.length){
            i++;
        }

        return siglas[i];
    }

    public static void setOrigen(String origen) {
        Viaje.origen = origen;
    }

    public static String getDestino() {
        return destino;
    }

    public static void setDestino(String destino) {
        Viaje.destino = destino;
    }

    public static String getFechaSalidaDiaNumero() {
        return fechaSalidaDia;
    }

    public static void setFechaSalidaDia(String fechaSalidaD) {
        Viaje.fechaSalidaDia = fechaSalidaD;
    }

    public static String getFechaDiaSemana() {
        return fechaDiaSemana;
    }

    public static void setFechaDiaSemana(String fechaDiaSemana) {
        FormatoHorasFechas fh = new FormatoHorasFechas();

        String dia =  fh.sacarDiaSemana(
                   Integer.parseInt(getFechaSalidaYear()),
                   Integer.parseInt(getFechaSalidaMes()),
                   Integer.parseInt(getFechaSalidaDiaNumero()));

        Viaje.fechaDiaSemana = dia.substring(0,1).toUpperCase() + dia.substring(1);
    }

    public static String getFechaSalidaMes() {
        return fechaSalidaMes;
    }

    public static String getFechaSalidaMesNombre() {
        int mes = Integer.parseInt(fechaSalidaMes) - 1;
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (mes >= 0 && mes <= 11 ) {
            month = months[mes];
        }
        return month;
    }

    public static void setFechaSalidaMes(String fechaSalidaM) {
        Viaje.fechaSalidaMes = fechaSalidaM;
    }

    public static String getFechaSalidaYear() {
        return fechaSalidaYear;
    }

    public static void setFechaSalidaYear(String fechaSalidaYear) {
        Viaje.fechaSalidaYear = fechaSalidaYear;
    }

    public static String getHoraSalidaFormato24Militar() {
        return horaSalida;
    }

    public static String getHoraSalidaFormato12() {
        //Se cambia formato de horas
        FormatoHorasFechas formato = new FormatoHorasFechas();
        return formato.dosDigitosyAM_PM(getHoraSalidaFormato24Militar());

    }

    public static void setHoraSalida(String horaSalida) {
        Viaje.horaSalida = horaSalida;
    }

    public static int getTotalPasajeros() {
        return totalPasajeros;
    }

    public static void setTotalPasajeros(int totalPasajeros) {
        Viaje.totalPasajeros = totalPasajeros;
    }

    public static int getImporteTotal() {

        return pasajeroArrayList.get(1).getImporte() + pasajeroArrayList.get(2).getImporte() +
                pasajeroArrayList.get(3).getImporte() + pasajeroArrayList.get(4).getImporte();
    }

    public static String getCorrida() {
        return corrida;
    }

    public static void setCorrida(String corrida) {
        Viaje.corrida = corrida;
    }

    public static String getIdBoleto() {
        return idBoleto;
    }

    public static void setIdBoleto(String idBoleto) {
        Viaje.idBoleto = idBoleto;
    }

    public static long getTiempo() {
        return tiempo;
    }

    public static void setTiempo(long tiempo) {
        Viaje.tiempo = tiempo;
    }
}
