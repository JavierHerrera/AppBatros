package appbatros.solutions.com.mx.appbatros.objetos;

public class Historial {
    private String id;
    private String nombre;
    private String tipoPasajero;
    private String asiento;
    private String importe;
    private String origen;
    private String destino;
    private String fecha;
    private String hora;
    private String tipoPago;
    private String referencia;
    private String status;

    public Historial(String id, String nombre, String tipoPasajero, String asiento, String importe, String origen, String destino, String fecha, String hora, String tipoPago, String referencia, String status) {
        this.id = id;
        this.nombre = nombre;
        this.tipoPasajero = tipoPasajero.replace("nino", "niño");
        this.asiento = asiento;
        this.importe = importe;
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.hora = hora;
        this.tipoPago = tipoPago;
        this.referencia = referencia;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipoPasajero() {
        return tipoPasajero;
    }

    public String getAsiento() {
        return asiento;
    }

    public String getImporte() {
        return importe;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public String getReferencia() {
        return referencia;
    }

    public String getStatus() {
        return status;
    }
}
