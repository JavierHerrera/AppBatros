package appbatros.solutions.com.mx.appbatros.objetos;

/**
 * Created by JH on 04/09/2017.
 */

public class Pasajeros {

    public static Pasajero pasajero1,pasajero2,pasajero3,pasajero4;
    private static String origen,destino;
    private static String fechaSalidaD, fechaSalidaM, fechaSalidaY;

    public static String getOrigen() {
        return origen;
    }

    public static void setOrigen(String origen) {
        Pasajeros.origen = origen;
    }

    public static String getDestino() {
        return destino;
    }

    public static void setDestino(String destino) {
        Pasajeros.destino = destino;
    }

    public static String getFechaSalidaD() {
        return fechaSalidaD;
    }

    public static void setFechaSalidaD(String fechaSalidaD) {
        Pasajeros.fechaSalidaD = fechaSalidaD;
    }

    public static String getFechaSalidaM() {
        return fechaSalidaM;
    }

    public static void setFechaSalidaM(String fechaSalidaM) {
        Pasajeros.fechaSalidaM = fechaSalidaM;
    }

    public static String getFechaSalidaY() {
        return fechaSalidaY;
    }

    public static void setFechaSalidaY(String fechaSalidaY) {
        Pasajeros.fechaSalidaY = fechaSalidaY;
    }
}
