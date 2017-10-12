package appbatros.solutions.com.mx.appbatros.objetos;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Objects;

import appbatros.solutions.com.mx.appbatros.extras.FormatoHorasFechas;

public class Viaje {

   // public  Pasajero pasajero1,pasajero2,pasajero3,pasajero4;
    private  String origen,destino;
    private  String horaSalida,  fechaSalidaDia, fechaSalidaMes, fechaSalidaYear, fechaDiaSemana;
    private  String corrida;
    private  String idBoleto;
    private  int totalPasajeros;
    public  ArrayList<Pasajero> pasajeroArrayList = new ArrayList<>(4);
    private  long tiempo = 300000;
    public  boolean redondo = false;
    public  Salidas salidas;


    public Viaje (){}


    public  int getTotalAdultos(){
        int total= 0;
        for (int i = 1; i <=totalPasajeros ; i++) {
            if(Objects.equals(pasajeroArrayList.get(i).getTipo(), "adulto")){ total = total + 1;}
        }
        return total;
    }

    public  int getTotalEstudiantes(){
        int total= 0;
        for (int i = 1; i <=totalPasajeros ; i++) {
            if(Objects.equals(pasajeroArrayList.get(i).getTipo(), "estudiante")){ total = total + 1;}
        }
        return total;
    }

    public  int getTotalMenores(){
        int total= 0;
        for (int i = 1; i <=totalPasajeros ; i++) {
            if(Objects.equals(pasajeroArrayList.get(i).getTipo(), "nino")){ total = total + 1;}
        }
        return total;
    }

    public  int getTotalInsen(){
        int total= 0;
        for (int i = 1; i <=totalPasajeros ; i++) {
            if(Objects.equals(pasajeroArrayList.get(i).getTipo(), "insen")){ total = total + 1;}
        }
        return total;
    }

    public  String getOrigen() {
        return origen;
    }

    public  String getOrigenSiglas() {

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

    public  String getDestinoSiglas() {

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

    public  void setOrigen(String origen) {
        this.origen = origen;
    }

    public  String getDestino() {
        return destino;
    }

    public  void setDestino(String destino) {
        this.destino = destino;
    }

    public  String getFechaSalidaDiaNumero() {
        return fechaSalidaDia;
    }

    public  void setFechaSalidaDia(String fechaSalidaD) {
        this.fechaSalidaDia = fechaSalidaD;
    }

    public  String getFechaDiaSemana() {
        return fechaDiaSemana;
    }

    public  void setFechaDiaSemana(String fechaDiaSemana) {
        FormatoHorasFechas fh = new FormatoHorasFechas();

        String dia =  fh.sacarDiaSemana(
                   Integer.parseInt(getFechaSalidaYear()),
                   Integer.parseInt(getFechaSalidaMes()),
                   Integer.parseInt(getFechaSalidaDiaNumero()));

        this.fechaDiaSemana = dia.substring(0,1).toUpperCase() + dia.substring(1);
    }

    public  String getFechaSalidaMes() {
        return fechaSalidaMes;
    }

    public  String getFechaSalidaMesNombre() {
        int mes = Integer.parseInt(fechaSalidaMes) - 1;
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (mes >= 0 && mes <= 11 ) {
            month = months[mes];
        }
        return month;
    }

    public  void setFechaSalidaMes(String fechaSalidaM) {
        this.fechaSalidaMes = fechaSalidaM;
    }

    public  String getFechaSalidaYear() {
        return fechaSalidaYear;
    }

    public  void setFechaSalidaYear(String fechaSalidaYear) {
        this.fechaSalidaYear = fechaSalidaYear;
    }

    public  String getHoraSalidaFormato24Militar() {
        return horaSalida;
    }

    public  String getHoraSalidaFormato12() {
        //Se cambia formato de horas
        FormatoHorasFechas formato = new FormatoHorasFechas();
        return formato.dosDigitosyAM_PM(getHoraSalidaFormato24Militar());

    }

    public  void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public  int getTotalPasajeros() {
        return totalPasajeros;
    }

    public  void setTotalPasajeros(int totalPasajeros) {
        this.totalPasajeros = totalPasajeros;
    }

    public  int getImporteTotal() {

        return pasajeroArrayList.get(1).getImporte() + pasajeroArrayList.get(2).getImporte() +
                pasajeroArrayList.get(3).getImporte() + pasajeroArrayList.get(4).getImporte();
    }

    public  String getCorrida() {
        return corrida;
    }

    public  void setCorrida(String corrida) {
        this.corrida = corrida;
    }

    public  String getIdBoleto() {
        return idBoleto;
    }

    public  void setIdBoleto(String idBoleto) {
        this.idBoleto = idBoleto;
    }

    public  long getTiempo() {
        return tiempo;
    }

    public  void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }
}
