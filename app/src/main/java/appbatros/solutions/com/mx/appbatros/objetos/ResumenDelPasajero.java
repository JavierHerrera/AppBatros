package appbatros.solutions.com.mx.appbatros.objetos;

/**
 * Created by JH on 04/10/2017.
 */

public class ResumenDelPasajero {

private String nombre,tipo,asiento,importe;

    public ResumenDelPasajero(String nombre, String tipo, String asiento, String importe) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.asiento = asiento;
        this.importe = importe;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getAsiento() {
        return asiento;
    }

    public String getImporte() {
        return importe;
    }
}
